/** 
 * This class represents a single tweet from the input file.
 *
 * Added to by: lcosta
 */

public class Tweet  {
	
	/**
	 * The text contents of the tweet.
	 */
	private String text;
	
	/**
	 * The latitude of the tweet.
	 */
	private double lat;

	/**
	 * The longitude of the tweet.
	 */
	private double lon;
	
	public Tweet(String text, double lat, double lon) {
		this.text = text;
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lon;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof Tweet == false) return false;
		Tweet other = (Tweet) obj;
		return text.equals(other.text) && lat == other.lat && lon == other.lon;
	}

	@Override
	public int hashCode() {
		return text.hashCode() + ((Double)lat).hashCode() + ((Double)lon).hashCode();
	}

	public String toString() {
		return text + " @ " + lat + ", " + lon;
	}
	
}
