# SkipLists

This projects implements a very efficient data structure called Skip Lists. It is a multi-layered linked list data strucutre.
It performs insert, search and delete opertaions in O(log n). As the input grows larger, the relative efficiency is improved.
You can see more on skip lists on the wikipedia page: https://en.wikipedia.org/wiki/Skip_list

This project was implemented during the course work, Implementation of Data Structures and Allgorithms. Below is the project
description.

Ver 1.0: Thu, Feb 5: Initial description

Code base: Do not use any data structures from Java's library for lists.  
Write the code from scratch.

Deadline: 11:59 PM on Thu, Feb 26 (1st), 11:59 PM on Fri, Mar 13 (2nd).
No excellence credits will be awarded after the 1st deadline.


Implement the skip list data structure (see http://en.wikipedia.org/wiki/Skip_list).
Compare its performance with Java's TreeMap.
Details of driver program with input/output specifications will be given soon.  
The Skip list data structure will be discussed in class soon.
The interface is given below:


/** See  http://en.wikipedia.org/wiki/Skip_list
 */

import java.lang.Comparable;
import java.util.Iterator;

public interface SkipList<T extends Comparable<? super T>> {

    void add(T x);  // Add an element x to the list

    T ceiling(T x); // Least element that is >= x, or null if no such element

    boolean contains(T x);  // Is x in the list?

    T findIndex(int n);  // Return the element at index n in the list

    T first();  // Return the first element of the list

    T floor(T x);  // Greatest element that is <= x, or null if no such element

    boolean isEmpty();  // Is the list empty?

    Iterator<T> iterator();  // Returns an iterator for the list

    T last();  // Return the last element of the list

    void rebuild();  // Rebuild this list into a perfect skip list

    boolean remove(T x);  // Remove x from this list; returns false if x is not in list

    int size();  // Number of elements in the list
}
