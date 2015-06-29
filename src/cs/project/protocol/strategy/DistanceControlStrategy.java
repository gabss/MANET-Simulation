/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/

package cs.project.protocol.strategy;

import cs.project.node.ManetNode;
import cs.project.observers.ManetObserver;
import cs.project.util.Geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import peersim.core.Network;

/**
 * Control strategy that incorporates two different strategy:
 *  - MAX_DISTANCE, prefer to add nearest nodes and to remove the farther ones
 *  - MIN_DISTANCE, prefer to add farther nodes and to remove the nearest ones
 * @author Stefano Pisciella
 */
public class DistanceControlStrategy extends AControlStrategy{
    
    private ManetNode _node;

    private Comparator<ManetNode> comp;
    
    protected DistanceControlStrategy(ManetNode node, final int minmax){
        _node = node;
        
        comp = new Comparator<ManetNode>(){
            @Override
            public int compare(ManetNode nod1, ManetNode nod2){
                return minmax * Double.compare( Geometry.torusDistance(_node.x, _node.y, nod1.x, nod1.y, ManetNode.size),
                                                Geometry.torusDistance(_node.x, _node.y, nod2.x, nod2.y, ManetNode.size));
            }
        };
    }
    
    @Override
    public void cycleCheck() {
        ManetNode n;
        List<ManetNode> possibleAdd = new ArrayList<>();
        for(int i = 0; i<Network.size(); i++){
            n = (ManetNode)Network.get(i);
            if(n == _node || _node.localNet.contain(n)) continue;
            if(Geometry.torusDistance(_node.x, _node.y, n.x, n.y, ManetNode.size) < ManetNode.radius){
                possibleAdd.add(n);
            }
        }
        
        Collections.sort(possibleAdd, comp);
        
        for(int i=0; i<possibleAdd.size(); i++){
            if(_node.localNet.size() < ManetNode.max_degree){
                if(possibleAdd.get(i).cs.tryToAdd(_node)){
                    ManetObserver.edge++;
                    _node.localNet.addNode(possibleAdd.get(i));
                }
            }else{
                List<ManetNode> neighbors = _node.localNet.getNeighbors();
                Collections.sort(neighbors, comp);
                if(comp.compare(neighbors.get(neighbors.size()-1), possibleAdd.get(i)) > 0){
                    if(possibleAdd.get(i).cs.tryToAdd(_node)){
                        _node.localNet.removeNode(_node.localNet.size()-1).localNet.removeNode(_node);
                        _node.localNet.addNode(possibleAdd.get(i));
                    }
                }else
                    break;
            }
        }
    }

    @Override
    public boolean tryToAdd(ManetNode nodeToAdd) {
        if(_node.localNet.size() >= ManetNode.max_degree){
            Collections.sort(_node.localNet.getNeighbors(), comp);
            if(comp.compare(_node.localNet.getNeighbors().get(_node.localNet.size()-1), nodeToAdd) > 0){
                ManetObserver.edge--;
                _node.localNet.removeNode(_node.localNet.size()-1).localNet.removeNode(_node);
            }else
                return false;
        }
        _node.localNet.addNode(nodeToAdd);
        return true;
    }
    
}
