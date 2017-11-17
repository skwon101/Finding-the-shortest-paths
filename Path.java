import java.util.LinkedList;

public class Path {
	public LinkedList<String> cities;
	public int totalCost;
	public int totalTime;
	
	public Path(LinkedList<String> cities, int c, int t){
		this.cities=cities;
		totalCost=c;
		totalTime=t;
	}
}
