/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.controls;
import cs.project.node.ManetNode;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Control that apply movement to node.
 * @author Stefano Pisciella
 */
public class ManetMovement implements Control{
    
    public ManetMovement(String prefix) {}
    
    /**
     * Every step (defined in the cfg), it moves nodes randomly according to their speed
     * @return always false
     */
    @Override
    public boolean execute() {
        ManetNode tmp;
        for (int i = 0; i < Network.size(); ++i) {

           tmp = (ManetNode)Network.get(i);
           
           double rndX = CommonState.r.nextDouble();
           double rndY = CommonState.r.nextDouble();

           tmp.x = (tmp.x + ((rndX > 0.5)? tmp.speed : -tmp.speed)) % ManetNode.size;
           tmp.y = (tmp.y + ((rndY > 0.5)? tmp.speed : -tmp.speed)) % ManetNode.size;

       }
        
        return false;
    }
    
}
