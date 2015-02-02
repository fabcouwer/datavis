package datavisproject;

import java.util.ArrayList;

//PTStop: Represents a public transportation stop
//We only consider bus stops (for now)
public class PTStop {

	private String id;
	private String name;
	private String town;
	private String municipality;
	private String province;
	private String latitude;
	private String longitude;
	private String operator;

	private ArrayList<String> lines;

	public PTStop(String id, String name, String town, String latitude,
			String longitude) {

		this.id = id;
		this.name = name;
		this.town = town;
		this.province = "";
		this.municipality = "";
		this.setOperator("");
		this.latitude = latitude;
		this.longitude = longitude;
		this.lines = new ArrayList<String>();

	}

	// Add a line if it is not already in the list of lines
	public void addLine(String line) {
		if (!lines.contains(line)) {
			lines.add(line);
		}
	}

	// Getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String toString() {
		String ts = "<PTStop: Name = " + name + " | Town = " + town
				+ " | Municipality = " + municipality + " | Province = "
				+ province + " | Lat/Long = " + latitude + " / " + longitude
				+ " | Routes : " + routesToString() + ">";
		return ts;
	}

	public String routesToString() {
		String toReturn = "";
		for (String route : lines) {
			toReturn += route + "/";
		}
		return (toReturn.substring(0, toReturn.length() - 1));
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
