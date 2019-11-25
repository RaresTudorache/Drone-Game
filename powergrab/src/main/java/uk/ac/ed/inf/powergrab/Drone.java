package uk.ac.ed.inf.powergrab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drone {
	
	public static double grabDistance = 0.00025;
	public static double r = 0.0003;
	protected static double dronePower = 250.0;
	protected static int nrMoves = 0;
	protected static double droneCoins = 0;
	
	protected static Position pos = new Position(55.944425, -3.188396);
	
	
	/*public static List<Position> allNextPositions(Position pos) {                                  //calculate all possible positions for a given start position
		
		List<Position> futurePositions = new ArrayList<>();
		List<Direction> allDirections = Arrays.asList(Direction.values());                      // take all the possible Directions
		
		for(Direction d : allDirections) {
			Position nextPos = pos.nextPosition(d);
			futurePositions.add(nextPos);
		}
		
		return futurePositions;
	}*/
	
	public static void FeatureInRange(Position pos) {                          //check if feature is in Range of the next position
		
	    for(int i=0;i<50;i++) {
	    	if(Math.pow((pos.latitude - App.latitudes[i]),2) + Math.pow((pos.longitude - App.longitudes[i]),2) <= Math.pow(grabDistance,2)){
	    		if (App.coins[i] > 0) {
					droneCoins += App.coins[i];
					dronePower += App.powers[i];
					App.coins[i] = 0;
					App.powers[i] = 0;
					App.symbols[i] = "danger";
					
				} else if (Math.abs(App.coins[i]) > droneCoins) {
					App.coins[i] += droneCoins;
					droneCoins = 0;
					if (Math.abs(App.powers[i]) > dronePower) {
						App.powers[i] += dronePower;
						dronePower = 0;
						break;
					} else {
						dronePower += App.powers[i];
						App.powers[i] = 0;
					}

				} else if (Math.abs(App.coins[i]) <= droneCoins) {
					droneCoins += App.coins[i];
					App.coins[i] = 0;
					if (Math.abs(App.powers[i]) > dronePower) {
						App.powers[i] += dronePower;
						dronePower = 0;
						break;
					} else {
						dronePower += App.powers[i];
						App.powers[i] = 0;
					}
				}
	  
	    	}
	    }
	    } 

	
	
	public static void main(String[] args) {
		//System.out.println(allNextPositions(pos).get(0).latitude);
	}
	

}
