package uk.ac.ed.inf.powergrab;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class Stateful extends Drone{

	
	public static boolean notInNegativeRange(Position pos) {
		int closestStation = getClosestStation(pos);  
		if(closestStation == -1) return true;             //tine cont de cea mai apropiata statie din rangeul tau
		else if(App.coins[closestStation] < 0 ) return false;
		
		return true;
	}
	
	 public static ArrayList<Direction> getNextDirections(ArrayList<Integer> res){
		 ArrayList<Direction> nextDirections = new ArrayList<>();
		 for(int i =0;i<res.size();i++) {
			 Direction dir = getNextDirection(i);
			 nextDirections.add(dir);
		 }
		  return nextDirections;
		
	   }
	
	public static int targetStation(Position pos) {
		double min =Double.MAX_VALUE;
		int k = -1;
		for(int i =0;i<50;i++) {
			if(App.coins[i] > 0) {
				double dist = Math.pow((pos.latitude - App.latitudes[i]),2) + Math.pow((pos.longitude - App.longitudes[i]),2);
				if(dist < min) {
					min = dist;
					k = i;
				}
			}
		}
		return k;
	}
	
	 public static Direction getNextDirection(int closestStation) {
		   ArrayList<Double> eDistances1 = new ArrayList<>();
		   
		   Map<Double, Direction> directionEdistances = new HashMap<>();
		   for(Direction d : Position.allDirections) {
			   Position pos1 = App.pos.nextPosition(d);
			   if(pos1.inPlayArea()) {
				   if(closestStation == -1 || App.coins[closestStation]>0) {
					   double dist1 = Math.pow((pos1.latitude - App.latitudes[closestStation]),2) + Math.pow((pos1.longitude - App.longitudes[closestStation]),2);
					   eDistances1.add(dist1);
					   directionEdistances.put(dist1, d);
				   }
				   
			   }
		   }
		   Collections.sort(eDistances1);
		
		   return directionEdistances.get(eDistances1.get(0));
	   }
	 
	 public static boolean noNegatives(Position pos) {
		 int closestStation = getClosestStation(pos);
		 if(closestStation == -1 || App.coins[closestStation] > 0)
			 return true;
		 
		 return false;
	 }
	 
	 public static Direction getNextDirection2(int station) {
		 
		 double min =Double.MAX_VALUE;
		 Direction dir =null;
		 //if(station == -1) return dir;
		 for(Direction d : Position.allDirections) {
			 Position pos = App.pos.nextPosition(d);
			 if(pos.inPlayArea() && noNegatives(pos)) {
				
					 double dist = Math.pow((pos.latitude - App.latitudes[station]),2) + Math.pow((pos.longitude - App.longitudes[station]),2);
					 if(dist < min) {
							min = dist;
							dir = d;
					}
				 
			 }
			 
		 }
		 return dir;
	 }
	 
	 public static void moveRandomStateful() {
		 
	 }
	   
 
	//getclosestStation - ia cea mai apropiata statie din rangeul tau
	//getNextDirection - ia directia celei mai apropiate statii
	public static void startGameStateful() {
		
		App.path = App.initializeLineString2();
		
		Direction d = null;
		//Position p = new Position(55.9447, -3.1887);
		//int target = targetStation(App.pos);
		int target = closestPositiveStation(App.pos);
		//System.out.println(noNegatives(p));
		System.out.println(target);
		System.out.println(App.coins[target]);
		System.out.println(App.latitudes[target]);
		System.out.println(App.longitudes[target]);
		
		
		while(dronePower >= 1.25 && nrMoves<250) {
			if(target == -1) {
				moveRandom(App.pos); //daca ai vizitat toate statiile pozitive muta te random
			}
			else {
			d = getNextDirection2(target);    //daca viitoarea pozitie nu are nimic in range sau are ceva
			
			//System.out.println(d);
			
			App.pos = moveStateful(d);
			addToLine(App.pos);
		
			int closest = getClosestStation(App.pos);
	
			   if(target == closest) {
				positiveCollect(target);
				target = closestPositiveStation(App.pos);
			}
			}
	       
		}
	}	
}