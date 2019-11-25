package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drone {
	
	public static double grabDistance = 0.00025;
	public static double radius = 0.0003;
	protected static double dronePower = 250.0;
	protected static int nrMoves = 0;
	protected static double droneCoins = 0;
	
	
	protected static Position pos = new Position(55.944425, -3.188396);
	
	
	protected static void positiveCollect(int k) {                                                     //if the station is positive connect
		droneCoins += App.coins[k];
		dronePower += App.powers[k];
		App.coins[k] = 0;
		App.powers[k] = 0;
		App.symbols[k] = "danger";
	}
	
	protected static void negativeCollect(int l) {                                                      //if the station is negative connect
		if(Math.abs(App.coins[l]) > droneCoins) {
			App.coins[l] += droneCoins;
			droneCoins = 0;
			if (Math.abs(App.powers[l]) > dronePower) {
				App.powers[l] += dronePower;
				dronePower = 0;
			} else {
				dronePower += App.powers[l];
				App.powers[l] = 0;
			}
		}
		else {
			droneCoins += App.coins[l];
			App.coins[l] = 0;
		   
		    if (Math.abs(App.powers[l]) > dronePower) {
			   App.powers[l] += dronePower;
			   dronePower = 0;
		     } 
		      else {
				dronePower += App.powers[l];
				App.powers[l] = 0;
		   }
	     }
	}
	
	public static boolean stationInRange(Position nextPos, int s) {
		if(Math.pow((nextPos.latitude - App.latitudes[s]),2) + Math.pow((nextPos.longitude - App.longitudes[s]),2) <= Math.pow(grabDistance,2)) {
			return true;
		}
		
		return false;
	}
	
	public static int getClosestStation(Position nextPos) {
		 List<Double> eDistances = new ArrayList<>();
		 Map<Double, Integer> indexEdistances = new HashMap<>();
		for (int i = 0; i < 50; i++) {
			if(Math.pow((nextPos.latitude - App.latitudes[i]),2) + Math.pow((nextPos.longitude - App.longitudes[i]),2) <= Math.pow(grabDistance,2)) {
				double dist = Math.pow((nextPos.latitude - App.latitudes[i]),2) + Math.pow((nextPos.longitude - App.longitudes[i]),2);
				eDistances.add(dist);
				indexEdistances.put(dist, i);
			}
		}
		
        if(eDistances.isEmpty()) return -1;
        
		Collections.sort(eDistances);
		return indexEdistances.get(eDistances.get(0));
		
	}
	
	public static void move(Position pos, Position nextPos) {
		pos = nextPos;
		nrMoves++;
  		dronePower -=1.25;
	}
	
	public static void moveRandom(Position pos, Position nextPos) {
		int random16 = App.random.nextInt(16);
		pos = pos.nextPosition(Direction.values()[random16]);
		nrMoves++;
		dronePower -=1.25;
	}

	public static void go(Position nextPos) {
		  int nrFeaturesinRange = 0;
		  if(nextPos.inPlayArea()) {
			  for(int i =0;i<50;i++) {
				  if(stationInRange(nextPos, i)) {                                         //if the station is in Range of the next virtual position
					  //double p = Math.pow((nextPos.latitude - App.latitudes[i]),2) + Math.pow((nextPos.longitude - App.longitudes[i]),2);
					  //eDistances.add(p);
					  //indexEdistances.put(p, i);
					  nrFeaturesinRange++;
					  if(App.coins[i] > 0 && nrFeaturesinRange == 1) {                                            //if there is just one station in range and is positive then grab
						  move(pos,nextPos);
						  Drone.positiveCollect(i);   
						 
					  }                       
					  else if(nrFeaturesinRange >= 2) {                                                         //if there is more then one station in the range get the closest 
						  	int closest = getClosestStation(nextPos);
						  	if(App.coins[closest] > 0) {                                                         //if the closest station is positive
						  		move(pos,nextPos);
						  		Drone.positiveCollect(closest);
						  	}
						  	else {
						  		move(pos,nextPos);
						  		Drone.negativeCollect(closest);                              //IMPROVEMENT: if negative go random
						  	}
					  }
					  else if(nrFeaturesinRange == 1 && App.coins[i]<0){                    //if there is just one station and is negative then go random somewhere else 
						    moveRandom(pos,nextPos);
						    go(pos);
						    
					  }
				  }
			  }
		}
	}
	
	public static void main(String[] args) {
		//System.out.println(allNextPositions(pos).get(0).latitude);
	}
	

}
