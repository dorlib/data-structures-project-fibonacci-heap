import java.util.Iterator;

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
    // fields of FibonacciHeap.
    public HeapNode min;
    private HeapNode treeListStart;
    public int treeCount;
    public int size;
    public int linkCount;
    public int cutCount;
    public int markedCount;

    /**
     * Construct a node from data
     * @param min
     * @param treeListStart
     * @param treeCount
     * @param size
     * @param linkCount
     * @param cutCount
     * @param markedCount
     */
    public FibonacciHeap(HeapNode min,
                         HeapNode treeListStart,
                         int treeCount,
                         int size,
                         int linkCount,
                         int cutCount,
                         int markedCount) {
        this.min = min;
        this.treeListStart = treeListStart;
        this.treeCount = treeCount;
        this.size = size;
        this.linkCount = linkCount;
        this.cutCount = cutCount;
        this.markedCount = markedCount;

    }

    /**
     * Constructs a heap using another heap data
     * @post: new heap point to the old heap nodes
     * @param heap heap to construct from
     */
    public FibonacciHeap(FibonacciHeap heap){
        this(
                heap.min,
                heap.treeListStart,
                heap.treeCount,
                heap.size,
                heap.linkCount,
                heap.cutCount,
                heap.markedCount
        );
    }

    /**
     * Default constructor, creates an empty heap.
     */
    public FibonacciHeap(){
        this(null, null, 0,0,0,0,0);
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
            this.treeListStart = newNode;
            this.size = 1;

            return newNode;
        }

        // insert the created node to the heap if the heap is not empty.
        HeapNode newNode = this.createNode(key, 0, false, null, null, this.treeListStart, this.treeListStart.prev);
        this.treeListStart.prev.next = newNode;
        this.treeListStart.prev = newNode;
        this.treeListStart = newNode;

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
        deleteMinNoUpdate();
        this.min = findMin();
        // TODO
    }

    private void consolidate(){
        // all trees but one has rank 0, the last tree has them all as children
        int maxRank = size - (treeCount - 1);
        HeapNode[] buckets = new HeapNode[maxRank];
        HeapNode firstInNewList = null;

        for (HeapNode tree : treeListStart) {
            int rank = tree.rank;
            if (buckets[rank] == null){
                // bucket is empty
                buckets[rank] = tree;
                continue; // avoid nested ifs
            }
            //else
            // bucket is filled, connect nodes;
            HeapNode connected = connectTrees(buckets[rank], tree);
            // empty the bucket
            buckets[rank] = null;
            if (firstInNewList == null){
                firstInNewList = connected;
                firstInNewList.next = firstInNewList;
                firstInNewList.prev = firstInNewList;
            }
            else{
                // add to new list
                HeapNode last =  firstInNewList.prev;
                last.next = connected;
                connected.prev = last;
                connected.next = firstInNewList;
                firstInNewList.prev = connected;
            }
        }
    }

    /**
     * Connect two trees such that the smaller one is at the top
     * @param tree1 and
     * @param tree2 will be connected
     */
    private HeapNode connectTrees(HeapNode tree1, HeapNode tree2){
        // pointer assignment for readability
        HeapNode smaller = tree1.key < tree2.key? tree1 : tree2;
        HeapNode larger = tree1.key < tree2.key? tree2 : tree1;
        HeapNode childStart = smaller.child;
        HeapNode childEnd = childStart.prev;

        // insert larger as the first child in the list
        smaller.child = larger;
        larger.next = childStart; childStart.prev = larger;
        larger.prev = childEnd;   childEnd.next = larger;
        return smaller;
    }

    /**
     * delete minimum and don't update the minimum pointer
     */
    private void deleteMinNoUpdate(){
        // TODO
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin()
    {
        if (this.isEmpty()){
            return null;
        }
    	HeapNode min = this.min;
        for (HeapNode node : this.treeListStart) {
            if (node.key < min.key){
                min = node;
            }
        }
        return min;
    }

    private void updateMin(){
        this.min = findMin();
    }

    /**
     * _INCREASES_ counter fields by the given values.
     * @pre: values can be negative to decrease.
     */
    private void addToCounters(int treeCount,
                               int size,
                               int linkCount,
                               int cutCount,
                               int markedCount){
        this.treeCount += treeCount;
        this.size += size;
        this.linkCount += linkCount;
        this.cutCount += cutCount;
        this.markedCount += markedCount;
    }

   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
        if (heap2.isEmpty()){
            return;
        }
        if (this.isEmpty() || heap2.min.key < this.min.key){
            this.min = heap2.min;
        }
        this.addToStartOfTreeList(heap2.treeListStart);
        this.addToCounters(
                heap2.treeCount,
                heap2.size,
                heap2.linkCount,
                heap2.cutCount,
                heap2.markedCount);
    }

    /**
     * Adds to the start of trees list
     * @param startHeap2 node at the start of the new list (can be one long)
     *                   Should have correct prev and next pointers
     */
    private void addToStartOfTreeList(HeapNode startHeap2){
        var startOrigin = this.treeListStart;
        var lastOrigin = startOrigin.prev;
        var lastHeap2 = startHeap2.prev;
        // concatenate
        startOrigin.prev = lastHeap2;
        lastHeap2.next = startOrigin;
        lastOrigin.next = startHeap2;
        startHeap2.prev = lastOrigin;
        // update pointer
        this.treeListStart = startHeap2;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.size; // should be replaced by student code
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of the array depends on the maximum order of a tree.)
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
        if (x == this.min) {
            this.deleteMin();
        }
    	var oldMin = this.min;
        this.decreaseKey(x, x.key - oldMin.key + 1);
        this.deleteMinNoUpdate();
        this.min = oldMin;
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

    private static void setLinks(int value){
        linkCount
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
    public static class HeapNode implements Iterable<HeapNode>{

        // fields of HeapNode.
    	public int key;
        public int rank;
        public boolean mark;
        public HeapNode child;
        public HeapNode parent;
        public HeapNode next;
        public HeapNode prev;

       /**
        * Construct a node from data
        * @param key
        * @param rank
        * @param mark
        * @param child
        * @param parent
        * @param next
        * @param prev
        */
    	public HeapNode(int key,
                        int rank,
                        boolean mark,
                        HeapNode child,
                        HeapNode parent,
                        HeapNode next,
                        HeapNode prev) {
            this.key = key;
            this.rank = rank;
            this.mark = mark;
            this.child = child;
            this.parent = parent;
            this.next = next;
            this.prev = prev;
    	}

       /**
        * Constructs a node with key
        * @param key to be stored in the node
        */
        public HeapNode(int key){
            this(key, 0, false, null, null, null, null);
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

        public Iterator<HeapNode> iterator(){
           return new TreesIterator(this);
        }
    }

    private static class TreesIterator implements Iterator<HeapNode>{

        HeapNode currentNode;
        HeapNode firstNode;
        public TreesIterator(HeapNode start){
            this.currentNode = start;
            this.firstNode = start;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null && currentNode.next != firstNode;
        }

        @Override
        public HeapNode next() {
            return currentNode.next;
        }
    }
}
