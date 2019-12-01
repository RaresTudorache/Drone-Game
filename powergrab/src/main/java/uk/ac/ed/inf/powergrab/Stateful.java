package uk.ac.ed.inf.powergrab;



import com.mapbox.geojson.Point;

public class Stateful extends Drone{
	

	protected static Position moveStateful(Direction d) {                                                   //very similar with the move(Direction d) method used for the stateless drone
		dronePower -=1.25;
		if(nrMoves == 0)
		  	   outputTXT =  App.pos.latitude + "," + App.pos.longitude + "," + d + ","
						+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
						+ "," + dronePower + '\n';
			else outputTXT += App.pos.latitude + "," + App.pos.longitude + "," + d + ","
					+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
					+ "," + dronePower+ '\n';

		App.pos = App.pos.nextPosition(d);
		nrMoves++;
  	
  		return App.pos;
	}
	/*
	public static int closestPositiveStation(Position pos1) {
		ArrayList<Double> eDistances = new ArrayList<>();
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
	*/
	/*
	 public static ArrayList<Direction> getNextDirections(ArrayList<Integer> res){
		 ArrayList<Direction> nextDirections = new ArrayList<>();
		 for(int i =0;i<res.size();i++) {
			 Direction dir = getNextDirection(i);
			 nextDirections.add(dir);
		 }
		  return nextDirections;
		
	   }*/
	
	protected static int targetStation(Position pos) {                                   //this method returns the closest positive station given a position
		double min =Double.MAX_VALUE;                                                    //I use this method to get the index of the "target" station 
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


	 protected static Direction getNextDirection(int station) {                       // this method returns the direction of the target station that is in play area 
		                                                                              // the target needs to be in play are and it shouldn't go next to a negative station
		 double min =Double.MAX_VALUE;
		 Direction dir =null;
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
/*
	 public static boolean equalss(Position pos, Position pos1) {
		 if(pos.latitude == pos1.latitude && pos.longitude == pos1.longitude)
			 return true;
		 
		 return false;
	 }
	 
	 public static boolean containsKeyy(Position pos, HashMap<Position, Integer> help) {
		
		 for( Position pos1 : help.keySet()) {
			 if(equalss(pos1,pos)) return true;	
     	}
		 
		 return false;
	 }
 */
	//fixat bug
	//file txt pus la punct
	//file geojson
	//comentat cod
	//raport
	protected static void startGameStateful() {                                             //this method is responsible for how the stateful drone behaves
		
		Direction d = null;
		int target = targetStation(App.pos);                                              //take the station that the drone wants to reach

		while(dronePower >= 1.25 && nrMoves<250) {                                        //loop until you run out of power or moves
			if(target == -1) {                                                            //target will be -1 when there are no more positive stations to go
				int randomInt = getRandomWithExclusion(App.random, 0, 15, badStations(App.pos));
			    d = getNextDirection(randomInt);                                           //when the drone runs out of positive stations go to a random valid position
			    App.pos = moveStateful(d);                                                 //move as required and add to the path
			    App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
			    
			}
			else {
			d = getNextDirection(target);                                                  //get the direction of the target
			
			App.pos = moveStateful(d);                                                      //move as required and add to the path
			App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));	

			int closest = getClosestStation(App.pos);                                      //take the closest station from the current position
	
			  if(target == closest) {                                                     //if the drone reached the target, connect to it
				positiveCollect(target);
				target = targetStation(App.pos);                                           //calculate the new target and loop again
			  }
		   }   
		}
	}	
}
