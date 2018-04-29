package courseworkpart3;
    
public class Vertex implements Comparable<Vertex> {
    
    public String name;  
    public int distance; 
    public Vertex predecessor;
    public double centrality;
    public int timesVisited;
    public boolean marked;
    public static final int INFINITY = Integer.MAX_VALUE;
    
    public Vertex(String v){
            name = v;
            distance = INFINITY;
            predecessor = null;
            centrality = 0.0;
            timesVisited = 0;
            marked=false;
    }//end of constructor
    
    public int hashCode(){
            return name.hashCode();
    }
    
    public String toString(){ 
            return name;
    }
    /**
     * Compare on the basis of distance from source first and 
     * then lexicographically
     */
    public int compareTo(Vertex other)
    {
            int diff = distance - other.distance;
            if (diff != 0)
                    return diff;
            else
                    return name.compareTo(other.name);
    }
}//end of class
