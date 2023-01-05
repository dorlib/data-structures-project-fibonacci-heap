/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    // fields of FibonacciHeap.
    public HeapNode min;
    public HeapNode lastNode;
    public int treeCount;
    public int size;
    public int linkCount;
    public int cutCount;
    public int markedCount;

    // FibonacciHeap's constructor.
    public FibonacciHeap(HeapNode min, HeapNode lastNode, int treeCount, int size, int linkCount, int cutCount, int markedCount) {
        this.min = min;
        this.lastNode = lastNode;
        this.treeCount = treeCount;
        this.size = size;
        this.linkCount = linkCount;
        this.cutCount = cutCount;
        this.markedCount = markedCount;
    }

    public HeapNode getMin() {
        return this.min;
    }

    public int getTreeCount() {
        return this.treeCount;
    }

    public int getSize() {
        return this.size;
    }

    public int getLinkCount() {
        return this.linkCount;
    }

    public int getCutCount() {
        return this.cutCount;
    }

    public int getMarkedCount() {
        return this.markedCount;
    }

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() {
    	return this.getSize() > 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key) {
        // insert the created node to the heap if the heap is empty.
        if (this.isEmpty()) {
            HeapNode newNode = this.createNode(key, 0, false, null, null, null, null);
            newNode.next = newNode;
            newNode.prev = newNode;

            this.min = newNode;
            this.lastNode = newNode;
            this.size = 1;

            return newNode;
        }

        // insert the created node to the heap if the heap is not empty.
        HeapNode newNode = this.createNode(key, 0, false, null, null, this.lastNode, this.lastNode.prev);
        this.lastNode.prev.next = newNode;
        this.lastNode.prev = newNode;
        this.lastNode = newNode;

        this.updateMin(newNode);
        this.size++;

        return newNode;
    }

    private HeapNode createNode(int key, int rank, boolean mark, HeapNode child, HeapNode parent, HeapNode next, HeapNode prev) {
        return new HeapNode(key, rank, mark, child, parent, next, prev);
    }

    private void updateMin(HeapNode newNode) {
        if (newNode.key< this.min.key) {
            this.min = newNode;
        }
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	return; // should be replaced by student code
     	
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
    	if (this.isEmpty()) {
            return null;
        }

        return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return -123; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    */
    public int nonMarked() 
    {    
        return -232; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
        return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

        // fields of HeapNode.
    	public int key;
        public int rank;
        public boolean mark;
        public HeapNode child;
        public HeapNode parent;
        public HeapNode next;
        public HeapNode prev;

        // HeapNode's
    	public HeapNode(int key, int rank, boolean mark, HeapNode child, HeapNode parent, HeapNode next, HeapNode prev) {
            this.key = key;
            this.rank = rank;
            this.mark = mark;
            this.child = child;
            this.parent = parent;
            this.next = next;
            this.prev = prev;
    	}

    	public int getKey() {
            return this.key;
    	}

        public int getRank() {
            return this.rank;
        }

        public boolean isMarked() {
            return this.mark;
        }

        public HeapNode getChild() {
            return this.child;
        }

        public HeapNode getParent() {
            return this.parent;
        }

        public HeapNode getNext() {
            return this.next;
        }

        public  HeapNode getPrev() {
            return this.prev;
        }
    }
}
