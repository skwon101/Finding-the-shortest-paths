

import java.util.*;

public abstract class AbstractGraph<V>  {
  protected List<V> vertices = new ArrayList<>(); // Store vertices
  protected List<List<Edge>> neighbors 
    = new ArrayList<>(); // Adjacency lists

  protected AbstractGraph() {
  }
  
  protected AbstractGraph(V[] vertices, int[][] edges) {
    for (int i = 0; i < vertices.length; i++)
      addVertex(vertices[i]);
    
    createAdjacencyLists(edges, vertices.length);
  }

  protected AbstractGraph(List<V> vertices, List<Edge> edges) {
    for (int i = 0; i < vertices.size(); i++)
      addVertex(vertices.get(i));
        
    createAdjacencyLists(edges, vertices.size());
  }

  protected AbstractGraph(List<Edge> edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      addVertex((V)(new Integer(i))); 
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  protected AbstractGraph(int[][] edges, int numberOfVertices) {
    for (int i = 0; i < numberOfVertices; i++) 
      addVertex((V)(new Integer(i))); 
    
    createAdjacencyLists(edges, numberOfVertices);
  }

  private void createAdjacencyLists(
      int[][] edges, int numberOfVertices) {
    for (int i = 0; i < edges.length; i++) {
      addEdge(edges[i][0], edges[i][1]);
    }
  }

  private void createAdjacencyLists(
      List<Edge> edges, int numberOfVertices) {
    for (Edge edge: edges) {
      addEdge(edge.u, edge.v);
    }
  }

  public List<List<Edge>> getNeiborsList(){
	  return neighbors;
  }
  
  public int getSize() {
    return vertices.size();
  }

  public List<V> getVertices() {
    return vertices;
  }

  public V getVertex(int index) {
    return vertices.get(index);
  }

  public int getIndex(V v) {
    return vertices.indexOf(v);
  }

  public List<Integer> getNeighbors(int index) {
    List<Integer> result = new ArrayList<>();
    for (Edge e: neighbors.get(index))
      result.add(e.v);
    
    return result;
  }

  public int getDegree(int v) {
    return neighbors.get(v).size();
  }

  public void printEdges() {
    for (int u = 0; u < neighbors.size(); u++) {
      System.out.print(getVertex(u) + " (" + u + "): ");
      for (Edge e: neighbors.get(u)) {
        System.out.print("(" + getVertex(e.u) + ", " +
          getVertex(e.v) + ") ");
      }
      System.out.println();
    }
  }

  public boolean addVertex(V vertex) {
    if (!vertices.contains(vertex)) {
      vertices.add(vertex);
      neighbors.add(new ArrayList<Edge>());
      return true;
    }
    else {
      return false;
    }
  }
 
  protected boolean addEdge(Edge e) {
    if (e.u < 0 || e.u > getSize() - 1)
      throw new IllegalArgumentException("No such index: " + e.u);

    if (e.v < 0 || e.v > getSize() - 1)
      throw new IllegalArgumentException("No such index: " + e.v);
    
    if (!neighbors.get(e.u).contains(e)) {
      neighbors.get(e.u).add(e);
      return true;
    }
    else {
      return false;
    }
  }
  
  public boolean addEdge(int u, int v) {
    return addEdge(new Edge(u, v));
  }
  
  public static class Edge {
    public int u; 
    public int v; 
    
    public Edge(int u, int v) {
      this.u = u;
      this.v = v;
    }
    
    public boolean equals(Object o) {
      return u == ((Edge)o).u && v == ((Edge)o).v; 
    }
  }

  public class Tree {
    private int root; // The root of the tree
    private int[] parent; // Store the parent of each vertex
    private List<Integer> searchOrder; // Store the search order

    /** Construct a tree with root, parent, and searchOrder */
    public Tree(int root, int[] parent, List<Integer> searchOrder) {
      this.root = root;
      this.parent = parent;
      this.searchOrder = searchOrder;
    }

    public List<V> getPath(int index) {
      ArrayList<V> path = new ArrayList<>();

      do {
        path.add(vertices.get(index));
        index = parent[index];
      }
      while (index != -1);

      return path;
    }

    public void printPath(int index) {
      List<V> path = getPath(index);
      System.out.print("A path from " + vertices.get(root) + " to " +
        vertices.get(index) + ": ");
      for (int i = path.size() - 1; i >= 0; i--)
        System.out.print(path.get(i) + " ");
    }

  }
}
