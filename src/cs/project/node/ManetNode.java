/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.node;

import cs.project.linkable.ManetNodeLinkable;
import cs.project.protocol.strategy.AControlStrategy;
import peersim.core.GeneralNode;

/**
 * Extends the class GeneralNode of PeerSim to create a prototype of a Manet's node
 * @author Stefano Pisciella
 */
public class ManetNode extends GeneralNode{
    
    //All manet's node have a fixed radius, size and max degree
    public static double radius = 1;
    public static int size = 32;
    public static int max_degree = 1;
    /////////////////////////////////
    //Local net of a node (the neighbors list)
    public ManetNodeLinkable localNet;
    //Coordinate of the node in the space
    public double x,y;
    public double speed;
    //Control strategy of the node see {@link cs.project.protocol.strategy.AControlStrategy}
    public AControlStrategy cs;
    ///////////////////////////////////////
    
    /**
     * The ManetNode will be initialized from the initializers see {@link cs.project.initializer.ManetInitializer}
     * @param prefix
     **/
    public ManetNode(String prefix) {
        super(prefix);
    }
    
}
