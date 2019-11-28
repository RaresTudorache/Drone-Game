package uk.ac.ed.inf.powergrab;

import java.awt.List;
import java.util.ArrayList;

public class Stateful extends Drone{


	
/*	public static void goStatefulgo(Position nextPos, Direction dir) {
		//int closestP = closestPositiveStation(nextPos);                          //look at the closest positive station
		//Direction dir = getNextDirection(closestP);                              // get the direction of the next positive station 
		//nextPos = moveStateful(dir);                                             //always move towards the closest positive station
		//addToLine(App.pos);   //inainte sa te muti vezi daca e ok sa te muti acolo
		int closestStation = getClosestStation(nextPos);                         //tine cont de cea mai apropiata statie din rangeul tau
		int nrFeatures = nrFeaturesinRange(nextPos);  							 //verifica daca sunt in range si cate sunt
		
		if(nrFeatures == 0) {
			nextPos = moveStateful(dir);    									  //always move towards the closest positive station
			addToLine(App.pos);
		}
		else if(nrFeatures == 1) {
			if(App.coins[closestStation] > 0) {
				App.pos = moveStateful(dir);    									  //always move towards the closest positive station
				addToLine(App.pos);
				positiveCollect(closestStation);
			}
			else return;         //if the station is negative
			                     //go to the next closest station                                     				                                                     
		}
		else {
			if(App.coins[closestStation] > 0) {
				App.pos = moveStateful(dir);    									  //always move towards the closest positive station
				addToLine(App.pos);
				positiveCollect(closestStation);
			}
			else return;
		}
	}*/

	/*public static void ifnot(Position pos) {
		ArrayList<Integer> closestStations = getClosestStations(pos);
		 ArrayList<Direction> orderedDirections = getNextDirections(closestStations);
		
		for(Direction d : orderedDirections) {
			  pos = App.pos.nextPosition(d);
			if(pos.inPlayArea()) {
			  goStatefulgo(pos,d);
			}
		}
	}*/
	
	public static void startGameStateful2() {
		App.path = App.initializeLineString2();
		ArrayList<Integer> closestStations = getClosestStations(App.pos);
		//int nr =0;
		int closestPositiveStation = closestPositiveStation(App.pos);
		ArrayList<Direction> orderedDirections = getNextDirections(closestStations);
		while(dronePower >= 1.25 && nrMoves<250) {
			
	
		if(closestPositiveStation == -1) {
			moveRandom(App.pos);
			addToLine(App.pos);
		} 
		else {
		   for(Direction d : orderedDirections) { 
			   Position nextPos = App.pos.nextPosition(d);  									  //always move towards the closest positive station
			   if(nextPos.inPlayArea()) {
				   int closestStation = getClosestStation(nextPos);                         //tine cont de cea mai apropiata statie din rangeul tau
				   int nrFeatures = nrFeaturesinRange(nextPos);  
				   
				   if(nrFeatures == 0) {
					   App.pos = moveStateful(d);    									  //always move towards the closest positive station
					   addToLine(App.pos);
					   break;
				   }
				   else if(nrFeatures == 1) {
						if(App.coins[closestStation] < 0) {
							//System.out.println("ajutor");
							continue;
						}
						else {
							App.pos = nextPos;    									  //always move towards the closest positive station
							addToLine(App.pos);
							positiveCollect(closestStation);
							//System.out.println("ajutor");
							break;
						}
					}
					else {
						if(App.coins[closestStation] < 0) {
							//System.out.println("ajutor");
							continue;
							
						}
						else {
							App.pos = nextPos;    									  //always move towards the closest positive station
							addToLine(App.pos);
							positiveCollect(closestStation);
							//System.out.println("ajutor");
							break;
						}
					}
			   }
		   }
		   
		 }
		
	   }
	}
	
	public static void startGameStateful() {
		App.path = App.initializeLineString2();
		ArrayList<Integer> closestStations = getClosestStations(App.pos);
		ArrayList<Direction> orderedDirections = getNextDirections(closestStations);
		//int closestPstation = closestPositiveStation(App.pos); //ia cea mai apropiata statie pozitiva de initial point
		//Direction dir = getNextDirection(closestPstation); //ia directia catre cea mai apropiata statie pozitiva
		//Position startPos = App.pos.nextPosition(dir);
		for (int i = 0; i < orderedDirections.size(); i++) {
			System.out.println(orderedDirections.get(i));
		}
		while(dronePower >= 1.25 && nrMoves<250) {
			for(Direction d : orderedDirections) {
				//System.out.println("help");
				//System.out.println(d);
				Position nextPos = App.pos.nextPosition(d);
				if(nextPos.inPlayArea()) {
					
					int closestStation = getClosestStation(nextPos); 
					int nrFeatures = nrFeaturesinRange(nextPos);  
					if(nrFeatures == 0) {
						App.pos = nextPos;    									  //always move towards the closest positive station
						addToLine(App.pos);
						System.out.println("ajutor");
                        break;
                   
					}
					else if(nrFeatures == 1) {
						if(App.coins[closestStation] < 0) {
							System.out.println("ajutor");
							continue;
						}
						else {
							App.pos = nextPos;    									  //always move towards the closest positive station
							addToLine(App.pos);
							positiveCollect(closestStation);
							System.out.println("ajutor");
							break;
						}
					}
					else {
						if(App.coins[closestStation] < 0) {
							System.out.println("ajutor");
							continue;
							
						}
						else {
							App.pos = nextPos;    									  //always move towards the closest positive station
							addToLine(App.pos);
							positiveCollect(closestStation);
							System.out.println("ajutor");
							break;
						}
					}
				}
			}
		} 
		
		
	}
	
}
