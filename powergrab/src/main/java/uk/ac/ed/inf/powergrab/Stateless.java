package uk.ac.ed.inf.powergrab;



public class Stateless extends Drone{
	
	
	public static void goStatelessGo(Position nextPos, Direction d) {
		int nrFeatures = nrFeaturesinRange(nextPos);                             //IMPROVEMENT: change getClosesStation
		int closest = getClosestStation(nextPos);
			if(nrFeatures == 1) {
				if(App.coins[closest] >0 ) {
					move(d);
					addToLine(App.pos);
					positiveCollect(closest);	
				}
				else {
					moveRandom(App.pos);
					addToLine(App.pos);
				}
			}
			else if(nrFeatures ==0 ){
				moveRandom(App.pos);
				addToLine(App.pos);
				
			}
			else {                  			//if there is more than on e station in range
			
			  	if(App.coins[closest] > 0) {                                                         //if the closest station is positive
			  		move(d);
			  		addToLine(App.pos);
			  		positiveCollect(closest);
			  		
			  	}
			  	else {
			  		moveRandom(App.pos);
			  		addToLine(App.pos);
	
			  	}
			}	
}

	
   public static void startGameStateless() {
	  App.path = App.initializeLineString2();
	  
	  while(dronePower >= 1.25 && nrMoves<250) {
		  for (Direction d : Position.allDirections) {
			  Position nextPos = App.pos.nextPosition(d);
			  if(nextPos.inPlayArea()) {
				  goStatelessGo(nextPos,d);
			      break;
		      }
		 }
	  }
	  
	}
	

}
