package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

public class Drone {
	
	public static double grabDistance = 0.00025;
	public static double radius = 0.0003;
	protected static double dronePower = 250.0;
	protected static int nrMoves = 0;
	protected static double droneCoins = 0;
	protected static String outputTXT;

	
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
	
	public static void move(Direction d) {
		dronePower -= 1.25;
		 if(nrMoves == 0)
	  	   outputTXT =  App.pos.latitude + "," + App.pos.longitude + "," + d + ","
					+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
					+ "," + dronePower + '\n';
		else outputTXT += App.pos.latitude + "," + App.pos.longitude + "," + d + ","
				+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
				+ "," + dronePower+ '\n';

		App.pos = App.pos.nextPosition(d);
		nrMoves++;
  		//dronePower -=1.25;
	}

	
	public static boolean moveTest(Position simPos) {
		int random16 = App.random.nextInt(16);
		simPos = App.pos.nextPosition(Direction.values()[random16]);
	/*	if(nrMoves == 0)
		  	   outputTXT =  App.pos.latitude + "," + App.pos.longitude + "," + Direction.values()[random16] + ","
						+ App.pos.nextPosition(Direction.values()[random16]).latitude + "," + App.pos.nextPosition(Direction.values()[random16]).longitude + "," + droneCoins
						+ "," + dronePower+ '\n';
		else outputTXT += App.pos.latitude + "," + App.pos.longitude + "," + Direction.values()[random16] + ","
					+ App.pos.nextPosition(Direction.values()[random16]).latitude + "," + App.pos.nextPosition(Direction.values()[random16]).longitude + "," + droneCoins
					+ "," + dronePower+ '\n';
					*/
		if(simPos.inPlayArea()) 
			return true;
			
		else 
			moveTest(simPos);
		
		return false;
	}
	public static int getRandomWithExclusion(Random rnd, int start, int end, ArrayList<Integer> wrongStations) {
	    int random = start + rnd.nextInt(end - start + 1 - wrongStations.size());
	    for (Integer ex : wrongStations) {
	        if (random < ex) {
	            break;
	        }
	        random++;
	    }
	    return random;
	}
	
	public static ArrayList<Integer> takeNext(Position simPos) {
		//ArrayList<Position> wrongDirections = new ArrayList<>();
		ArrayList<Integer> wrongStations = new ArrayList<>();
		for(Direction d : Position.allDirections) {
			simPos = App.pos.nextPosition(d);
			if(!simPos.inPlayArea()) {
				int k = Arrays.asList(Direction.values()).indexOf(d);
				wrongStations.add(k);
			}
		}
		return wrongStations;
	}	
	
	public static void moveRandom(Position simPos) {
		int randomInt = getRandomWithExclusion(App.random, 0, 15, takeNext(simPos));
		
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
			//dronePower -= 1.25;
	
	}


	public static int nrFeaturesinRange(Position nextPos) {
		int nr = 0;
		for(int j =0;j<50;j++) {
			  if(stationInRange(nextPos, j))                                          //if the station is in Range of the next virtual position
				  nr++;
			  }
		return nr;
	}
	
///////////////////////////for stateful
	
	public static int closestPositiveStation(Position pos1) {
		List<Double> eDistances = new ArrayList<>();
		Map<Double, Integer> indexEdistances = new HashMap<>();
		
		for(int i =0;i<50;i++) {
			if(App.coins[i] > 0) {                                              // eDistances will be empty when you go through all the stations
				double dist = Math.pow((pos1.latitude - App.latitudes[i]),2) + Math.pow((pos1.longitude - App.longitudes[i]),2);
				eDistances.add(dist);
				indexEdistances.put(dist, i);
			}
		}	
		if(eDistances.isEmpty()) return -1;
		Collections.sort(eDistances);
		return indexEdistances.get(eDistances.get(0));
	}
	
  
   public static ArrayList<Integer> getClosestStations(Position pos){
	    List<Double> eDistances = new ArrayList<>();
		Map<Double, Integer> indexEdistances = new HashMap<>();
		ArrayList<Integer> res = new ArrayList<>();
		
		for(int i =0;i<50;i++) {
			if(App.coins[i] > 0) {                                              // eDistances will be empty when you go through all the stations
				double dist = Math.pow((pos.latitude - App.latitudes[i]),2) + Math.pow((pos.longitude - App.longitudes[i]),2);
				eDistances.add(dist);
				indexEdistances.put(dist, i);
			}
		}	
		
		Collections.sort(eDistances);
		 for(int i=0;i<eDistances.size();i++) {
			   res.add(indexEdistances.get(eDistances.get(i)));
		   }
		 if(eDistances.isEmpty()) return res;
		return res;
   }
   

	public static Position moveStateful(Direction d) {
		App.pos = App.pos.nextPosition(d);
		nrMoves++;
  		dronePower -=1.25;
  		return App.pos;
	}
	
	
	@SuppressWarnings("static-access")
	public static void addToLine(Position new_point) {

		LineString temp = LineString.fromJson(App.path);
		ArrayList<Point> list = (ArrayList<Point>) temp.coordinates();
		Point new_p = Point.fromLngLat(new_point.longitude, new_point.latitude);
		list.add(new_p);
		LineString s_line = LineString.fromJson(App.path);
		App.path = s_line.fromLngLats(list).toJson();

	}
}

