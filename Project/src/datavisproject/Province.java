package datavisproject;

import java.util.ArrayList;

public class Province {

	private String name;
	private ArrayList<Municipality> municipalities;
	private ArrayList<String> busRoutes;

	public Province(String name) {
		this.name = name;
		this.municipalities = new ArrayList<Municipality>();
		this.busRoutes = new ArrayList<String>();
	}

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

	public int getTotalPopulation() {
		int sum = 0;
		for (Municipality m : municipalities) {
			sum += m.getPopulation();
		}
		return sum;
	}

	public int getTotalPopPerSquareKm() {
		int sum = 0;
		for (Municipality m : municipalities) {
			sum += m.getPopPerSquareKm();
		}
		return sum;
	}

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
