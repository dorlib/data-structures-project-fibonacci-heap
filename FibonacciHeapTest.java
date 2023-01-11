
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.util.stream.IntStream;

class FibonacciHeapTest {
    Random random = new Random(69420);
    FibonacciHeap heap;
    static final int[] PRIMES = new int[] {2,3,5,7,11,13,17,19,23,29,31,37};
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
            heap2.meld(heap);
            heap = heap2;
            if (i % consolidateEvery == 0){
                heap.consolidate();
            }
        }
    }

    /**
     * @return an array of all numbers in the heap in order of insertion
     */
    int[] randomInserts(int i, Set<Integer> inHeap){
        ArrayList<Integer> lst = new ArrayList<>();
        while(lst.size() < i){
            int randint = random.nextInt(-100*i,100*i);
            if (!inHeap.contains(randint)){
                lst.add(randint);
                inHeap.add(randint);
            }
        }
        int[] arr = new int[lst.size()];
        for (int j = 0; j < lst.size(); j++) {
            heap.insert(lst.get(j));
            arr[j]=lst.get(j);
            if (random.nextInt() % 2 == 0){
                heap.consolidate();
            }
        }
        return arr;
    }

    int[] randomInserts(int i){
        Set<Integer> inHeap = new HashSet<>();
        return randomInserts(i, inHeap);
    }


    /*void printHeap(FibonacciHeap h){
        StringBuilder[] output = new StringBuilder[h.treeCount];
        int i = 0;
        for (FibonacciHeap.HeapNode tree: h.treeListStart) {
            output[i] = new StringBuilder("(%d)\n  |\n".formatted(tree.key));
            output[i].append(printTree(tree.child));
        }
        System.out.println(Arrays.toString(output));
    }
    String printTree(FibonacciHeap.HeapNode node){
        if (node == null){
            return "";
        }
        StringBuilder output = new StringBuilder();
        for (FibonacciHeap.HeapNode bro : node) {
            output.append("(%d)->".formatted(bro.key));
        }
        int len = output.length();
        output.replace(len-2, len-1, "\n  |\n");
        output.append(printTree(node.child));
        return output.toString();
    }*/

    @Nested
    class deleteMin {

        @BeforeEach
        void setUp(){
            buildHeap(10);
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
                buildHeap(i);
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
            for (int i = 10; i < 1000; i++) {
                buildHeap(i);
                for (int j = 1; j < i; j++) {
                    try {
                        heap.deleteMin();
                        assertEquals(j, heap.min.key);
                    } catch (Exception e){
                        errors.append("Error at length ").append(i).append("\n");
                        errors.append("    ").append(e.getMessage()).append("\n");
                    }
                }
            }
            assert errors.toString().equals("") : errors;
        }
        @Test
        void testDeletionFromTrees(){
            StringBuilder errors = new StringBuilder();
            for (var j : PRIMES) {
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
        @Test
        void binomialTree8(){
            buildHeap(8);
            heap.consolidate();
            heap.deleteMin();
            assertEquals(heap.min.key, 1);
            assertEquals(heap.treeCount, 3);
        }
        @Test
        void binomialTree2(){
            buildHeap(2);
            assertEquals(heap.treeCount, 2);
            heap.consolidate();
            heap.deleteMin();
            assertEquals(heap.min.key, 1);
            assertEquals(heap.treeCount, 1);
        }
    }

    @Nested
    class kMin {
        void buildBinomial(int k){
            buildHeap((int) Math.pow(2,k));
            heap.consolidate();
        }

        @Test
        void testKMin(){
            for (int i = 0; i < 8; i++) {
                buildBinomial(i);
                int[] ret;
                int[] expected;
                for (int j = 0; j < Math.pow(2,i); j++) {
                    try {
                        ret = FibonacciHeap.kMin(heap, j);
                    } catch (Exception e){
                        System.err.printf("at iteration i = %d, j = %d%n", i,j);
                        throw e;
                    }
                    expected = IntStream.range(0,j).toArray();
                    assertArrayEquals(expected, ret,
                            "i = %d, j = %d\n ret = %s\n".formatted(i,j,Arrays.toString(ret))
                    );
                }
            }
        }
    }

    @Nested
    class meld {

        @Test
        public void oneEmpty(){
            buildHeap(11); // set this.heap
            var emptyHeap = new FibonacciHeap();

            heap.meld(emptyHeap);
            assertEquals(11, heap.size);
            var node = heap.treeListStart;
            for (int i = 10; i >= 0; i--) {
                assertEquals(i, node.key);
                node = node.next;
            }

            emptyHeap = new FibonacciHeap();
            emptyHeap.meld(heap);
            assertEquals(11, emptyHeap.size);
            node = emptyHeap.treeListStart;
            for (int i = 10; i >= 0; i--) {
                assertEquals(i, node.key);
                node = node.next;
            }
        }

        @Test
        public void bothEmpty(){
            heap = new FibonacciHeap();
            var heap2 = new FibonacciHeap();
            heap.meld(heap2);
            assertEquals(0, heap.size);
            assertEquals(0, heap.markedCount);
            assertEquals(0,heap.treeCount);
            assertNull(heap.min);
            assertNull(heap.treeListStart);
        }
    }

    @Nested
    class countersRep {
        @Test
        void empty() {
            heap = new FibonacciHeap();
            int[] arr = heap.countersRep();
            assertArrayEquals(new int[] {}, arr);
        }

        @Test
        void linkedList() {
            for (int i = 10; i < 100; i++) {
                heap = new FibonacciHeap();
                buildHeap(i);
                int[] arr = heap.countersRep();
                assertEquals(1, arr.length, "Iteration %d".formatted(i));
                assertEquals(i, arr[0]);
            }
        }

        @Test
        void binomialHeap(){
            for (int i = 10; i < 1000; i++) {
                heap = new FibonacciHeap();
                buildHeap(i);
                heap.consolidate();
                testArray(i);
            }
        }

        @Test
        void lazyBinomialHeap(){
            for (int j : PRIMES) {
                for (int i = 10; i < 1000; i++) {
                    heap = new FibonacciHeap();
                    buildHeap(i,j);
                    testArray(i);
                }
            }
        }

        private void testArray(int i) {
            int[] arr = heap.countersRep();
            int sum = 0;
            int multiplier = 1;
            for (int bit: arr) {
                sum += multiplier * bit;
                multiplier = multiplier * 2;
            }
            assertEquals(heap.size, sum, "Iteration %d".formatted(i));
        }
    }

    @Nested
    class findMin{
        @Test
        void minWasInsertedLongAgo() {
            final int N = 1000;
            for (int i = 0; i < N; i++) {
                int large = N*N;
                heap = new FibonacciHeap();
                heap.insert(large);
                heap.insert(large+1);
                heap.insert(-large);
                int[] itemsInHeap = randomInserts(i);
                assertEquals(-large, heap.min.key);
                assertEquals(-large, heap.findMin().key);
            }
        }
    }

    @Nested
    class insert{
        @Test
        void insertRandomNumbers(){
            // long test
            for (int i = 1; i < 5000; i++) {
                heap = new FibonacciHeap();
                int[] itemsInHeap = randomInserts(i);
                assertEquals(heap.size, i, "iteration i = %d\n".formatted(i));
                Arrays.sort(itemsInHeap);
                int[] fromHeap = new int[i];
                int delCount = 0;
                for (int j = 0; j < i; j++) {
                    fromHeap[j] = heap.treeListStart().key;
                    heap.deleteMin();
                    delCount++;
                    assertEquals(heap.size, i - delCount);
                }
                assertArrayEquals(itemsInHeap, fromHeap, "iteration i = %d\n".formatted(i));
            }
        }
    }

    @Nested
    class decreaseKey{
        @Test
        void simpleCase(){
            var heap2 = new FibonacciHeap();
            heap = new FibonacciHeap();
            Set<Integer> integerSet = new HashSet<>(Arrays.stream(randomInserts(1000)).boxed().toList());
            heap2.insert(555);
            heap.meld(heap2);
            integerSet.add(555);
            var node = heap2.min;
            randomInserts(1000, integerSet);
            assertEquals(0, heap.getCutCount());
            int oldTreeCount = heap.treeCount;
            heap.decreaseKey(node, 93185);
            assertTrue(heap.getCutCount() > 0);
            assertEquals(oldTreeCount + 1, heap.treeCount);
        }

        @Test
        void binomialTree(){
            buildHeap((int) Math.pow(2, 4));
            heap.consolidate();
            var oldmin = heap.min;
            var node = heap.min.child;
            heap.decreaseKey(node, 1000000);
            assertEquals(heap.min, node);
            assertEquals(2, heap.treeCount);
            assertNotSame(node, oldmin.child);
            assertNotSame(node, oldmin.child.next.prev);
            assertNotSame(node, oldmin.child.prev.next);
            assertFalse(node.isMarked());
            assertFalse(oldmin.isMarked());

            buildHeap((int) Math.pow(2, 5));
            heap.consolidate();
            var root = heap.treeListStart;
            var child1 = root.child;
            var child2 = child1.child;
            var child3 = child2.child;
            var child4 = child3.child;
            heap.decreaseKey(child4, 900000);
            assertTrue(child3.isMarked());
            heap.decreaseKey(child3, 900000);
            assertFalse(child3.isMarked());
            assertTrue(child2.isMarked());
            heap.decreaseKey(child2, 900000);
            assertFalse(child2.isMarked());
            assertTrue(child1.isMarked());
            heap.decreaseKey(child1, 900000);
            assertFalse(child1.isMarked());
            assertFalse(root.isMarked());
        }
    }

    @Nested
    class Iterator{
        @Test
        void simpleCase(){
            FibonacciHeap.HeapNode start = new FibonacciHeap.HeapNode(0);
            FibonacciHeap.HeapNode node = start;
            for (int i = 1; i < 10; i++) {
                node.next = new FibonacciHeap.HeapNode(i);
                node.next.prev = node;
                node = node.next;
            }
            node.next = start;
            start.prev = node;

            for (FibonacciHeap.HeapNode item: new FibonacciHeap.IterableNode(start)) {
                assertEquals(start, item);
                assertInstanceOf(FibonacciHeap.HeapNode.class, item);
                assertNotSame(FibonacciHeap.IterableNode.class, item.getClass());
                start = start.next;
            }
        }
    }
}