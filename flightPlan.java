
import java.io.*;

import java.util.*;

public class flightPlan {
	
	static int FlightNum=1;
	
	public static void main(String[] arg) throws IOException{
		BufferedReader flightData, requestedFlightPlans;
		BufferedWriter bw = null;
		FileWriter fw = null;
			try{
			fw=new FileWriter(arg[2]);
			bw=new BufferedWriter(fw);
			bw.write("");
			bw.close();
			flightData=new BufferedReader(new FileReader(arg[0]));
			requestedFlightPlans=
					new BufferedReader(new FileReader(arg[1]));
			ArrayList<String> vertices=new ArrayList<>();
			LinkedList<WeightedEdge> edges=new LinkedList<>();
			String str = null;
			String depart = null;
			String arrival = null;	
			int cost=0;
			int time=0;
			int num=Integer.parseInt(flightData.readLine());
			for(int i=0;i<num;i++){
				str=flightData.readLine();
				StringTokenizer st=new StringTokenizer(str, "|");
				depart=st.nextToken();
				arrival=st.nextToken();
				cost=Integer.parseInt(st.nextToken());
				time=Integer.parseInt(st.nextToken());
				if(!vertices.contains(depart))
					vertices.add(depart);
				if(!vertices.contains(arrival))
					vertices.add(arrival);
				WeightedEdge edge=new WeightedEdge
						(vertices.indexOf(depart), vertices.indexOf(arrival), 
								cost, time);
				edges.add(edge);
				WeightedEdge edge2=new WeightedEdge
						(vertices.indexOf(arrival), vertices.indexOf(depart), 
								cost, time);
				edges.add(edge2);
			}
			WeightedGraph<String> graph=
					new WeightedGraph<>(vertices,edges);
			
			num=Integer.parseInt(requestedFlightPlans.readLine());
			for(int i=0;i<num;i++){
				str=requestedFlightPlans.readLine();
				StringTokenizer st=new StringTokenizer(str, "|");
				depart=st.nextToken();
				arrival=st.nextToken();
				String CostOrTime;
				if(st.nextToken().equals("T"))
					CostOrTime="Time";
				else
					CostOrTime="Cost";
				shortPath(depart, arrival, CostOrTime, graph, arg[2]);
			}
		}
		catch(Exception e){
			System.out.println("Exeption has occured.");
		}
	}
	
	public static void shortPath(String d,String a,String CorT,
			WeightedGraph<String> graph, String outputFile)throws IOException{
		List<String> vertices=graph.vertices;
		int departIndex=vertices.indexOf(d);
		int arrivalIndex=vertices.indexOf(a);
		List<List<AbstractGraph.Edge>> AdjacencyList=graph.neighbors;
		Stack<AbstractGraph.Edge> stack=new Stack<>();
		LinkedList<Integer> visitedCities=new LinkedList<>();
		LinkedList<Path> Paths=new LinkedList<>();

		if(AdjacencyList.get(departIndex).isEmpty())
			System.out.println("Error occurs because no flight plan can be created.");
		else{
			visitedCities.add(departIndex);
			for(int i=0;i<AdjacencyList.get(departIndex).size();i++){
				stack.push(AdjacencyList.get(departIndex).get(i));
			}
			while(!stack.isEmpty()){
				AbstractGraph.Edge edge=stack.pop();
				int parent=edge.u;
				int current=edge.v;
				while(parent!=visitedCities.getLast()){
					visitedCities.removeLast();
				}
				
				visitedCities.add(current);
				if(current==arrivalIndex){
					int totalCost=0;
					int totalTime=0;
					LinkedList<String> cities=new LinkedList<>();
					for(int i=0;i<visitedCities.size();i++){
						String city=vertices.get(visitedCities.get(i));
						cities.add(city);
						if(i<visitedCities.size()-1){
							try {
								totalCost+=graph.getCost(visitedCities.get(i), 
										visitedCities.get(i+1));
								totalTime+=graph.getTime(visitedCities.get(i), 
										visitedCities.get(i+1));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
					Path path=new Path(cities,totalCost,totalTime);
					if(CorT.equals("Cost")){
						if(Paths.isEmpty())
							Paths.add(path);
						else if(Paths.size()==1){
							if(path.totalCost<Paths.getFirst().totalCost)
								Paths.addFirst(path);
							else
								Paths.add(path);
						}
						else if(Paths.size()==2){
							boolean inserted=false;
							for(int i=0;i<Paths.size();i++){
								if(path.totalCost<Paths.get(i).totalCost){
									inserted=true;
									Paths.add(i, path);
									break;
								}
							}			
							if(!inserted)
								Paths.add(path);
						}				
						else if(Paths.size()>=3){
							for(int i=0;i<Paths.size();i++){
								if(path.totalCost<Paths.get(i).totalCost){
									Paths.add(i, path);
									Paths.removeLast();
									break;
								}
							}		
						}
					}
					
					else{
						if(Paths.isEmpty())
							Paths.add(path);
						else if(Paths.size()==1){
							if(path.totalTime<Paths.getFirst().totalTime)
								Paths.addFirst(path);
							else
								Paths.add(path);
						}
						else if(Paths.size()==2){
							boolean inserted=false;
							for(int i=0;i<Paths.size();i++){
								if(path.totalTime<Paths.get(i).totalTime){
									inserted=true;
									Paths.add(i, path);
									break;
								}
							}			
							if(!inserted)
								Paths.add(path);
						}				
						else if(Paths.size()>=3){
							for(int i=0;i<Paths.size();i++){
								if(path.totalTime<Paths.get(i).totalTime){
									Paths.add(i, path);
									Paths.removeLast();
									break;
								}
							}		
						}
					}
				}
				
				else{
					if(!AdjacencyList.get(current).isEmpty()){
						for(int i=0;i<AdjacencyList.get(current).size();i++){
							int child=AdjacencyList.get(current).get(i).v;
							if(!visitedCities.contains(child))
								stack.push(AdjacencyList.get(current).get(i));
						}
					}
				}
			}
		}
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		fw=new FileWriter(outputFile,true);
		bw=new BufferedWriter(fw);
		bw.write("Flight "+FlightNum+": "+d+","
				+ " "+a+" ("+CorT+")");
		bw.newLine();
		FlightNum++;
		int count=0;
		for(int i=0;i<Paths.size();i++){
			LinkedList<String> cities=Paths.get(i).cities;
			int cost=Paths.get(i).totalCost;
			int time=Paths.get(i).totalTime;
			count=i+1;
			bw.write("Path "+count+": ");
			for(int j=0;j<cities.size();j++){
				if(j<cities.size()-1)
					bw.write(cities.get(j)+" -> ");
				else
					bw.write(cities.get(j)+". ");
			}
			bw.write("Time: "+time+" Cost: "+cost);
			bw.newLine();
		}
		bw.newLine();		
		bw.close();
	}
}
