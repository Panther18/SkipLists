
/**
 * @author Pavan Yadiki
 * This class implements all the methods listed in SkipList interface
 * This program is developed as a part of academic projects for the Implementation of Data Structures and Algorithms course
 */

import java.util.Iterator;
import java.lang.Math;
import java.util.Random;

public class SkipListImpl<T extends Comparable<? super T>> implements SkipList<T> {

	Node<T> head, tail;
	int size; // Holds the size of the skip list
	int maxLevel;  //  The current maximum level of a Skip List
	int newLevelSize;  //  The size of the list at which current maximum level should be increased by 1
	Random rand;  //  Random object used to choose the level of a node in Skip List
	RebuildReturns<T> r;  //  Auxiliary object used during rebuild()
	/**
	 * This function initializes the head and tail pointers of the Skip List
	 * Sets the maximum level equal to the value of (log 100)[Base 2].
	 * Maximum Level of the Skip List is dynamically increased with the growing size
	 */
	public SkipListImpl() {
		// TODO Auto-generated constructor stub
		int expectedSize = 1000000;
		int expectedLevel = (int) (Math.log10(expectedSize)/Math.log10(2));  // Level in an ideal case is log(n)
		head = new Node<T>(expectedLevel, null);
		tail = new Node<T>(expectedLevel, null);
		maxLevel = expectedLevel;
		for(int i = 0; i < expectedLevel; i++){
			head.next[i] = tail;
		}
		rand = new Random();
		newLevelSize = expectedSize;
		r= new RebuildReturns<>(null, 0);
	}

	/**
	 * This class defines the data that a Skip List holds. Data can be of any data type that is Comparable
	 * Also defines the array of Node pointers each of which point to the Node after them.
	 * Each Node pointer is associated with a integer value width which indicates the number of elements skipped when this link is used
	 * @author Pavan Yadiki
	 *
	 * @param <E> - A value of Data type can that is stored in the Node. E can be any data type that is comparable
	 */
	private class Node<E extends Comparable<? super E>>{
		E data;
		Node<E>[] next;
		int[] width;
		int NodeLevel;
		@SuppressWarnings("unchecked")
		/**
		 * This constructor sets the data of the node
		 * @param level - Indicates the level of this node
		 * @param data - Value that this Node holds
		 */
		Node(int level, E data){
			this.data = data;
			next = new Node[level];
			width = new int[level];
			NodeLevel = level;
		}

		private void updateLevel(){
			//  Declare a temporary array equal to the size of the # of active nodes
			@SuppressWarnings("unchecked")
			Node<E>[] dummy = new Node[NodeLevel];
			int[] dummy_width = new int[NodeLevel];
			for(int i = 0; i < NodeLevel; i++){
				dummy[i] = next[i];
				dummy_width[i] = width[i];
			}
			width = dummy_width;
			next = dummy;
		}
	}

	/**
	 * This class lists the values that are returned when a search function is called
	 * It returns the Node, whose value we are searching and the the list of all the Nodes at which the level is descended
	 * @author Pavan Yadiki
	 *
	 * @param <V> - This accepts any data type that is comparable
	 */
	private class Contains<V extends Comparable<? super V>>{
		Node<V> element;
		Node<V>[] nodesTraversed;
		int maxSearchSize;
		@SuppressWarnings("unchecked")
		public Contains() {
			// TODO Auto-generated constructor stub
			maxSearchSize = maxLevel;
			nodesTraversed = new Node[maxSearchSize];
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			String toReturn = "";
			for(int i = 0; i < nodesTraversed.length; i++){
				toReturn += "\t" + nodesTraversed[i];
			}
			return toReturn;
		}
	}

	private class RebuildReturns<P extends Comparable<? super P>>{
		Node<P> nextOne;
		int weight;
		public RebuildReturns(Node<P> temp, int w) {
			// TODO Auto-generated constructor stub
			nextOne = temp;
			weight = w;
		}
	}
	/**
	 * This function either increases/decreases the size of the Skip List
	 * @param update - Valid values for this argument are "increase" (increases size by 1) and "decreases" (decreases size by 1)
	 */
	private void updateSize(String update){
		if(update.compareToIgnoreCase("increase") == 0){
			size++;
		}
		else{
			size--;
		}
	}

	@Override
	/**
	 * @see {@link com.my.package.Class#method()}
	 */

