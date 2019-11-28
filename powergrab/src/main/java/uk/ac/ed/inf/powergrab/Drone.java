package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

public class Drone {
	
	public static double grabDistance = 0.00025;
	public static double radius = 0.0003;
	protected static double dronePower = 250.0;
	protected static int nrMoves = 0;
	protected static double droneCoins = 0;

	
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
		App.pos = App.pos.nextPosition(d);
		nrMoves++;
  		dronePower -=1.25;
	}
		
	public static void moveRandom(Position simPos) {
		int random16 = App.random.nextInt(16);
		simPos = App.pos.nextPosition(Direction.values()[random16]);
		if(simPos.inPlayArea()) {
			App.pos = simPos;
			nrMoves++;
			dronePower -= 1.25;
		}
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
	
   public static Direction getNextDirection(int closestStation) {
	   List<Double> eDistances1 = new ArrayList<>();
	   
	   Map<Double, Direction> directionEdistances = new HashMap<>();
	   for(Direction d : Position.allDirections) {
		   Position pos1 = App.pos.nextPosition(d);
		   if(pos1.inPlayArea()) {
			   double dist1 = Math.pow((pos1.latitude - App.latitudes[closestStation]),2) + Math.pow((pos1.longitude - App.longitudes[closestStation]),2);
			   eDistances1.add(dist1);
			   directionEdistances.put(dist1, d);
		   }
	   }
	   Collections.sort(eDistances1);
	
	   return directionEdistances.get(eDistances1.get(0));
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
   
   public static ArrayList<Direction> getNextDirections(List<Integer> res){
	 ArrayList<Direction> nextDirections = new ArrayList<>();
	 for(int i =0;i<res.size();i++) {
		 Direction dir = getNextDirection(i);
		 nextDirections.add(dir);
	 }
	  return nextDirections;
<<<<<<< HEAD
	
   }
	
=======
	
   }
	
>>>>>>> dbbc1c9a0ab3e0b68af31e828f2e0d5ede66c3bf
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


				





			/*	  if(App.coins[j] > 0 && nrFeatures == 1) {                                             //if there is just one station in range and is positive then grab
					  move(pos,nextPos);
					  Drone.positiveCollect(j);   
					 
				  }                       
				  else if(nrFeatures >= 2) {                                                         //if there is more then one station in the range get the closest 
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
				  else if(nrFeatures == 1 && App.coins[j]<0){                     //if there is just one station and is negative then go random somewhere else 
					    moveRandom(pos);
					    go(pos);
					    
				  }
			}
		}*/
		
	    
				  
		/*  if(nextPos.inPlayArea()) {
			  for(int i =0;i<50;i++) {
				                                                             //if the station is in Range of the next virtual position
					 
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
						    moveRandom(pos);
						    go(pos);
						    
					  }
				  
			  }
		}*/
