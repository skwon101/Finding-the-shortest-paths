
import java.util.*;

public class WeightedGraph<V> extends AbstractGraph<V> {

  public WeightedGraph() {
  }
  
  public WeightedGraph(V[] vertices, int[][] edges) {
    createWeightedGraph(java.util.Arrays.asList(vertices), edges);
  }

  public WeightedGraph(int[][] edges, int numberOfVertices) {
    List<V> vertices = new ArrayList<>();
    for (int i = 0; i < numberOfVertices; i++)
      vertices.add((V)(new Integer(i)));
    
    createWeightedGraph(vertices, edges);
  }

  public WeightedGraph(List<V> vertices, List<WeightedEdge> edges) {
    createWeightedGraph(vertices, edges);
  }

  public WeightedGraph(List<WeightedEdge> edges,
      int numberOfVertices) {
    List<V> vertices = new ArrayList<>();
    for (int i = 0; i < numberOfVertices; i++)
      vertices.add((V)(new Integer(i)));
    
    createWeightedGraph(vertices, edges);
  }

  private void createWeightedGraph(List<V> vertices, int[][] edges) {
    this.vertices = vertices;     

    for (int i = 0; i < vertices.size(); i++) {
      neighbors.add(new ArrayList<Edge>()); // Create a list for vertices
    }

    for (int i = 0; i < edges.length; i++) {
      neighbors.get(edges[i][0]).add(
        new WeightedEdge(edges[i][0], edges[i][1], edges[i][2], edges[i][3]));
    }
  }

  private void createWeightedGraph(
      List<V> vertices, List<WeightedEdge> edges) {
    this.vertices = vertices;     

    for (int i = 0; i < vertices.size(); i++) {
      neighbors.add(new ArrayList<Edge>()); // Create a list for vertices
    }

    for (WeightedEdge edge: edges) {      
      neighbors.get(edge.u).add(edge); // Add an edge into the list
    }
  }

  public int getCost(int u, int v) throws Exception {
    for (Edge edge : neighbors.get(u)) {
      if (edge.v == v) {
        return ((WeightedEdge)edge).cost;
      }
    }
    
    throw new Exception("Edge does not exit");
  }
  
  public int getTime(int u, int v) throws Exception {
	    for (Edge edge : neighbors.get(u)) {
	      if (edge.v == v) {
	        return ((WeightedEdge)edge).time;
	      }
	    }
	    
	    throw new Exception("Edge does not exit");
	  }
  
  public boolean addEdge(int u, int v, int cost, int time) {
    return addEdge(new WeightedEdge(u, v, cost, time));
  }

  public ShortestPathTree getShortestPath(int sourceVertex) {
    double[] cost = new double[getSize()];
    for (int i = 0; i < cost.length; i++) {
      cost[i] = Double.POSITIVE_INFINITY; 
    }
    cost[sourceVertex] = 0;

    int[] parent = new int[getSize()];
    parent[sourceVertex] = -1;
    
    List<Integer> T = new ArrayList<>();
    
    while (T.size() < getSize()) {
      int u = -1; 
      double currentMinCost = Double.POSITIVE_INFINITY;
      for (int i = 0; i < getSize(); i++) {
        if (!T.contains(i) && cost[i] < currentMinCost) {
          currentMinCost = cost[i];
          u = i;
        }
      }
      
      T.add(u); 
      
      for (Edge e : neighbors.get(u)) {
        if (!T.contains(e.v) 
            && cost[e.v] > cost[u] + ((WeightedEdge)e).cost) {
          cost[e.v] = cost[u] + ((WeightedEdge)e).cost;
          parent[e.v] = u; 
        }
      }
    } 
    return new ShortestPathTree(sourceVertex, parent, T, cost);
  }

  public class ShortestPathTree extends Tree {
    private double[] cost; 

    public ShortestPathTree(int source, int[] parent, 
        List<Integer> searchOrder, double[] cost) {
      super(source, parent, searchOrder);
      this.cost = cost;
    }

    public double getCost(int v) {
      return cost[v];
    }

  }
}