/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.util;

/**
 * Util class for geometric operation
 * @author Stefano Pisciella
 */
public class Geometry {
    
    /**
     * Compute the distance between two points in a torus field
     * @param aX x of the first point
     * @param aY y of the first point
     * @param bX x of the second point
     * @param bY y of the second point
     * @param size the field size (square)
     * @return the distance between the points (aX, aY), (bX, bY)
     */
    public static double torusDistance(double aX, double aY, double bX, double bY, int size){
        return  Math.sqrt(  Math.pow( Math.min(Math.abs(aX-aY), size - Math.abs(aX-aY)), 2) +
                            Math.pow( Math.min(Math.abs(aY-bY), size - Math.abs(aY-bY)), 2)   );
        
    }
    
}
