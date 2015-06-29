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
import peersim.core.CommonState;
import peersim.core.Network;

/**
 * Control strategy that remove and add nodes in a random manner
 * @author Stefano Pisciella
 */
public class RandomControlStrategy extends AControlStrategy{
    
    private ManetNode _node;
    
    protected RandomControlStrategy(ManetNode node){
        _node = node;
    }

    @Override
    public void cycleCheck() {
        ManetNode n;
        for(int i = 0; i<Network.size(); i++){
            n = (ManetNode)Network.get(i);
            if(n == _node || _node.localNet.contain(n)) continue;
            
            if(Geometry.torusDistance(_node.x, _node.y, n.x, n.y, ManetNode.size) < ManetNode.radius){
                if(n.cs.tryToAdd(_node)){
                    if(_node.localNet.size() >= ManetNode.max_degree){
                        ManetObserver.edge--;
                        _node.localNet.removeNode(i%_node.localNet.size())
                                .localNet.removeNode(_node);
                    }
                    _node.localNet.addNode(n);
                    ManetObserver.edge++;
                }
            }
        }
    }

    @Override
    public boolean tryToAdd(ManetNode nodeToAdd) {
        if(_node.localNet.size() >= ManetNode.max_degree){
            _node.localNet.removeNode(CommonState.r.nextInt(ManetNode.max_degree))
                                .localNet.removeNode(_node);
            ManetObserver.edge--;
        }
        _node.localNet.addNode(nodeToAdd);
        return true;
    }



    
    
}
