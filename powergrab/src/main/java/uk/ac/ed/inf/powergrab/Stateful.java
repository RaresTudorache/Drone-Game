package uk.ac.ed.inf.powergrab;


public class Stateful extends Drone{

	
	public static void goStatefulgo(Position nextPos) {
		int closestP = closestPositiveStation(nextPos);                          //look at the closest positive station
		Direction dir = getNextDirection(closestP);                              // get the direction of the next positive station 
		nextPos = moveStateful(dir);                                             //always move towards the closest positive station
		addToLine(App.pos);
		int closestStation = getClosestStation(nextPos);                         //tine cont de cea mai apropiata statie din rangeul tau
		int nrFeatures = nrFeaturesinRange(nextPos);  							 //verifica daca sunt in range si cate sunt
		
		if(nrFeatures == 0) {
			nextPos = moveStateful(dir);    									  //always move towards the closest positive station
			addToLine(App.pos);
		}
		else if(nrFeatures == 1) {
			if(closestP == closestStation) {
				positiveCollect(closestStation);
				App.pos = moveStateful(dir);    									  //always move towards the closest positive station
				addToLine(App.pos);
			}
			else {                                                           //if the station is negative
				                                                             //go to the next closest station
			}
		
		}
	}
	
	public static void startGameStateful() {
		App.path = App.initializeLineString2();
		while(dronePower >= 1.25 && nrMoves<250) {
			
			int closestP = closestPositiveStation(App.pos);
			 for (Direction d : Position.allDirections) {
				  Position nextPos = App.pos.nextPosition(d);
			//if(App.coins[closestN] < 0) {
			//	negativeCollect(closestN);
			//}
			
			//if(stationInRange(App.pos, closestP)) {
			positiveCollect(closestP);
			//}
			
			if(closestP == -1) return;      //daca s au terminat statiile pozitive -> misca te random evitand cele rosii
			
		}
	}
	
}
