/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.linkable;

import java.util.List;

/**
 * Generic interface for linkable object
 * @author Stefano Pisciella
 */
public interface ILinkable<T> {
    
    /**
     * Check if the item T is in the neighbors list
     * @param t the item to check
     * @return true if t it's in the neighbors list.
     */
    public boolean contain(T t);
    /**
     * Add item T to the neighbors
     * @param t the item to add
     * @return true if the element has been added to the neighbors
     */
    public boolean addNode(T t);
    /**
     * Remove item T from neighbors
     * @param t the item to remove
     * @return true if the element has been removed from the neighbors
     */
    public boolean removeNode(T t);
    /**
     * Remove the i'th element of the neighbors
     * @param i the index of the element to remove
     * @return the removed element
     */
    public T removeNode(int i);
    /**
     * 
     * @return the size of neighbors' list
     */
    public int size();
    /**
     * 
     * @return the list of neighbors 
     */
    public List<T> getNeighbors();
    
}
