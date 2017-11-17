

public class WeightedEdge extends AbstractGraph.Edge{
  public int cost;
  public int time;// The weight on edge (u, v)

  /** Create a weighted edge on (u, v) */
  public WeightedEdge(int u, int v, int cost, int time) {
    super(u, v);
    this.cost = cost;
    this.time = time;
  }

  /** Compare two edges on weights */
  public int compareToCost(WeightedEdge edge) {
    if (cost > edge.cost) {
      return 1;
    }
    else if (cost == edge.cost) {
      return 0;
    }
    else {
      return -1;
    }
  }
  
  public int compareToTime(WeightedEdge edge) {
	if (time > edge.time) {
	   return 1;
	}
	else if (time == edge.time) {
	   return 0;
	}	
	else {
	   return -1;
	}
  }
}

