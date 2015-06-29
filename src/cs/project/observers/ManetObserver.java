/**********************************************************
 **********************************************************
 * **************** Stefano Pisciella ******************* *
 * ********* stefano.pisciela@studio.unibo.it *********** *
 * ******************* 0000739593 *********************** *
 **********************************************************
 **********************************************************/
package cs.project.observers;

import cs.project.node.ManetNode;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Observer for compute the statistics of the net
 * @author Stefano Pisciella
 */
public class ManetObserver implements Control {

    //Count the number of edges in the net
    public static int edge = 0;
    //Needed for the count of bridges
    //private List<List<ManetNode>> components = new ArrayList<>();
    private List<ManetNode> cGiantComponent;
    private boolean check = false;
    
    //Vertex struct used in the diameter's algorithm
    class Vertex implements Comparable<Vertex>{
        int index;
        int dist;
        
        Vertex(int index, int dist){
            this.index = index;
            this.dist = dist;
        }
        
        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(dist, o.dist);
        }
        
        @Override
        public boolean equals(Object o){
            return ((Vertex)o).index == this.index;
        }
        
    }
    
    
    public ManetObserver(String prefix) {
        edge = 0;
        new File("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size).mkdir();
    }

    /**
     * print the statistical snapshot of the current situation
     * @return boolean
     */
    @Override
    public boolean execute() {
        //Don't keep statistics at init
        if(check){
            int sz = Network.size();


            edgeDensity(sz);
            giantComponent(sz);
            giantComponentDiameter();
           // bridge();
            clustering();
            degreeDistribution(sz);
            voidOperation();
            
        }else {
            check = true;
            randomSeed();
        }
        return false;
    }
    private void voidOperation(){
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/void");
        System.out.println("p");
    }
    /**
     * Save the seed of the random generator of the current expirement
     */
    private void randomSeed(){
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/seed");
        System.out.println(CommonState.r.getLastSeed());
    }
    /**
     * Save the clustering coefficient of the giant component
     */
    private void clustering(){
        float[] nodeClusteringCoeff = new float[cGiantComponent.size()];
        for (ManetNode cNode : cGiantComponent){
            List<ManetNode> neighbors = cNode.localNet.getNeighbors();
            int cont = 0;
            for(ManetNode node : neighbors){
                for(ManetNode cFriend : node.localNet.getNeighbors()){
                    if(neighbors.contains(cFriend))
                        cont++;
                }
            }
            if(neighbors.size() > 1)
                nodeClusteringCoeff[cGiantComponent.indexOf(cNode)] = ((float)(cont/2))/((((float)neighbors.size())*(neighbors.size()-1))/2);
            else
                nodeClusteringCoeff[cGiantComponent.indexOf(cNode)] = 0;
        }
        float avg = 0;
        for (int i = 0; i<cGiantComponent.size(); i++){
            avg += nodeClusteringCoeff[i];
        }
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/clustering");
        System.out.println("{"+avg/cGiantComponent.size()+"}");
    }
    
    /**
     * Print the number of bridges in the net
     */
    /*
    private void bridge(){
        int bridge = 0;
        
        for (List<ManetNode> cComponent : components){
            for(int j = 0; j<cComponent.size(); j++){
                ManetNode node = cComponent.get(j);
                ManetNode tmp;
                int size = node.localNet.size();
                for(int i = 0; i<size; i++){
                    tmp = node.localNet.getNeighbors().get(i);
                    node.localNet.removeNode(i).localNet.removeNode(node);
                    if(!isComponent(new ArrayList<>(cComponent))) bridge++;
                    node.localNet.getNeighbors().add(i, tmp);
                    tmp.localNet.addNode(node);
                }
            }
        }
        components.clear();
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/bridge");
        
        System.out.println("{"+bridge/2+"}");
    }
    */
    /**
     * Verify if the passed list of manet nodes is connected
     * @param nodes the list to check
     * @return true if nodes is connected
     */
    private boolean isComponent(List<ManetNode> nodes){
        if(nodes.size() > 1){
            Stack<ManetNode> elements = new Stack<>();
            elements.addAll(nodes.get(0).localNet.getNeighbors());
            nodes.remove(0);
            while(!elements.empty()){
                ManetNode cNode = elements.pop();
                if(!nodes.remove(cNode)) continue;
                elements.addAll(cNode.localNet.getNeighbors());
            }
            if(nodes.isEmpty()) return true;
        }
        return false;
    }
    
    /**
     * Print the degree distribution of the net
     * @param sz size of net
     */
    private void degreeDistribution(int sz){
        int[] degDist = new int[ManetNode.max_degree+1];
        
        for(int i=0; i<sz; i++){
            degDist[((ManetNode)Network.get(i)).localNet.size()]++;
        }
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/degreedist");
       
        System.out.print("{{"+degDist[0]+","+0+"}");
        for (int i=1; i<degDist.length; i++){
            System.out.print(",{"+degDist[i]+","+i+"}");
        }
        System.out.println("}");
    }
    
    /**
     * Print the edge density of the net
     * @param sz size of the net
     */
    private void edgeDensity(int sz){
        int maxEdge = (sz*ManetNode.max_degree)/2;
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/edgedens");

        System.out.println("{"+(float)edge/maxEdge+", "+edge+"}");
    }
    /**
     * Print the number of nodes in the giant component of the net
     * @param sz size of net
     */
    private void giantComponent(int sz){
        int maxComponent = 0;
        int numOfComponent = 0;
        Map<Integer, ManetNode> visitedNode = new HashMap<>();
        ManetNode tmpNode;
        
        for(int i=0; i<sz; i++){
            if(visitedNode.containsKey(i)) continue;
            int count = 1;
            List<ManetNode> cComponent = new ArrayList<>();
            numOfComponent++;
            tmpNode = (ManetNode) Network.get(i);
            cComponent.add(tmpNode);
            visitedNode.put(i, tmpNode);
            Stack<ManetNode> nodeToVisit = new Stack<>();
            nodeToVisit.addAll(tmpNode.localNet.getNeighbors());
            while(!nodeToVisit.empty()){
                tmpNode = nodeToVisit.pop();
                if(visitedNode.containsKey(tmpNode.getIndex())) continue;
                cComponent.add(tmpNode);
                visitedNode.put(tmpNode.getIndex(), tmpNode);
                count++;
                nodeToVisit.addAll(tmpNode.localNet.getNeighbors());
            }
            if(maxComponent< count){
                cGiantComponent = cComponent;
                maxComponent = count;
            }
        }
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/giantcomp");

        System.out.println(maxComponent);
    }
    /**
     * Save the diameter of the giant component
     */
    private void giantComponentDiameter(){
        int diameter = 0;
        List<Vertex> distance = new ArrayList<>();
        Queue<Vertex> unvisited = new PriorityQueue<>();
        
        for(ManetNode node : cGiantComponent){
            distance.clear();
            unvisited.clear();
            Vertex src = new Vertex(node.getIndex(), 0);
            distance.add(src);
            unvisited.add(src);
            //init
            for(ManetNode cNode : cGiantComponent){
                if(node != cNode){
                    Vertex vert = new Vertex(cNode.getIndex(), Integer.MAX_VALUE);
                    distance.add(vert);
                    unvisited.add(vert);
                }
            }
            //
            while(!unvisited.isEmpty()){
                Vertex u = unvisited.poll();
                for(ManetNode neighbor : ((ManetNode)Network.get(u.index)).localNet.getNeighbors()){
                    Vertex vert = distance.get(distance.indexOf(new Vertex(neighbor.getIndex(), 0)));
                    if(unvisited.contains(vert)){
                        int alt = u.dist + 1;
                        if(alt < vert.dist){
                            vert.dist = alt;
                            unvisited.remove(vert);
                            unvisited.add(vert);
                        }
                    }
                }
            }
            
            for (Vertex v : distance){
                if (v.dist > diameter) diameter = v.dist;
            }
        }
        
        
        outputRedirect("result-node"+Network.size()+"-rad"+ManetNode.radius+"-deg"+ManetNode.max_degree+"-size"+ManetNode.size+"/diameter");

        System.out.println("{"+diameter+"}");
    }
    
    
    /**
     * Util for redirect the system's out into a file
     * @param fileName the name of the file where redirect the output
     */
    private void outputRedirect(String fileName){
        try {
            System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName, true)),true));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManetObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

