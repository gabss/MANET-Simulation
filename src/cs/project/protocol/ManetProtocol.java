/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/

package cs.project.protocol;

import cs.project.node.ManetNode;
import cs.project.observers.ManetObserver;
import cs.project.util.Geometry;
import java.util.ArrayList;
import java.util.List;
import peersim.cdsim.CDProtocol;
import peersim.core.Node;

/**
 * @author Stefano Pisciella
 */
public class ManetProtocol implements CDProtocol{

    
    
    public ManetProtocol(String prefix){  
    }
    
    
    @Override
    public ManetProtocol clone(){
        ManetProtocol dolly = null;
	try { dolly = (ManetProtocol) super.clone(); }
	catch( CloneNotSupportedException e ) {}
        
        return dolly;
	
    }
    /**
     * Check the neighbors to find if someone has going out of trasmission area
     * and invoke the control strategy to find new connections
     * 
     * @param node
     * @param protocolID 
     */
    @Override
    public void nextCycle(Node node, int protocolID) {
       
        ManetNode thisNode = (ManetNode) node;
        ManetNode tmpNode;
        List<ManetNode> nodeToRemove = new ArrayList<>();
        for(int i=0; i<thisNode.localNet.getNeighbors().size(); i++){
            tmpNode = thisNode.localNet.getNeighbors().get(i);
            if( Geometry.torusDistance(thisNode.x, thisNode.y, tmpNode.x, tmpNode.y, ManetNode.size) >= ManetNode.radius ){
                tmpNode.localNet.removeNode(thisNode);
                nodeToRemove.add(tmpNode);
                ManetObserver.edge--;
            }
        }
        for (ManetNode cNode : nodeToRemove){
            thisNode.localNet.removeNode(cNode);
        }
        
        thisNode.cs.cycleCheck();
    }
    
    
    
}
