/**
 * Assignment #3: JSON
 * Assignment details: https://docs.google.com/document/d/1ymo39aqY80kcdheIe_Na7atHPcuOKJiTeWQHedZi2jg/
 * 
 * To compile: javac -cp json-simple-1.1.1.jar:. ReadFluTweets.java
 * To run: java -cp json-simple-1.1.1.jar:. ReadFluTweets -logfile=log.txt -datafile=flu_tweets.json -statesfile=states.json
 * 
 * @author lcosta
 * Due Feb 17, 2023
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ReadFluTweets {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Error with runtime arguments");
            return;
        }

        // Get names of data, state, and log files:
        String tweetfile = null;
        String statesfile = null;
        String logfile = null;
        try {
            tweetfile = getFileName("-datafile=", ".json", args);
            statesfile = getFileName("-statesfile=", ".json", args);
            logfile = getFileName("-logfile=", ".txt", args);
        }
        catch (IllegalArgumentException x) {
            System.out.println("Error with runtime arguments");
            return;
        }

        // Parsing file for tweets:
        Object stateObj = null;
        try {
            stateObj = new JSONParser().parse(new FileReader(statesfile));
        }
        catch (FileNotFoundException x) {
            System.out.println("State file not found");
            return;
        }
        catch (ParseException x) {
            System.out.println("Error with states file");
            return;
        }
        
        Set<State> states = new HashSet<>();

        Iterator itr1 = ((JSONArray) stateObj).iterator();
        try {
            while (itr1.hasNext()) {
                JSONObject JSONState = (JSONObject) itr1.next();
                String name = JSONState.get("name").toString();
                double latitude = Double.parseDouble(JSONState.get("latitude").toString());
                double longitude = Double.parseDouble(JSONState.get("longitude").toString());
                State s = new State(name, latitude, longitude);
                states.add(s);
            }
        }
        catch (NullPointerException x) {
            System.out.println("Error with state file");
            return;
        }
        
        //System.out.println(states);
        
        // Parsing file for tweets:
        Object tweetObj = null;
        try {
            tweetObj = new JSONParser().parse(new FileReader(tweetfile));
        }
        catch (FileNotFoundException x) {
            System.out.println("Data file not found");
            return;
        }
        catch (ParseException x) {
            System.out.println("Error with data file");
            return;
        }
        Set<Tweet> tweets = new HashSet<>();

        Iterator itr2 = ((JSONArray) tweetObj).iterator();
        while (itr2.hasNext()) {
            try {
                Tweet t = toFluTweet((JSONObject) itr2.next());
                if (t == null)
                    continue;
                tweets.add(t);
            }
            catch (IndexOutOfBoundsException x) {
                System.out.println("Error reading file");
                return;
            }
            catch (NullPointerException x) {
                System.out.println("Error reading file");
                return;
            }
        }
        // System.out.println(tweets.size());

        Map<String, Integer> perState = new TreeMap<>();
        
        try {
            File log = new File(logfile);
            if (log.createNewFile())
                System.out.println("File created: " + log.getName());
            // else 
            //     System.out.println("File already exists.");
            
            FileWriter writer = new FileWriter(logfile);
            for (Tweet t : tweets) {
                String closest = closestState(t.getLatitude(), t.getLongitude(), states);
                if (perState.containsKey(closest)) 
                    perState.put(closest, perState.get(closest) + 1);
                else 
                    perState.put(closest, 1);
                
                writer.write(closest + "  " + t.getText() + "\n");
            }
            writer.close();
        
        } catch (IOException e) {
            System.out.println("Error writing file");
            return;
        }
        // Resource for writing files: https://www.w3schools.com/java/java_files_create.asp

        Iterator<Map.Entry<String, Integer>> perStateItr = perState.entrySet().iterator();
          
        while(perStateItr.hasNext()) {
            Map.Entry<String, Integer> entry = perStateItr.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Retrieves file name from runtime arguments (or any other String array)
     * @param prefix the prefix that indicates the file of interest
     * @param type the file extension type being searched for
     * @param args the String array containing file names and their prefixes
     * @return the file name as a String
     */
    private static String getFileName(String prefix, String type, String[] args) {
        for (String s : args) {
            if (s.startsWith(prefix) && s.endsWith(type))
                return s.substring(prefix.length());
        }
        throw new IllegalArgumentException();
    }

    /**
     * Converts a JSONObject into a Tweet if it has text related to "flu" based on its contents
     * @param JSONtweet the JSONObject to be analyzed and converted into a Tweet
     * @return an instance of class Tweet with the information from the JSONObject 
     * or null if the JSONObject text does not contain keyword "flu"
     */
    private static Tweet toFluTweet(JSONObject JSONTweet) {
        //List<Tweet> fluTweets = new LinkedList<>();
        JSONArray location = (JSONArray) JSONTweet.get("location");

        //Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Object.toString()" because the return value of "org.json.simple.JSONObject.get(Object)" is null
        String text = JSONTweet.get("text").toString();

        if (!containsFlu(text)) 
            return null;

        // ADDRESS index out of bounds
        double latitude = Double.parseDouble(location.get(0).toString());
        double longitude = Double.parseDouble(location.get(1).toString());
        Tweet tweet = new Tweet(text, latitude, longitude);

        return tweet;
    }

    /**
     * Assesses whether a tweet contains the word "flu"
     * @param text the text to be analyzed
     * @return true if flu is present in the String and false otherwise
     */
    private static boolean containsFlu(String text) {
        text = text.toLowerCase();

        if (text.equals("flu") || text.equals("#flu"))
            return true;

        if (text.contains(" flu ") || text.contains(" #flu "))
            return true;
        
        if (text.endsWith(" flu") || text.endsWith(" #flu"))
            return true;
        
        if (text.startsWith("flu") || text.startsWith("#flu")) {
            int charAfter = text.indexOf(" flu") + 4;
            if (charAfter >= 3 && !Character.isLetter(text.charAt(charAfter)))
                return true;
        }
        
        // case: _flu followed by a non-letter
        int charAfter = text.indexOf(" flu") + 4;
        if (charAfter >= 4 && !Character.isLetter(text.charAt(charAfter))) 
            return true;

        return false;
    }

    /**
     * Returns the state closest to the coordinates given based on Euclidian distance.
     * 
     * @param lat the latitude of the coordinate pair
     * @param lon the longitude of the coordinate pair
     * @param states a set that contains states and the location of their center
     * @return the name of the state closest to the coordinates as a String
     */
    public static String closestState(double lat, double lon, Set<State> states) {
        // set closest to first state in list
        State closest = null;
        double distOfClosest = -1;

        for (State s : states) {
            double thisDist = dist(lat, lon, s.getLatitude(), s.getLongitude());
            if (closest == null) {
                closest = s;
                distOfClosest = thisDist;
            }
            else if (thisDist < distOfClosest) {
                distOfClosest = thisDist;
                closest = s;
                // System.out.println(closest.getName() + " is closer");
            }
        }

        return closest.getName();
    }

    /**
     * Finds the Euclidian distance between two coordinates.
     * @param lat1 latitude of the first location
     * @param lon1 longitude of the first location
     * @param lat2 latitude of the second location
     * @param lon2 longitude of the second location
     * @return the Euclidean distance between (lat1, lon1) and (lat2, lon2)
     */
    private static double dist(double lat1, double lon1, double lat2, double lon2) {
        return Math.pow((Math.pow((lat1 - lat2), 2.0) + Math.pow((lon1 - lon2), 2.0)), 0.5);
    }

    // https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/
}
