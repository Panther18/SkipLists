import java.lang.Comparable;
import java.util.Iterator;

/**
 * This interface lists all the methods that a Skip List should perform
 * Skip Lists can accept any data type that is Comparable
 * @author Panther
 *
 * @param <T> The Skip Lists enforces a constraint that only elements that are Comparable can be stored
 */
public interface SkipList<T extends Comparable<? super T>> {

	/**
	 * Add an element of type T to the Skip List
	 * @param x - Add this element to Skip List
	 */
    void add(T x);  // Add an element x to the list
    
    /**
     * This functions finds the ceiling of a given number in a Skip List
     * @param x - Element whose ceiling value has to be found
     * @return - Returns an element Y where Y is the smallest element that is greater than or equal to x.
     * 	Otherwise, it returns null
     */
    T ceiling(T x); // Least element that is >= x, or null if no such element

    /**
     * This function checks if a given element is present in the list
     * @param x - Element that has to be searched for in Skip List
     * @return - Returns True if the element is found. Otherwise, this returns False.
     */
    boolean contains(T x);  // Is x in the list?

    /**
     * This functions finds the element at a given index in the Skip List
     * @param n - The index position of the element we want to retrieve
     * @return - Returns the element of type T at index
     */
    T findIndex(int n);  // Return the element at index n in the list

    /**
     * This function returns the first element of the Skip List
     * @return - First element of the list. If the Skip List has zero elements, it returns zero.
     */
    T first();  // Return the first element of the list

    /**
     * This functions finds the floor of a given number in a Skip List
     * @param x - Element whose ceiling value has to be found
     * @return - Returns an element Y where Y is the greatest element that is lesser than or equal to x.
     */
    T floor(T x);  // Greatest element that is <= x, or null if no such element

    /**
     * This function checks if the Skip List is empty
     * @return - True, if the Skip List is empty. Otherwise, it returns False
     */
    boolean isEmpty();  // Is the list empty?

    /**
     * This function gives access to an iterator that can be used to traverse the entire Skip List
     * @return - An iterator of type T
     */
    Iterator<T> iterator();  // Returns an iterator for the list

    /**
     * This function returns the last element of the Skip List
     * @return - Last element of the Skip List. If the size of the Skip List is zero, it returns null
     */
    T last();  // Return the last element of the list

    /**
     * This function rebuilds the entire Skip List to make the search possible in O(log n) time
     */
    void rebuild();  // Rebuild this list into a perfect Skip List

    /**
     * This function removes an element that equals x in the Skip List
     * @param x - The element we would want to remove from Skip List
     * @return - True if the element is successfully removed. Otherwise, this returns False
     */
    boolean remove(T x);  // Remove x from this list; returns false if x is not in list

    /**
     * This function returns the size of the Skip List.
     * @return - Size of the Skip List
     */
    int size();  // Number of elements in the list
}