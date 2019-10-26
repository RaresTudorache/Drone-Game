package uk.ac.ed.inf.powergrab;

import java.io.*;
import com.google.common.io.CharStreams;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
	
	public static double grab = 0.00025;
	public static double radius = 0.0003;
	
	protected static double drone_power = 250.0;
	protected static int nr_moves = 0;
	protected static double drone_coins = 0;
	protected static Position pos = new Position(55.944425, -3.188396);
	//protected static int mapLength;
	protected static double latitudes[] = new double[50];   //geometry[1]
	protected static double longitudes[] = new double[50];  //geometry[0]
	protected static float powers[] = new float[50];
	protected static float coins[] = new float[50];
	protected static String symbols[] = new String[50];
	
    public static void parseMaps(String mapString) throws IOException {
    	
  
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
    	
    	//for(int i=0;i<50;i++) {
    		//System.out.println(coins[i]);
    //	}
    	System.out.println(fc);
    }
	
	
    public static void main( String[] args ) throws IOException 	{
    	String mapString = "http://homepages.inf.ed.ac.uk/stg/powergrab/2019/09/15/powergrabmap.geojson";
    	parseMaps(mapString);
        
    }
}
