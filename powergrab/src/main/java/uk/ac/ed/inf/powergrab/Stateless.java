package uk.ac.ed.inf.powergrab;


import java.util.Arrays;
import java.util.List;

public class Stateless extends Drone{
	
	//protected static Position pos = new Position(55.944425, -3.188396);
	
	public static void grab(Position pos) {
		
		List<Direction> allDirections = Arrays.asList(Direction.values());
	  //List<Position> futurePositions = Drone.allNextPositions(pos);
	  for (Direction d : allDirections) {
		  Position nextPos = pos.nextPosition(d);
		  if(nextPos.inPlayArea()) pos = nextPos;
		  FeatureInRange(nextPos);
	  }
	  
	}
	
	public static void main(String[] args) {
		//System.out.println(allNextPositions(pos));
	}
	

}
