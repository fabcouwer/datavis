package datavisproject;

import java.util.ArrayList;

// LatLongPolygon class
// Represents a polygon made up of LatLongPoint objects
public class LatLongPolygon {

	private String id;
	private ArrayList<LatLongPoint> points;

	public LatLongPolygon(String id) {
		this.id = id;
		this.points = new ArrayList<LatLongPoint>();
	}

	// Getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<LatLongPoint> getPoints() {
		return points;
	}

	public void add(LatLongPoint p) {
		this.points.add(p);
	}

}
