package uk.ac.ed.inf.powergrab;



import java.util.ArrayList;

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


	//fixat bug
	//raport
	 
	protected static boolean oppositeDirections(Direction d1, Direction d2) {
		
	    if(d1 == Direction.E && d2 == Direction.W) return true;
	    else if(d1 == Direction.ESE && d2 == Direction.WNW) return true;
	    else if(d1 == Direction.SE && d2 == Direction.NW) return true;
	    else if(d1 == Direction.SSE && d2 == Direction.NNW) return true;
	    else if(d1 == Direction.S && d2 == Direction.N) return true;
	    else if(d1 == Direction.SSW && d2 == Direction.NNE) return true;
	    else if(d1 == Direction.SW && d2 == Direction.NE) return true;
	    else if(d1 == Direction.WSW && d2 == Direction.ENE) return true;
	    
	    return false;
	} 
	 
	protected static void startGameStateful() {                                             //this method is responsible for how the stateful drone behaves
		ArrayList <Direction> repetedDirections = new ArrayList<>();
		Direction d = null;
		int target = targetStation(App.pos);                                              //take the station that the drone wants to reach
        int nr =0;
        int ok =0;
		while(dronePower >= 1.25 && nrMoves<250) {                                        //loop until you run out of power or moves
			if(target == -1) {                                                            //target will be -1 when there are no more positive stations to go
				int randomInt = getRandomWithExclusion(App.random, 0, 15, badStations());
			    d = getNextDirection(randomInt);                                           //when the drone runs out of positive stations go to a random valid position
			    App.pos = moveStateful(d);                                                 //move as required and add to the path
			    App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
			    
			}
			else {
			d = getNextDirection(target);                                                  //get the direction of the target

			//////////////////////
			repetedDirections.add(d);
			
			if(repetedDirections.size() >3 && ok == 0) {
				
			   for(int i=1;i<=repetedDirections.size()-1;i++) {
					if(oppositeDirections(repetedDirections.get(i),repetedDirections.get(i-1))) {
					    nr++;
					    repetedDirections.remove(d);
						
						int randomInt = getRandomWithExclusion(App.random, 0, 15, badStations());
					    d = getNextDirection(randomInt);                                           //when the drone runs out of positive stations go to a random valid position
					    
					}
					if(nr > 100) ok =1;
			    }
			
			}
			//System.out.println(nr);
			
			//////////////////////////////////////
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