	public void add(T x) {
		// TODO Auto-generated method stub
		Contains<T> c = search(x);
		int level = choice();
		if(c.element == null){
			Node<T> newNode = new Node<>(level, x);
			for(int move = 0; move < level; move++){
				newNode.next[move] = c.nodesTraversed[move].next[move];
				c.nodesTraversed[move].next[move] = newNode;
			}
			updateWidths(newNode, c);
			updateSize("increase");
			increaseLevel();
		}
		else{
			try {
				//	throw new Exception("Element already exists");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method increases the maximum level a new node can take.
	 * 		1. Increases the current maximum level by 1
	 * 		2. Create a new Next[] arrays for head and tail with # of levels = currentMaxLevel + 1
	 * 		3. Copy the content from old next array of head to the new next array head
	 * @author Pavan Yadiki
	 */
	private void increaseLevel(){
		
		//  Reinitialize the next array of head and tail to increase the max. level
		if(size == newLevelSize){  //  Increase the max. Level by 1 and reset all the tail pointers
			@SuppressWarnings("unchecked")
			Node<T>[] headLevel = new Node[maxLevel + 1];
			int[] headWidth = new int[maxLevel + 1];
			@SuppressWarnings("unchecked")
			Node<T>[] tailLevel = new Node[maxLevel + 1];
			int currentMaxLevel = maxLevel;
			for(int level = 0; level < currentMaxLevel; level++){  //  Copy the references from the current head
				headLevel[level] = head.next[level];
				headWidth[level] = head.width[level];  //  Copy the width's from the previous head
			}
			headLevel[maxLevel] = tail;
			headWidth[maxLevel] = 0;  //  Since it points to the tail, it's width is 0
			head.next = headLevel;  //  Set the new Next[] to the head
			head.width = headWidth;  //  Set the new width[] corresponding to each link of the head
			tail.next = tailLevel;
			maxLevel += 1; //  Increase the maximum level by 1
			newLevelSize *= 2;
		}
	}

	/**
	 * This method updates the width of all the links affected by the insertion of newNode
	 * Assume you are adding node 'B' after 'A' (to write comments with an example)
	 * @param newNode - Newly added node
	 * @param c - Returned by the search(newNode.data)
	 * @author Pavan Yadiki
	 */
	private void updateWidths(Node<T> newNode, Contains<T> c){
		int prevNodeLevel = c.nodesTraversed[0].width.length;
		int newNodeLevel = newNode.width.length;
		int travel = 0;
		int commonLevel = Math.min(prevNodeLevel, newNodeLevel);  //  Find the minimum of prevNodeLevel and newNodeLevel
		travel = 0;
		for(; travel < commonLevel; travel++){
			// For all the same levels of A and B, make the level of A to 1 and the current widths of levels in 'A' will go the corresponding level of 'B'
			newNode.width[travel] = c.nodesTraversed[travel].width[travel];
			c.nodesTraversed[travel].width[travel] = 1;
		}
		travel = commonLevel;  //  Set the level to one above the common level

		if(commonLevel == newNodeLevel){  //  # of levels of newNode is either less or equal to the # of levels of previous node
			for(; travel < c.nodesTraversed.length; travel++){  //  For all the remaining levels of contains, increase the width by 1
				if(c.nodesTraversed[travel].next[travel] != tail){
					c.nodesTraversed[travel].width[travel]++;
				}
			}
		}
		else{  //  # of levels of newNode is greater than the # of levels of previous node
			for(; travel < newNodeLevel; travel++){  //  For all the remaining levels of newNode, find the width by finding the sum of width of below levels
				int top = travel-1;
				Node<T> move = c.nodesTraversed[travel];
				int tempWidth = 0; 
				while(move != newNode){
					tempWidth += move.width[top];
					move = move.next[top];

				}
				int currentWidth = c.nodesTraversed[travel].width[travel];
				if(currentWidth == 0) {  //  If the current width is 0, make it 1 
					c.nodesTraversed[travel].width[travel] = tempWidth;
					newNode.width[travel] = 0;
				} 
				else{
					currentWidth++;
					c.nodesTraversed[travel].width[travel] = tempWidth;
					newNode.width[travel] = currentWidth - tempWidth;					
				}
			}
			travel = newNodeLevel;
			for(; travel < c.nodesTraversed.length; travel++){  //  These are the links that fly over this newNode and also does not point to tail
				if(c.nodesTraversed[travel].next[travel] != tail){
					c.nodesTraversed[travel].width[travel]++;
				}
			}
		}
	}

	/**
	 * This function chooses a random level that a new node can take. Level is equal to the number of 1's generated before first 0
	 * @return - Level chooses randomly
	 */
	private int choice(){
		int level = 1;
		while (level < maxLevel - 1){
			if(rand.nextInt(2) == 1){
				level++;
			}
			else{
				break;
			}
		}
		return level;
	}

	@Override
	public T ceiling(T x) {
		// TODO Auto-generated method stub
		Contains<T> c = search(x);
		if(c.element != null){
			return c.element.data;
		}
		else{
			if(c.nodesTraversed[0].next[0] != null){
				return c.nodesTraversed[0].next[0].data;
			}
			else{
				return null;
			}
		}
	}

	@Override
	public boolean contains(T x) {
		// TODO Auto-generated method stub
		Contains<T> result = search(x);
		if(result.element == null){
			return false;
		}
		return true;
	}
	/**
	 * This function searches if an element 'x' is present in the list.
	 * @param x - The element we want to search
	 * @return An object of Contains
	 * 
	 */
	private Contains<T> search(T x){
		Contains<T> c = new Contains<>();
		Node<T> move;
		move = head;
		int top = maxLevel-1;
		for(; top >=0; top--){  //  Move from top level to downwards whenever 'x' is less than the data of the cur. list
			if(move.next[top] != tail){
				while(move.next[top].data.compareTo(x) < 0) {
					move = move.next[top];
					if(move.next[top] == tail){ break; }  // Break if the node's next points to null
				}
				c.nodesTraversed[top] = move;			
			}
			else{
				c.nodesTraversed[top] = move;	
			}
		}

		if(move.next[0] != tail){ //  If the next node is not tail, check if the element is matching
			if(move.next[0].data.compareTo(x) == 0){
				c.element = move.next[0];
				return c;
			}
			else{
				c.element = null;
				return c;
			}	
		}
		else{
			c.element = null;
			return c;
		}
	}

	@Override
	public T findIndex(int n) {
		// TODO Auto-generated method stub
		Node<T> travel = head;
		n += 1;  //  Increase n by 1 (Range of index for user is 0 - (size - 1))
		int remainingIndex = n, level = -1;
		if(size < n || n < 0){
			return null;  //  Index position is more than the # of elements present and should be >= 0
		}
		outerLoop: for(int top = maxLevel - 1; top >= 0; top--){
			if(travel.next[top] != tail){
				while( remainingIndex >= travel.width[top]){
					remainingIndex -= travel.width[top];
					if(remainingIndex == 0){ level = top; break outerLoop; }
					travel = travel.next[top];
					if(travel.next[top] == tail){ break; }
				}				
			}
		}
		if(remainingIndex == 0){
			return travel.next[level].data;
		}
		else{
			return null;
		}
	}

	@Override
	public T first() {
		// TODO Auto-generated method stub
		if(size != 0){
			return head.next[0].data;
		}
		else{
			return null;
		}
	}

	/*
	 * @see SkipList#floor(java.lang.Comparable)
	 */
	@Override
	public T floor(T x) {
		// TODO Auto-generated method stub
		Contains<T> c = search(x);
		if(c.element != null){
			return c.element.data;
		}
		else{
			return c.nodesTraversed[0].data;
		}
	}

	/*
	 * @see SkipList#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (size == 0);
	}

	/*
	 * @see SkipList#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return new skipListIterator();
	}

	/**
	 * This class implements the Iterator interface to traverse the Skip List
	 * @author Pavan Yadiki
	 */
	private class skipListIterator implements Iterator<T>{
		Node<T> nextNode;
		public skipListIterator() {
			// TODO Auto-generated constructor stub
			nextNode = head;
		}
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return (nextNode.next[0] != tail);
		}
		@Override
		public T next() {
			// TODO Auto-generated method stub
			nextNode = nextNode.next[0];
			return nextNode.data;
		}
	}

	/*
	 * @see SkipList#last()
	 */
	@Override
	public T last() {
		// TODO Auto-generated method stub
		Node<T> move = head;
		int top = maxLevel-1;
		for(; top >=0; top--){
			while(move.next[top] != tail)
				move = move.next[top];
		}
		return move.data;
	}

	/**
	 * This function returns the next node that has to be pointed by the node 'from' during rebuild
	 * Typically this node is two leaps away from its below level
	 * @param from - Node from where we should find the next node
	 * @param level - The level of the next pointer which has to be set
	 * @return - The reference of the new node which will be pointed to
	 */
	private void getNextNode(Node<T> from, int level){
		//  Take two leaps from its previous level
		if(from.next[level-1] != tail && from.next[level - 1] != null){
			r.nextOne = from.next[level-1].next[level-1];
			r.weight = 2 * from.next[level-1].width[level - 1];
		}
		else{
			r.nextOne = from.next[level-1];
			r.weight = from.width[level-1];
		}
	}


	/**
	 * Idea:
	 * In the first iteration, get the reference to your immediate node
	 * Declare the # of levels of each node to be equal to log n
	 * Set your next[0] to your next node
	 * From second iteration, you are called 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void rebuild() {
		// TODO Auto-generated method stub
		//  First iteration
		//  Get the reference to your next pointer, and continue till you reach the tail
		Node<T> nextNode = head;
		Node<T> temp;
		int levelSize = (int) Math.ceil(Math.log10(size)/Math.log10(2));
		if(levelSize == 0){ levelSize++; } //  Set the levelSize equal to 1 if there are zero elements in the list currently
		while(nextNode != tail){
			temp = nextNode.next[0];  //  Get the reference to your immediate node
			nextNode.next = new Node[levelSize]; //  Declare the # of size of each array to be equal to log(size)
			nextNode.width = new int[levelSize]; //  Declare the array to hold the width of each link
			nextNode.next[0] = temp;  //  Point to your immediate node
			nextNode.NodeLevel = 1;  //  Set the active # levels of each node to one
			nextNode.width[0] = 1;  //  Set the width of the first link to 1 as it points to only one element
			nextNode = nextNode.next[0];  //  Go to the next node
		}
		tail.next = new Node[levelSize]; //  Declare the # of levels for tail
		//  Remaining iterations
		nextNode = head;
		temp = null;
		//		RebuildReturns<T> r;
		int levelsCompleted = 1;
		for(levelsCompleted = 1; levelsCompleted < levelSize; levelsCompleted++){  //  For all the remaining levels
			boolean bFoundTail = false;
			while(bFoundTail == false){
				getNextNode(nextNode, levelsCompleted);
				nextNode.next[levelsCompleted] = r.nextOne;  //  Point to this node
				nextNode.width[levelsCompleted] = r.weight;
				nextNode.NodeLevel++;  //  Increase the # of active levels of this node by one
				nextNode = r.nextOne;  //  Go to the next node
				if(nextNode == tail){
					nextNode = head;  //  Reset the reference to next node
					bFoundTail = true;  //  Break the loop to start with next level
				}
			}
		}
		//  Update the each of the nodes to have only the required number of levels
		temp = head;
		while(temp != tail){
			temp.updateLevel();
			temp = temp.next[0];
		}
		maxLevel = levelSize;  //  Update the new level size
	}

	@Override
	public boolean remove(T x) {
		// TODO Auto-generated method stub
		Contains<T> c = new Contains<>();
		c = search(x);
		if(c.element == null){
			return false;
		}
		else{
			updateSize("decrease");
			for(int move = 0; move < maxLevel; move++){
				if(c.nodesTraversed[move].next[move] == c.element){
					c.nodesTraversed[move].next[move] = c.element.next[move];
				}
			}
			return true;
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	/**
	 * Prints the Skip List
	 * Each Node prints the value it holds and the data of the nodes it points to
	 */
	public void print(){
		Node<T> travel = head;
		System.out.print("Head points to");
		for(int i = 0; i < head.next.length; i++){
			System.out.print("\t" + head.next[i].data);
		}
		System.out.print("My widths are: ");
		for(int i =0; i < travel.width.length; i++){
			System.out.print(travel.width[i] +"\t");
		}
		System.out.println();
		Iterator<T> move = this.iterator();
		while(move.hasNext()){
			try{
				travel = travel.next[0];
				if(travel != tail){
					System.out.print("My value is: " + travel.data + ". I point to:\t");
					for(int i=0; i < travel.NodeLevel; i++){
						if(travel.next[i] != tail){
							//if(travel.next[i] == null)
							System.out.print(travel.next[i].data + "\t");
						}
						else{
							System.out.print("tail\t");
						}
					}
					System.out.print("My widths are: ");
					for(int i =0; i < travel.width.length; i++){
						System.out.print(travel.width[i] +"\t");
					}
					System.out.println();
				}
				else{ break; }
			}
			catch (Exception e) {
				System.out.println("Exception");
				// TODO: handle exception
			}
		}

	}

	/**
	 * This function finds the element at a given index
	 * This function is designed to highlight the efficiency of the Skip Lists
	 * @param index - The index position of the Skip List
	 * @return - Element present at Index. If index is lesser than size, this returns null
	 */
	public T findIndex_LinearSearch(int index){
		Node<T> travel = head;
		for(int i=1; i < index; i++){
			travel = travel.next[0];
		}
		if(travel.next[0] != null){
			return travel.next[0].data;
		}
		return null;
	}
}

