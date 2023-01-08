
import static org.junit.jupiter.api.Assertions.*;

import jdk.jfr.TransitionFrom;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.w3c.dom.Node;

import javax.xml.stream.events.StartDocument;
import java.util.jar.JarEntry;

class FibonacciHeapTest {

    @Nested
    class deleteMin {
        FibonacciHeap heap;

        @BeforeEach
        void setUp(){
            buildHeap(10);
        }
        void buildHeap(Integer l) {
            buildHeap(l, l+1);
        }
        void buildHeap(Integer l, int consolidateEvery) {
            l = (l==null || l < 1)? 10 : l;
            FibonacciHeap.HeapNode min = new FibonacciHeap.HeapNode(0);
            min.next = min;
            min.prev = min;
            heap = new FibonacciHeap(min,min,1,1,0);
            for (int i = 1; i < l; i++) {
                var node = new FibonacciHeap.HeapNode(i);
                node.next = node;
                node.prev = node;
                var heap2 = new FibonacciHeap(node, node, 1,1,0);
                heap.meld(heap2);
                if (i % consolidateEvery == 0){
                    heap.consolidate();
                }
            }
        }
        @Test
        void testConsolidateFromLinkedList(){
            heap.consolidate();
            var min = heap.min;
            assertEquals(min.key , 0);
            assertEquals(min.child.key, 1);
            assertEquals(min.rank, 1);
            var start = min.prev;
            assertEquals(start.key, 2);
            assertEquals(start.rank, 3);
            assertEquals(start.child.key, 6);
            assertEquals(start.child.next.key, 4);
            testParenthoodAfterConsolidation();
            /* (0) ------>  (2)
             *  |          /
             * (1)       (6) -> (4) -> (3)
             *           /
             *         (8) -> (7) -> (5)
             *         /
             *       (9)
             */
        }
        void testParenthoodAfterConsolidation(){
            var parent = heap.treeListStart;
            while (parent.child != null){
                var child = parent.child;
                assertEquals(parent, child.parent, "Child failed");
                assertTrue(parent.key < child.key, "Child was smaller");
                var bro = child.next;
                while (bro != child){
                    assertEquals(parent, bro.parent, "Bro failed");
                    assertTrue(parent.key < bro.key, "Child was smaller");
                    bro = bro.next;
                }
                parent = child;
            }
        }
        @Test
        void testConsolidateAtDifferentLengths(){
            for (int i = 11; i < 10000; i++) {
                this.buildHeap(i);
                try {
                    testParenthoodAfterConsolidation();
                } catch (Exception e){
                    System.err.printf("Error at length  %d:%n", i);
                    System.err.println("    " + e.getMessage());
                }
            }
        }
        @Test
        void testDeletionFromLinkedList(){
            StringBuilder errors = new StringBuilder();
            for (int i = 10; i < 10000; i++) {
                buildHeap(i);
                try {
                    heap.deleteMin();
                    assertEquals(1, heap.min.key);
                } catch (Exception e){
                    errors.append("Error at length ").append(i).append("\n");
                    errors.append("    ").append(e.getMessage()).append("\n");
                }
            }
            assert errors.toString().equals("") : errors;
        }
        @Test
        void testDeletionFromTrees(){
            StringBuilder errors = new StringBuilder();
            for (var j : new int[] {2,3,5,7,11,13,17,19,23,29,31,37}) {
                for (int i = 10; i < 100; i++) {
                    buildHeap(i, j);
                    try {
                        heap.deleteMin();
                        assertEquals(1, heap.min.key);
                    } catch (Exception e) {
                        errors.append("Error at length ").append(i)
                                .append(" consolidate at ").append(j).append("\n");
                        errors.append("    ").append(e.getMessage()).append("\n");
                    }
                }
            }
            assert errors.toString().equals("") : errors;
        }
    }

    @Nested
    class findMin {
        @BeforeEach
        void setUp() {
        }
    }

    @Nested
    class meld {
        @BeforeEach
        void setUp() {
        }
    }

    @Nested
    class countersRep {
        @BeforeEach
        void setUp() {
        }
    }
}