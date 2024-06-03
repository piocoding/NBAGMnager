
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
    private final int X = Integer.MAX_VALUE;
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
    private final String[] cities;
    private ArrayList<Integer> pathIndex = new ArrayList<>();
    private ArrayList<String> pathName = new ArrayList<>();
    private boolean[] visited = new boolean[distance.length];
    private int totalDistance;
    private int current;   

    public Journey() {    
        cities = new String[]{"Spurs", "Warriors", "Celtics", "Heat", "Lakers", "Suns", "Magic", "Nuggets", "Thunder", "Rockets"};
        totalDistance = 0;
        current = 0;   
    }
        
    public void findShortestPath(){
        visited[current] = true;
        pathIndex.add(current);
        
        for (int i=0;i<distance.length-1;i++){
            int next=findNearestNeighbour();
            totalDistance+=distance[current][next];
            visited[next]= true;
            pathIndex.add(next);
            current=next;
        }
    }
  
    private int findNearestNeighbour(){
        int nearest = -1;
        int minDistance = Integer.MAX_VALUE;
        
        for (int i = 0; i < distance.length; i++) {
            if (!visited[i] && distance[current][i] < minDistance) {
                nearest = i;
                minDistance = distance[current][i];
            }
        }
        
        return nearest;
    }
    
    public String printPath(){
        for(int i=0;i<pathIndex.size();i++)
            pathName.add(cities[pathIndex.get(i)]);
        
        return pathName.toString();
    }
    
    public int printTotalDistance(){
        return totalDistance;
    }
    
    public String printReasoning(){
        return "This travel plan is the best because\n"
                + "1. minimized physical and mental fatigue due to traveling\n" 
                + "2. reduced travel stress from continuous long-distance travel\n"
                + "3. less travel times, more preparation time";
    } 
}

