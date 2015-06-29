/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.linkable;

import cs.project.node.ManetNode;
import java.util.ArrayList;
import java.util.List;

/**
 * General implementation of ILinkable interface with ManetNode
 * @author Stefano Pisciella
 */
public class ManetNodeLinkable implements ILinkable<ManetNode>{
    
    List<ManetNode> _neighbors = new ArrayList<>();
    
    
    @Override
    public boolean contain(ManetNode t){
        return _neighbors.contains(t);
    }
    
    @Override
    public boolean addNode(ManetNode t) {
        if(!_neighbors.contains(t))
            return _neighbors.add(t);
        return false;
    }

    @Override
    public boolean removeNode(ManetNode t) {
        return _neighbors.remove(t);
    }
    
    @Override
    public ManetNode removeNode(int i) {
        return _neighbors.remove(i);
    }

    @Override
    public int size() {
        return _neighbors.size();
    }

    @Override
    public List<ManetNode> getNeighbors() {
        return _neighbors;
    }
    
}
