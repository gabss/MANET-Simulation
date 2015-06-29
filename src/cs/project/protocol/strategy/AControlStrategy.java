/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/

package cs.project.protocol.strategy;

import cs.project.node.ManetNode;

/**
 * Abstract class the define the Control strategy for choose and remove connections
 * @author Stefano Pisciella
 */
public abstract class AControlStrategy {
    
    /**
     * Return the right control strategy (defined in the cfg)
     * @param node node to control
     * @param prefix string that define the control strategy to apply
     * @return the right control strategy
     */
    public static AControlStrategy getStrategy(ManetNode node, String prefix){
        AControlStrategy cs = null;
        switch(prefix){
            case "RANDOM":
                cs = new RandomControlStrategy(node);
                break;
            case "MAX_DEGREE":
                cs = new MaxDegreeControlStrategy(node);
                break;
            case "MIN_DISTANCE":
                cs = new DistanceControlStrategy(node, -1);
                break;
            case "MAX_DISTANCE":
                cs = new DistanceControlStrategy(node, 1);
                break;
        }
        
        return cs;
    }
    
    /**
     * Called every cycle, check if there are new connections to estabilish
    */
    public abstract void cycleCheck();
    
    /**
     * Test if is convenient to estabilish connection with passed node
     * @param nodeToAdd node that ask to be added at the node's neighbors
     * @return true, if the connection has been estabilished
     */
    public abstract boolean tryToAdd(ManetNode nodeToAdd);
    
    
}
