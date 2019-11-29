 
package uk.ac.ed.inf.powergrab;

import java.io.*;
import com.google.common.io.CharStreams;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.LineString;
import com.google.gson.JsonElement;

/**
 * Hello world!
 *
 */
public class App 
{

	protected static Position pos = new Position(55.944425, -3.188396);

	protected static double latitudes[] = new double[50];   //geometry[1]
	protected static double longitudes[] = new double[50];  //geometry[0]
	protected static float powers[] = new float[50];
	protected static float coins[] = new float[50];
	protected static String symbols[] = new String[50];
	public static Random random;                                     //variable for going random
	public static String path;                                       //path of the drone
	
	//protected String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2019/09/15/powergrabmap.geojson";
	
	public App() throws IOException{
		
	}
	
    public static String parseMap(String mapString) throws IOException {
    	
  
    	URL mapUrl = new URL(mapString);
    	HttpURLConnection conn = (HttpURLConnection) mapUrl.openConnection();
    																						//conn.get, conn.put, conn.post;
    	conn.setReadTimeout(10000);   												  		//milliseconds
    	conn.setConnectTimeout(15000);  												    //milliseconds
    	conn.setRequestMethod("GET");
    	conn.setDoInput(true);
    	conn.connect();    																	//starts the query
    	
    	InputStream input = conn.getInputStream();
    	//String mapSource = input.toString();
    	Reader reader = new InputStreamReader(input);
    	String mapSource = CharStreams.toString(reader);                                          //geojson content from the server
    	
    	return mapSource;
    	
    }

	
    public static void getFeatures(String mapSource) throws IOException{
    	
       FeatureCollection fc  = FeatureCollection.fromJson(mapSource);            		  //returns a FeatureCollection
       List<Feature> F = fc.features();                                                  //f is a list of Feature objects
    	
    	for(int i=0;i<F.size();i++) {
    		
    		Point p = (Point) F.get(i).geometry();                                           	//g is a geometry object(it may also be a point)
    		                                                                                    //if p is a Point, then p.coordinates() is a list of double precision numbers!!!
    		longitudes[i] = p.coordinates().get(0);                                                     //coins and power are floating point numbers
    		latitudes[i] = p.coordinates().get(1);                                                   //if f is a Feature with property coins, then f.getProperty("coins") is a JsonElement
    		coins[i] = F.get(i).getProperty("coins").getAsFloat();                              //convert a JsonElement using method such as getAsString() and getAsFloat() 
    		powers[i] = F.get(i).getProperty("power").getAsFloat();
    		symbols[i] = F.get(i).getProperty("marker-symbol").getAsString();
    	}
   }
    
    public static String initializeLineString2() {
		String init = "{\"TYPE\": \"LineString\",\"coordinates\": [";
		String end = "],\"properties\": {}}";
		String pos_str = App.pos.toString();
		return init + pos_str + end;

	}

    public static void main( String[] args ) throws IOException 	{
    	
    	random = new Random(5678);
    	
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2020/12/28/powergrabmap.geojson";
    	String mapSource = parseMap(mapString);
        getFeatures(mapSource);
    	//Stateless.startGameStateless();
        Stateful.startGameStateful();
    	System.out.println(path);
    	System.out.println(Drone.dronePower);
    	System.out.println(Drone.droneCoins);
    	System.out.println(Drone.nrMoves);
    }
}