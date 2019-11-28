package uk.ac.ed.inf.powergrab;

import java.awt.List;
import java.util.ArrayList;

public class Stateful extends Drone{

	
	public static void goStatefulgo(Position nextPos, Direction dir) {
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
	}
	
	public static void startGameStateful() {
		App.path = App.initializeLineString2();
		while(dronePower >= 1.25 && nrMoves<250) {
			ArrayList<Integer> closestStations = getClosestStations(App.pos);
			ArrayList<Direction> orderedDirections = getNextDirections(closestStations);
			//orderedDirections tine toate directiile orodonate fata de pozitia initiala
			//fa un for printre toate satiile pozitive
			for(Direction d : orderedDirections) {
				Position nextPos = App.pos.nextPosition(d);
				goStatefulgo(nextPos,d);
				break;
				//int closestP = closestPositiveStation(nextPos);
				//if(closestP == -1) goStatefulgo(nextPos, dir); //xxxxx
				
				
			}
		}
			/*for(Direction d : Position.allDirections) {
				Position nextPos = App.pos.nextPosition(d);
				int closestP = closestPositiveStation(nextPos);
				if(closestP == -1) goStatefulgo(nextPos, dir);
				Direction dir = getNextDirection(closestP); 
				goStatefulgo(nextPos,dir);
			}*/
			
			 
			//Position nextPos = moveStateful(dir);
			//addToLine(nextPos);
		
			     //daca s au terminat statiile pozitive -> misca te random evitand cele rosii
			//else {
			//App.pos = moveStateful(dir);
			//addToLine(App.pos);
		//	positiveCollect(closestP);
		//	}
		
		
	}
	
}
