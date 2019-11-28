package uk.ac.ed.inf.powergrab;

import java.awt.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

public class Stateful extends Drone{


	
/*	public static void goStatefulgo(Position nextPos, Direction dir) {
		//int closestP = closestPositiveStation(nextPos);                          //look at the closest positive station
		//Direction dir = getNextDirection(closestP);                              // get the direction of the next positive station 
		//nextPos = moveStateful(dir);                                             //always move towards the closest positive station
		//addToLine(App.pos);   //inainte sa te muti vezi daca e ok sa te muti acolo
		int closestStation = getClosestStation(nextPos);                         //tine cont de cea mai apropiata statie din rangeul tau
		int nrFeatures = nrFeaturesinRange(nextPos);  							 //verifica daca sunt in range si cate sunt
		
		if(nrFeatures == 0) {
			nextPos = moveStateful(dir);    									  //always move towards the closest positive station
			addToLine(App.pos);
		}
		else if(nrFeatures == 1) {
			if(App.coins[closestStation] > 0) {
				App.pos = moveStateful(dir);    									  //always move towards the closest positive station
				addToLine(App.pos);
				positiveCollect(closestStation);
			}
			else return;         //if the station is negative
			                     //go to the next closest station                                     				                                                     
		}
		else {
			if(App.coins[closestStation] > 0) {
				App.pos = moveStateful(dir);    									  //always move towards the closest positive station
				addToLine(App.pos);
				positiveCollect(closestStation);
			}
			else return;
		}
	}*/

	/*public static void ifnot(Position pos) {
		ArrayList<Integer> closestStations = getClosestStations(pos);
		 ArrayList<Direction> orderedDirections = getNextDirections(closestStations);
		
		for(Direction d : orderedDirections) {
			  pos = App.pos.nextPosition(d);
			if(pos.inPlayArea()) {
			  goStatefulgo(pos,d);
			}
		}
	}*/
	
	//public static Direction dontGo(Position nextPos) {
		//int closestStation = getClosestStation(nextPos); 
		//int nrFeatures = nrFeaturesinRange(nextPos);  
	//}
	
	/*public static double getMinDistance(Position pos) {
		double min = Double.MAX_VALUE;
		for(int i = 0;i<50;i++) {
			if(App.coins[i]> 0) {
				double dist = Math.pow((pos.latitude - App.latitudes[i]),2) + Math.pow((pos.longitude - App.longitudes[i]),2);
				if(dist < min) {
					min = dist;
				}
			}
		}
	}*/
	
	public static boolean notInNegativeRange(Position pos) {
		int closestStation = getClosestStation(pos);  
		if(closestStation == -1) return true;             //tine cont de cea mai apropiata statie din rangeul tau
		else if(App.coins[closestStation] < 0 ) return false;
		
		return true;
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
 
	
	public static void startGameStateful() {
		
		App.path = App.initializeLineString2();
		
		Direction d = null;
		int target= targetStation(App.pos);
		
		while(dronePower >= 1.25 && nrMoves<250) {
			d = getNextDirection(target);
		   for(Direction dir : Position.allDirections) {
			   Position nextPos = App.pos.nextPosition(dir);
			   int closest = getClosestStation(nextPos);      //ia cea mai apropiata statie din range
			  
			   if(nextPos.inPlayArea()) {
			       if(closest == -1 || App.coins[closest]>0) {
			    	   d = getNextDirection(target);
			       }
			      
//closest == -1 inseamna ca nu e nicio statie in range
			   
			   if(target == -1)  {   //daca s au terminat verzile
				   moveRandom(nextPos);                   //plimba te random fara rosii
			   }
			  }
		   }
		   //fa o functie care sa te mute la target care ia ca parametru pozitia actuala
		  //System.out.println(d);
		  App.pos = moveStateful(d);
		  addToLine(App.pos);
		  //System.out.println("mutare");
		  if(stationInRange(App.pos, target)) {
			  positiveCollect(target);
			  //fa nou target
			  target = targetStation(App.pos);
		  }
		  else {
			  App.pos = moveStateful(d);
			  addToLine(App.pos);
		  }
			
		}
	}		
	  /* double dist = Math.pow((nextPos.latitude - App.latitudes[target]),2) + Math.pow((nextPos.longitude - App.longitudes[target]),2);
	   if(dist < min) {
		   min = dist;
		   d = dir;
	   }*/
	   //int closest = getClosestStation(nextPos);
}