
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Ayu Sabrina
 */
public class Journey {
    private final int X = Integer.MAX_VALUE; // no edge exists
    private final int[][] distance = {
        {0   ,X   ,X   ,X   ,X   ,500 ,1137,X   ,678 ,983 },
        {X   ,0   ,X   ,X   ,554 ,X   ,X   ,1507,2214,X   },
        {X   ,X   ,0   ,3045,X   ,X   ,X   ,2845,X   ,2584},
        {X   ,X   ,3045,0   ,X   ,X   ,268 ,X   ,X   ,X   },
        {X   ,554 ,X   ,X   ,0   ,577 ,X   ,X   ,1901,X   },
        {500 ,X   ,X   ,X   ,577 ,0   ,X   ,X   ,X   ,X   },
        {1137,X   ,X   ,268 ,X   ,X   ,0   ,X   ,X   ,458 },
        {X   ,1507,2845,X   ,X   ,X   ,X   ,0   ,942 ,X   },
        {678 ,2214,X   ,X   ,1901,X   ,X   ,942 ,0   ,778 },
        {983 ,X   ,2584,X   ,X   ,X   ,458 ,X   ,778 ,0   }       
    };
    private final String[] cities; // name of each cities
    private ArrayList<Integer> path = new ArrayList<>(); // store travel path
    private boolean[] visited = new boolean[distance.length]; // check if the city is visited
    private int totalDistance; 
    private int current; 

    public Journey() {    
        cities = new String[]{"Spurs", "Warriors", "Celtics", "Heat", "Lakers", "Suns", "Magic", "Nuggets", "Thunder", "Rockets"};
        totalDistance = 0;
        current = 0; // started at Spurs 
        findShortestPath();
    }
    
    public static void main(String[] args){
        Journey j = new Journey();
        System.out.println(j.getSchedule());
        System.out.println(j.getTotalDistance());
    }
        
    public void findShortestPath(){
        visited[current] = true; // mark the current node as visited
        path.add(current); // add current node to the path
        
        // iterate through the nodes
        for (int i=0;i<distance.length-1;i++){
            // find the next destination
            int next=findNearestNeighbour();
            
            totalDistance+=distance[current][next];
            
            visited[next]= true;
            path.add(next);
            current=next;
        }
    }
    
    // find the nearest unvisited neighbor
    private int findNearestNeighbour(){
        int nearest = -1;
        int minDistance = Integer.MAX_VALUE;
        
        // iterate through all nodes
        for (int i = 0; i < distance.length; i++) {
            
            // check if the node is unvisited and if the distance is less than the current minimum
            if (!visited[i] && distance[current][i] < minDistance) {
                nearest = i;
                minDistance = distance[current][i];
            }
        }
        return nearest;
    }
    
    // print the travel plan in a list
    public String getSchedule(){
        StringBuilder pathString = new StringBuilder();
        for (int i=0;i<path.size();i++)
            pathString.append((i+1)).append(". ").append(cities[path.get(i)]).append("\n");  
        return pathString.toString();
    }
    
    // total distance travelled in KM
    public String getTotalDistance(){
        StringBuilder distanceString = new StringBuilder();
        distanceString.append("\nTotal distance traveled: ").append(totalDistance).append(" KM");
        return distanceString.toString();
    }
    
    // justifications of the travel plan
    public String getReasoning(){
        return """
               This travel plan is the best because:
               1. minimized physical and mental fatigue due to traveling
               2. reduced travel stress from continuous long-distance travel
               3. less travel times, more preparation time""";
    } 
}

