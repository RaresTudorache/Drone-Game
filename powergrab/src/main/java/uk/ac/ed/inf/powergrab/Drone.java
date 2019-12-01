package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Drone {
	
	protected static double grabDistance = 0.00025;
	protected static double radius = 0.0003;
	protected static double dronePower = 250.0;
	protected static int nrMoves = 0;
	protected static double droneCoins = 0;
	protected static String outputTXT;                                                          //string used to create the TXT file

	
	public static void positiveCollect(int k) {                                              //steps to be taken when the drone needs to connect to a positive station
		droneCoins += App.coins[k];
		dronePower += App.powers[k];
		App.coins[k] = 0;
		App.powers[k] = 0;
	}
	
	public static void negativeCollect(int l) {                                               //steps to be taken when the drone has to connect to a negative station
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
	
	public static boolean stationInRange(Position nextPos, int s) {                                     //method for checking if a station is in the range of the current position
		if(Math.pow((nextPos.latitude - App.latitudes[s]),2) + Math.pow((nextPos.longitude - App.longitudes[s]),2) <= Math.pow(grabDistance,2)) {
			return true;
		}
		
		return false;
	}
	   
	public static int getClosestStation(Position nextPos) {                                                 // method that returns the index of the closest        
		 List<Double> eDistances = new ArrayList<>(); 														// station in the range of the current position
		 Map<Double, Integer> indexEdistances = new HashMap<>();
		 for (int i = 0; i < 50; i++) {
			if(stationInRange(nextPos, i)) {
				double dist = Math.pow((nextPos.latitude - App.latitudes[i]),2) + Math.pow((nextPos.longitude - App.longitudes[i]),2);
				eDistances.add(dist);
				indexEdistances.put(dist, i);
			}
		}
		
        if(eDistances.isEmpty()) return -1;
        
		Collections.sort(eDistances);
		return indexEdistances.get(eDistances.get(0));
		
	}

	
	 public static boolean noNegatives(Position pos) {                                    //this method return false if the closest station from the current position is negative
		 int closestStation = getClosestStation(pos);
		 if(closestStation == -1 || App.coins[closestStation] > 0)
			 return true;
		 
		 return false;
	 }
	 
	
	public static int getRandomWithExclusion(Random rnd, int start, int end, ArrayList<Integer> wrongStations) {         //method for taking a random number excluding the ones that are
	    int random = start + rnd.nextInt(end - start + 1 - wrongStations.size());                                        //added to the list
	    for (Integer ex : wrongStations) {
	        if (random < ex) {
	            break;
	        }
	        random++;
	    }
	    return random;
	}
	
	public static ArrayList<Integer> badStations(Position simPos) {                                         //this method returns an ArrayList with all next positions that are not valid
		ArrayList<Integer> wrongStations = new ArrayList<>();
		for(Direction d : Position.allDirections) {                                               
			simPos = App.pos.nextPosition(d);
			if(!simPos.inPlayArea() || !noNegatives(simPos)) {                                             //a position is not valid if it is out of play area or 
				int k = Arrays.asList(Direction.values()).indexOf(d);                                      // if it is in range of a negative station
				wrongStations.add(k);
			}
		}
		return wrongStations;
	}	
	
	public static void moveRandom(Position simPos) {                                                      //this method takes a random direction that is valid and 
		int randomInt = getRandomWithExclusion(App.random, 0, 15, badStations(simPos));                   //it updates current position, dronePower and number of moves 
		                                                                                                  //same as in the previous move method, I chose to form the output String for the TXT file
		simPos = App.pos.nextPosition(Direction.values()[randomInt]);
		dronePower -= 1.25;
		
		if(nrMoves == 0)
	  	  outputTXT =  App.pos.latitude + "," + App.pos.longitude + "," + Direction.values()[randomInt] + ","
					+ App.pos.nextPosition(Direction.values()[randomInt]).latitude + "," + App.pos.nextPosition(Direction.values()[randomInt]).longitude + "," + droneCoins
					+ "," + dronePower+ '\n';
	    else outputTXT += App.pos.latitude + "," + App.pos.longitude + "," + Direction.values()[randomInt] + ","
				+ App.pos.nextPosition(Direction.values()[randomInt]).latitude + "," + App.pos.nextPosition(Direction.values()[randomInt]).longitude + "," + droneCoins
				+ "," + dronePower+ '\n';
	
		App.pos = simPos;
		nrMoves++;
	}


	public static int nrFeaturesinRange(Position nextPos) {                                           //method for counting the number of stations in the range of a position
		int nr = 0;
		for(int j =0;j<50;j++) {
			  if(stationInRange(nextPos, j))                                                          //if the station is in Range of the next virtual position
				  nr++;
			  }
		return nr;
	}


}

