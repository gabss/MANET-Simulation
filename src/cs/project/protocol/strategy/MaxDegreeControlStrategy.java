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
 * Control strategy that prefer to add nodes with smaller degree insted of the ones with bigger degree.
 * @author Stefano Pisciella
 */
public class MaxDegreeControlStrategy extends AControlStrategy{
    
    private ManetNode _node;
    private Comparator<ManetNode> comp = new Comparator<ManetNode>(){
            @Override
            public int compare(ManetNode x, ManetNode y){
                return Integer.compare(x.localNet.size(), y.localNet.size());
            }
        };
    protected MaxDegreeControlStrategy(ManetNode node){
        _node = node;
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
                if(neighbors.get(neighbors.size()-1).localNet.size()>possibleAdd.get(i).localNet.size()){
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
            if(_node.localNet.getNeighbors().get(_node.localNet.size()-1).localNet.size() > nodeToAdd.localNet.size()){
                ManetObserver.edge--;
                _node.localNet.removeNode(_node.localNet.size()-1).localNet.removeNode(_node);
            }else
                return false;
        }
        _node.localNet.addNode(nodeToAdd);
        return true;
    }
    
}
