package uk.ac.ed.inf.powergrab;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stateless extends Drone{
	
	
	public static void startGame() {
		
	  int nrFeaturesinRange = 0;
	  List<Direction> allDirections = Arrays.asList(Direction.values());
	  while(dronePower > 0 || nrMoves<=250) {
		  for (Direction d : allDirections) {
			  Position nextPos = pos.nextPosition(d);
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
	  }
	  
	}
	
	public static void main(String[] args) {
		//System.out.println(allNextPositions(pos));
	}
	

}
