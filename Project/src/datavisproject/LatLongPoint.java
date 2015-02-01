package datavisproject;

import java.util.ArrayList;

public class LatLongPoint {

	private float latitude;
	private float longitude;
	private String id;
	private String province;

	public LatLongPoint(float latitude, float longitude, String id) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.id = id;
		this.province = "";
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	// Returns true if this point is within the boundaries of the given polygon.
	// Based on the example for JS found at
	// http://www.counsellingbyabhi.com/2013/01/google-map-check-whether-point-latlong.html
	public boolean isContainedIn(LatLongPolygon polygon) {

		int crossings = 0;
		ArrayList<LatLongPoint> path = polygon.getPoints();

		// for each edge
		for (int i = 0; i < path.size(); i++) {
			LatLongPoint a = path.get(i);
			int j = i + 1;
			if (j >= path.size()) {
				j = 0;
			}
			LatLongPoint b = path.get(j);
			if (rayCrossesSegment(this, a, b)) {
				crossings++;
			}
		}
		return (crossings % 2 == 1);
	}

	// Helper function for isContainedIn
	private boolean rayCrossesSegment(LatLongPoint point, LatLongPoint a,
			LatLongPoint b) {
		float px = point.getLongitude();
		float py = point.getLatitude();
		float ax = a.getLongitude();
		float ay = a.getLatitude();
		float bx = b.getLongitude();
		float by = b.getLatitude();
		if (ay > by) {
			ax = b.getLongitude();
			ay = b.getLatitude();
			bx = a.getLongitude();
			by = a.getLatitude();
		}
		// alter longitude to cater for 180 degree crossings
		if (px < 0) {
			px += 360;
		}
		if (ax < 0) {
			ax += 360;
		}
		if (bx < 0) {
			bx += 360;
		}

		if (py == ay || py == by) {
			py += 0.00000001;
		}
		if ((py > by || py < ay) || (px > Math.max(ax, bx))) {
			return false;
		}
		if (px < Math.min(ax, bx)) {
			return true;
		}

		float red = (ax != bx) ? ((by - ay) / (bx - ax)) : Float.MAX_VALUE;
		float blue = (ax != px) ? ((py - ay) / (px - ax)) : Float.MAX_VALUE;
		return (blue >= red);

	}
}
