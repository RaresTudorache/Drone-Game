package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;

import com.mapbox.geojson.Point;

public class Stateless extends Drone{
	
	

	protected static void move(Direction d) {                                                                //method for updating the current position, dronePower and number of moves for the stateless drone          
		dronePower -= 1.25;                                                                               //here, I also chose to update the output String for the TXT file
		 if(nrMoves == 0)
	  	   outputTXT =  App.pos.latitude + "," + App.pos.longitude + "," + d + ","
					+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
					+ "," + dronePower + '\n';
		else outputTXT += App.pos.latitude + "," + App.pos.longitude + "," + d + ","
				+ App.pos.nextPosition(d).latitude + "," + App.pos.nextPosition(d).longitude + "," + droneCoins
				+ "," + dronePower+ '\n';

		App.pos = App.pos.nextPosition(d);
		nrMoves++;
	}

	protected static void goStatelessGo(Position nextPos, Direction d) {                              //this method is responsible for how the statelss drone behaves
		int nrFeatures = nrFeaturesinRange(nextPos);                                                  //take the number of features in the range of nextPos
		int closest = getClosestStation(nextPos); 													  //take the closest station from nextPos
			if(nrFeatures == 1) {
				if(App.coins[closest] >0 ) {                                                         //if there is only one station in range and it is positive 
					move(d);                                                                         //then go there, connect and add the move to the path
					App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
					positiveCollect(closest);	
				}
				else {															 	       //if the only station in range is negative then go to a random valid position and 
					moveRandom(App.pos);                                                   //add the move to the path
					App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
                 }
			}
			else if(nrFeatures ==0 ){                                                                //if there is no station in range then go to a random valid position and 
				moveRandom(App.pos);           														 //add the move to the path
				App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
			 }
			else {                  			                                                      //if there is more than on e station in range
			
			  	if(App.coins[closest] > 0) {                                                          //if the closest station is positive
			  		move(d);																	      //then go there, connect and add the move to the path
			  		App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
			  		positiveCollect(closest);
			  		
			  	}
			  	else {                                                                                //if the closest station is negative
			  		moveRandom(App.pos);                                                              //then go to a random valid position and add the move to the path
			  		App.path.add(Point.fromLngLat(App.pos.longitude, App.pos.latitude));
	
			  	}
			}	
	}

    protected static void startGameStateless() {                                
	  while(dronePower >= 1.25 && nrMoves<250) {                                                //loop until drone runs our of moves or power
		  for (Direction d : Position.allDirections) {
			  Position nextPos = App.pos.nextPosition(d);                                       //try for direction d
			  if(nextPos.inPlayArea()) {
				  goStatelessGo(nextPos,d);
				  break;
		      }
		 }
	  }
	}
   

}
	