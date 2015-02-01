package datavisproject;

import java.util.ArrayList;

public class LatLongPolygon {

	private String id;
	private ArrayList<LatLongPoint> points;

	public LatLongPolygon(String id) {
		this.id = id;
		this.points = new ArrayList<LatLongPoint>();
	}

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
