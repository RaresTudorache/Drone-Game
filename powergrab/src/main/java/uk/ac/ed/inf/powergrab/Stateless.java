package uk.ac.ed.inf.powergrab;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stateless extends Drone{
	
	
	public static void startGame() {
		
	  App.path = App.initializeLineString2();
	  
	  while(dronePower >= 1.25 && nrMoves<250) {
		  for (Direction d : Position.allDirections) {
			  Position nextPos = App.pos.nextPosition(d);
			  if(nextPos.inPlayArea()) {
			      go(nextPos,d);
			      break;
		      }
		 }
	  }
	  
	}
	

}
