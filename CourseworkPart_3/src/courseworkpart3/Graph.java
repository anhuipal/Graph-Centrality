package courseworkpart3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    
    private HashMap<Vertex, TreeSet<Vertex>> AdjList;
    private HashMap<String, Vertex> Vertices;
    private HashMap<String,Integer> Paths;
    private static final TreeSet<Vertex> EMPTY_SET = new TreeSet<Vertex>();
    private String[] edges;
    private int NumVertices;
    private int NumEdges;
    private int numPaths;
       
    public Graph() {
        
        AdjList = new HashMap<Vertex, TreeSet<Vertex>>();
        Vertices = new HashMap<String, Vertex>();
        Paths = new HashMap<String,Integer>();
        NumVertices = NumEdges = 0;

    }//end of constructor
    
    /**
     * This method reads the edge list for the graph from the file "edgeList.txt"
     */
    public void readFile(){
    
        try{
        
            FileReader fileReader = new FileReader("edgeList.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String result = "";
            
            while((line =bufferedReader.readLine())!=null){
                for(int i = 0;i<3;i+=2){
                    result += String.valueOf(line.charAt(i));
                }//inner loop
            }//outer loop
            
            edges = new String[result.length()/2];
            
            int j = 0;
            for(int i = 0;i<result.length()/2;i++){
                edges[i] = String.valueOf(result.charAt(j)) + String.valueOf(result.charAt(j+1));
                j+=2;
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }//end of readFile
    
    /**
     *Adds a vertex if it doensn't exist
     * @param name of the vertex
     * @return the vertex added
     */
    public Vertex addVertex(String name) {
            Vertex v;
            v = Vertices.get(name);
            if (v == null) {
                    v = new Vertex(name);
                    Vertices.put(name, v);
                    AdjList.put(v, new TreeSet<Vertex>());
                    Paths.put(v.name, 0);
                    NumVertices += 1;
            }
            return v;
    }

	/**
	 * Returns the Vertex matching v
	 * @param name a String name of a Vertex that may be in
	 * this Graph
	 * @return the Vertex with a name that matches v or null
	 * if no such Vertex exists in this Graph
	 */
	public Vertex getVertex(String name) {
		return Vertices.get(name);
	}

	/**
	 * Returns true iff v is in this Graph, false otherwise
	 * @param name a String name of a Vertex that may be in
	 * this Graph
	 * @return true iff v is in this Graph
	 */
	public boolean hasVertex(String name) {
		return Vertices.containsKey(name);
	}

	/**
	 * Is from-to, an edge in this Graph. The graph is 
	 * undirected so the order of from and to does not
	 * matter.
	 * @param from the name of the first Vertex
	 * @param to the name of the second Vertex
	 * @return true iff from-to exists in this Graph
	 */
	public boolean hasEdge(String from, String to) {

		if (!hasVertex(from) || !hasVertex(to))
			return false;
		return AdjList.get(Vertices.get(from)).contains(Vertices.get(to));
	}
	
	/**
	 * Add to to from's set of neighbors, and add from to to's
	 * set of neighbors. Does not add an edge if another edge
	 * already exists
	 * @param from the name of the first Vertex
	 * @param to the name of the second Vertex
	 */
        
    public void addEdge(String from, String to) {
        Vertex v, w;
        if (hasEdge(from, to))
                return;
        NumEdges += 1;
        if ((v = getVertex(from)) == null)
                v = addVertex(from);
        if ((w = getVertex(to)) == null)
                w = addVertex(to);
        AdjList.get(v).add(w);
        AdjList.get(w).add(v);
    }//end of addEdge
        
    public void addEdges() {
        this.readFile();
        for(int i = 0; i<edges.length;i++){
            this.addEdge(String.valueOf(edges[i].charAt(0)), String.valueOf(edges[i].charAt(1)));
        }
    }//end of addEdges
    
    /**
     * Call the bfs for every source and target in the graph
     */
    public void bfs(){
        for(Vertex v : this.getVertices()){
            for(Vertex u : this.getVertices()){
                if(!(v.name.equals(u.name))){
                    findPaths(v.name,u.name);
                }
            }
        }
    }//end of bfs
    
    /**
     *This method finds all the shortest paths from every source to every other node
     * @param sourceVertex the source vertix
     * @param targetVertex the target vertix
     */
    public void findPaths(String sourceVertex, String targetVertex) {
        
        Queue<String> q = new LinkedList<String>();
        
        Vertex source = new Vertex(sourceVertex);
        Vertex target = new Vertex(targetVertex);
        Vertex current = new Vertex("");
        
        q.add(source.name);
        current.predecessor = source;
        
        while (!q.isEmpty()) {
            
            current.predecessor.name=q.peek();
            current.name = q.poll();
            current.marked=false;
            
            for(Vertex path : this.getVertices()){

                if(current.name.equals(target.name) || this.hasEdge(current.name, target.name)){
                   
                    //System.out.println("ADDED : " + current.name);
                    Vertices.get(current.name).timesVisited++;
                    //System.out.println("path FOUND" + " to :" + target.name);
                    numPaths++;
                    return;
                    
                }
                else{
                    if(this.hasEdge(current.name, path.name) && current.marked!=true){
                        
                        q.add(path.name);
                        //System.out.println("ADDED : " + current.name);
                        Vertices.get(current.name).timesVisited++;
                        current.marked=true;
                        
                         for(Vertex neighbor : this.adjacentTo(path)){
                            if(!(neighbor.name.equals(source.name))){
                                q.add(neighbor.name);
                            }
                        }//neighbor
                    }
                }
            }//inner loop
        }//outer loop
    }//end of findPaths
    
    /**
     * Find the number of times that a node has being visited
    **/
    public void timesVisited(){
        for(Vertex v: this.getVertices()){
            Paths.put(v.name, v.timesVisited);
            //System.out.println(v.name + ": " + v.timesVisited); // debugging mesage 
        }
    }
    
    /**
     * Finds the centrallity of a node
     */
    public void findHighestCentrallity(){
        int max = 0;
        Vertex v  = new Vertex("");
        for(String s : Paths.keySet()){
            if(max<Paths.get(s)){
                max = Paths.get(s);
                v.name=s;
            }
        }
        if(max!=0){
            v.centrality=(double)max/numPaths;
        }
        
        System.out.println("The node " + v.name + " has the highest centrallity of degree : " + v.centrality);
        
    }//end of findHighestCentrallity
   
	/**
	 * Return an iterator over the neighbors of Vertex named v
	 * @param name the String name of a Vertex
	 * @return an Iterator over Vertices that are adjacent
	 * to the Vertex named v, empty set if v is not in graph
	 */
    public Iterable<Vertex> adjacentTo(String name) {
        if (!hasVertex(name))
                return EMPTY_SET;
        return AdjList.get(getVertex(name));
    }

    /**
     * Return an iterator over the neighbors of Vertex v
     * @param v the Vertex
     * @return an Iterator over Vertices that are adjacent
     * to the Vertex v, empty set if v is not in graph
     */
    public Iterable<Vertex> adjacentTo(Vertex v) {
        if (!AdjList.containsKey(v))
                return EMPTY_SET;
        return AdjList.get(v);
    }

    /**
     * Returns an Iterator over all Vertices in this Graph
     * @return an Iterator over all Vertices in this Graph
     */
    public Iterable<Vertex> getVertices() {
        return Vertices.values();
    }

    public int numVertices(){
        return NumVertices;
    }
    
    public int numEdges(){
        return NumEdges;
    }
   
    public String toString() {
        String s = "";
        for (Vertex v : Vertices.values()) {
            s += v + ": ";
            for (Vertex w : AdjList.get(v)) {
                    s += w + " ";
            }
            s += "\n";
        }
        return s;
    }//end of toString
}//end of class