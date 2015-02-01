package datavisproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// ProvinceProcessor
// Assigns a province to (long,lat) points and outputs a file
// Based on whether or not they are inside the province polygons
public class ProvinceProcessor {

	public static String provincesFile = "D:\\Friso\\Dropbox\\Studie\\Data Visualization\\Project\\datasets\\provincies.txt";
	public static String latlongFile = "D:\\Friso\\Dropbox\\Studie\\Data Visualization\\Project\\datasets\\latlong.txt";
	public static String outputFile = "D:\\Friso\\Dropbox\\Studie\\Data Visualization\\Project\\datasets\\output.txt";

	public static HashMap<String, ArrayList<LatLongPolygon>> provinces;
	public static ArrayList<LatLongPoint> points;

	public static void main(String[] args) {

		readProvinces();
		readPoints();
		assignProvinces();

		// Output a file with id - province

	}

	// Make (long,lat) arrays for provinces
	public static void readProvinces() {
		String line;
		BufferedReader br;

		String provinceName;
		ArrayList<LatLongPolygon> currentProvince;
		provinces = new HashMap<String, ArrayList<LatLongPolygon>>();

		try {
			br = new BufferedReader(new FileReader(provincesFile));
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split(",");
				currentProvince = lineIntoPolygons(lineSplit);
				provinceName = lineSplit[lineSplit.length - 1];
				provinces.put(provinceName, currentProvince);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<LatLongPolygon> lineIntoPolygons(String[] line) {
		ArrayList<LatLongPolygon> result = new ArrayList<LatLongPolygon>();
		String provinceName = line[line.length - 1];
		int numPolygons = 1;
		LatLongPolygon currentPolygon = new LatLongPolygon(provinceName
				+ numPolygons);

		// Get all points, last value in array is province name so ignore it
		for (int i = 0; i < line.length - 1; i = i + 2) {
			// Parse long/lat, add the point to current polygon
			float longitude = Float.parseFloat(line[i].replaceAll("(|)", ""));
			float latitude = Float
					.parseFloat(line[i + 1].replaceAll("(|)", ""));
			currentPolygon.add(new LatLongPoint(latitude, longitude,
					(provinceName + numPolygons)));

			// If we have reached the end of the polygon, add it to the array
			if (line[i + 1].endsWith(")")) {
				numPolygons++;
				result.add(currentPolygon);
				currentPolygon = new LatLongPolygon(provinceName + numPolygons);
			}
		}
		// Add the current polygon at the end if the line only consisted of 1
		// polygon (and thus did not have any brackets)
		if (result.isEmpty()) {
			result.add(currentPolygon);
		}
		return result;
	}

	// Read the points file into a list of latlongpoints
	public static void readPoints() {
		String line;
		BufferedReader br;

		try {
			br = new BufferedReader(new FileReader(provincesFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				// TODO
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Assign provinces to all latlongpoints in the list
	public static void assignProvinces() {

	}

}
