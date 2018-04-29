package courseworkpart3;

public class findCentrallity {
    public static void main(String[] args) {
        
        Graph G = new Graph();
        
        //adds the edges
        
        G.addEdges();
        
        //bfs in order to find the paths
        
        G.bfs();
        
        //compute times visited
        
        G.timesVisited();
        
        //find bridge centrality
        
        G.findHighestCentrallity();
        
    }//end of main
    
}//end of class
