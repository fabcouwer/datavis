package datavisproject;

import java.util.ArrayList;

// Municipality class
// Represents a municipality in the Netherlands
public class Municipality {

	private String name;
	private String province;
	private ArrayList<PTStop> busStops;
	private ArrayList<String> busRoutes;
	private int population;
	private int popPerSquareKm;

	public Municipality(String name, String province, int population,
			int popPerSquareKm) {
		this.name = name;
		this.province = province;
		this.population = population;
		this.popPerSquareKm = popPerSquareKm;
		this.busStops = new ArrayList<PTStop>();
		this.busRoutes = new ArrayList<String>();
	}

	// Add a bus stop and its routes to this municipality
	public void addStop(PTStop busStop) {
		busStops.add(busStop);
		for (String route : busStop.getRoutes()) {
			if (!busRoutes.contains(route))
				busRoutes.add(route);
		}
	}

	// Returns the number of distinct busroutes operating in this municipality
	public int numberOfRoutes() {
		return busRoutes.size();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Municipality))
			return false;
		Municipality that = (Municipality) other;
		return this.name.equals(that.getName());
	}

	// Getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getPopPerSquareKm() {
		return popPerSquareKm;
	}

	public void setPopPerSquareKm(int popPerSquareKm) {
		this.popPerSquareKm = popPerSquareKm;
	}

	public ArrayList<PTStop> getBusStops() {
		return busStops;
	}

	public ArrayList<String> getBusRoutes() {
		return busRoutes;
	}

}
