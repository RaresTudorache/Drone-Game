package uk.ac.ed.inf.powergrab;

import java.util.Arrays;
import java.util.List;

public class Position {
	

	public double latitude;

	public double longitude;
	
	public static List<Direction> allDirections = Arrays.asList(Direction.values());
	
	public Position(double latitude, double longitude) { 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Position nextPosition(Direction direction) { 

	
		int i = allDirections.indexOf(direction);	
		
		double degree = 22.5 * i;
		double newlong = this.longitude + Math.sin(Math.toRadians(degree))*Drone.radius;
		double newlat = this.latitude + Math.cos(Math.toRadians(degree))*Drone.radius;
	
		Position nextPos = new Position(newlat,newlong);
		return nextPos;
	}
	
	public boolean inPlayArea() { 
		if( longitude < -3.184319 && longitude > -3.192473 &&
				latitude > 55.942617 && latitude < 55.946233) {
			return true;
		}
	  return false;
	}
	
	public String toString()
	{
		return "[" + longitude + "," + latitude + "]";
	}

}