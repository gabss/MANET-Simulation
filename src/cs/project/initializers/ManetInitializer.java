/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.initializers;

import cs.project.linkable.ManetNodeLinkable;
import cs.project.node.ManetNode;
import cs.project.protocol.strategy.AControlStrategy;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Initializer of the Manet's net.
 * @author Stefano Pisciella
 */
public class ManetInitializer implements Control{
    
    //Config par string
    private static final String PAR_RADIUS = "radius";
    private static final String PAR_SIZE = "fieldsize";
    private static final String PAR_MAX_DEGREE = "maxdegree";
    private static final String PAR_CONTROLSTRATEGY = "controlstrategy";
    private static final String PAR_MAXSPEED = "max_speed";
    private static final String PAR_MINSPEED = "min_speed";
    ///////////////////////////////////////7
    private final String csPrefix;
    private final double max_speed;
    private final double min_speed;
    
    public ManetInitializer(String prefix) {
        ManetNode.size = Configuration.getInt(prefix + "." + PAR_SIZE, 32);
        ManetNode.radius = Configuration.getDouble(prefix + "." + PAR_RADIUS, 1);
        ManetNode.max_degree = Configuration.getInt(prefix + "." + PAR_MAX_DEGREE);
        csPrefix = Configuration.getString(prefix + "." + PAR_CONTROLSTRATEGY, "RANDOM");
        max_speed = Configuration.getDouble(prefix + "." + PAR_MAXSPEED, 2);
        min_speed = Configuration.getDouble(prefix + "." + PAR_MINSPEED, 0);
        
    }
    
    /**
     * Configure all the node with randomic position, random speed, local empty net and a control strategy
     * @return always false
     */
    @Override
    public boolean execute() {
        for (int i = 0; i < Network.size(); ++i) {
           ManetNode node = (ManetNode)Network.get(i);
           node.x = CommonState.r.nextInt(ManetNode.size);
           node.y = CommonState.r.nextInt(ManetNode.size);
           node.localNet = new ManetNodeLinkable();
           node.cs = AControlStrategy.getStrategy(node, csPrefix);
           
           //Speed
           node.speed = CommonState.r.nextDouble() * (max_speed - min_speed) + min_speed;
       }

        return false;
    }
    
}
