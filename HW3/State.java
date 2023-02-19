/** 
 * This class represents a single state from the input file.
 *
 * Added to by: lcosta
 */

 public class State  {
	/**
	 * The name of the state.
	 */
	private String name;
	
	/**
	 * The latitude of the center of the state.
	 */
	private double lat;

	/**
	 * The longitude of the center of the state.
	 */
	private double lon;
	
	public State(String name, double lat, double lon) {
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lon;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj instanceof Tweet == false) return false;
		State other = (State) obj;
		return name.equals(other.name) && lat == other.lat && lon == other.lon;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + ((Double)lat).hashCode() + ((Double)lon).hashCode();
	}

	public String toString() {
		return name + " @ " + lat + ", " + lon;
	}
	
}
