package uk.ac.ed.inf.powergrab;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stateless extends Drone{
	
	
	public static void startGame() {
		
	  App.path = App.initializeLineString2();
	  List<Direction> allDirections = Arrays.asList(Direction.values());
	  while(dronePower >= 1.25 && nrMoves<250) {
		  for (Direction d : allDirections) {
			  Position nextPos = App.pos.nextPosition(d);
			  if(nextPos.inPlayArea()) {
			      go(nextPos,d);
			      break;
		      }
			  }
	  }
	  
	}
	

}
