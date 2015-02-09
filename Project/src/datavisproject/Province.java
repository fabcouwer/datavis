package datavisproject;

import java.util.ArrayList;

//Province class
//Represents a province in the Netherlands
public class Province {

	private String name;
	private ArrayList<Municipality> municipalities;
	private ArrayList<String> busRoutes;

	public Province(String name) {
		this.name = name;
		this.municipalities = new ArrayList<Municipality>();
		this.busRoutes = new ArrayList<String>();
	}

	// Adds a municipality to the list
	// This also adds bus routes, but in the end we did not use this in our
	// results as we can only identify bus routes by their number
	// and a province might have multiple routes with the same number
	public void addMunicipality(Municipality m) {
		if (!municipalities.contains(m)) {
			municipalities.add(m);
			for (String route : m.getBusRoutes()) {
				if (!busRoutes.contains(route))
					busRoutes.add(route);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Calculates the total population by summing over municipalities
	public int getTotalPopulation() {
		int sum = 0;
		for (Municipality m : municipalities) {
			sum += m.getPopulation();
		}
		return sum;
	}

	// Calculates average population per km2 by averaging over municipalities
	public int getTotalPopPerSquareKm() {
		int sum = 0;
		for (Municipality m : municipalities) {
			sum += m.getPopPerSquareKm();
		}
		return sum / municipalities.size();
	}

	// Calculates the total amount of bus stops by summing over municipalities
	public int getTotalStops() {
		int sum = 0;
		for (Municipality m : municipalities) {
			sum += m.getBusStops().size();
		}
		return sum;
	}

	public int getTotalRoutes() {
		return busRoutes.size();
	}

	public boolean equals(Object other) {
		if (!(other instanceof Province))
			return false;
		Province that = (Province) other;
		return this.name.equals(that.getName());
	}

}
