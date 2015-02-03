package datavisproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

// ProvinceProcessor
// Assigns a province to (long,lat) points and outputs a file
// Based on whether or not they are inside the province polygons
public class ProvinceProcessor {

	static String prefix = "C:\\Users\\F\\";

	public static String municipalitiesFile = prefix
			+ "Dropbox\\Studie\\Data Visualization\\Project\\datasets\\gemeenten_nieuw.csv";
	public static String latlongFile = prefix
			+ "Dropbox\\Studie\\Data Visualization\\Project\\datasets\\latlong.txt";
	public static String outputFile = prefix
			+ "Dropbox\\Studie\\Data Visualization\\Project\\datasets\\output.txt";

	public static HashMap<String, ArrayList<LatLongPolygon>> provinces;
	public static HashMap<String, ArrayList<LatLongPolygon>> municipalities;
	public static ArrayList<LatLongPoint> points;

	public static void main(String[] args) {

		municipalities = readFileIntoPolygons(municipalitiesFile);
		readPoints();
		assignMunicipalities();
		outputToFile();
	}

	private static HashMap<String, ArrayList<LatLongPolygon>> readFileIntoPolygons(
			String fileName) {
		String line;
		BufferedReader br;
		LatLongPolygon currentItem;
		HashMap<String, ArrayList<LatLongPolygon>> itemMap = new HashMap<String, ArrayList<LatLongPolygon>>();

		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				String[] lineSplit = line.split(",");
				currentItem = lineIntoPolygon(lineSplit);
				if (itemMap.get(currentItem.getId()) != null) {
					itemMap.get(currentItem.getId()).add(currentItem);
				} else {
					ArrayList<LatLongPolygon> newPolyList = new ArrayList<LatLongPolygon>();
					newPolyList.add(currentItem);
					itemMap.put(currentItem.getId(), newPolyList);
				}
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(fileName + " read into memory.");
		return itemMap;

	}

	// Helper method to convert a comma-separated line representing one or more
	// polygons into an arraylist of polygons
	public static LatLongPolygon lineIntoPolygon(String[] line) {
		String itemName = line[line.length - 2] + "/" + line[line.length - 1];
		LatLongPolygon currentPolygon = new LatLongPolygon(itemName);

		// Get all points, last 2 values in array are the name so ignore those
		for (int i = 0; i < line.length - 2; i = i + 2) {
			// Parse long/lat, add the point to current polygon
			float longitude = Float
					.parseFloat(line[i].replaceAll("[\"()]", ""));
			float latitude = Float.parseFloat(line[i + 1].replaceAll("[\"()]",
					""));
			currentPolygon
					.add(new LatLongPoint(latitude, longitude, (itemName)));
		}
		return currentPolygon;
	}

	// Read the points file into a list of latlongpoints
	public static void readPoints() {

		BufferedReader br;
		points = new ArrayList<LatLongPoint>();

		try {
			br = new BufferedReader(new FileReader(latlongFile));
			br.readLine();
			String line;
			String[] splitLine;
			LatLongPoint currentPoint;
			float latitude;
			float longitude;

			while ((line = br.readLine()) != null) {
				splitLine = line.split(",");
				latitude = Float.parseFloat((splitLine[1].replaceAll("[\"()]",
						"")));
				longitude = Float.parseFloat((splitLine[2].replaceAll("[\"()]",
						"")));
				currentPoint = new LatLongPoint(latitude, longitude,
						splitLine[0]);
				points.add(currentPoint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Points read into memory.");

	}

	private static void assignMunicipalities() {
		// For every point, check if it is in polygons corresponding to each
		// province
		int count = 0;
		for (LatLongPoint point : points) {
			for (Entry<String, ArrayList<LatLongPolygon>> entry : municipalities
					.entrySet()) {
				// Don't check the next province if we have found it
				if (!point.getMunicipality().equals("")) {
					continue;
				} else {
					for (LatLongPolygon currentPolygon : entry.getValue()) {
						if (point.isContainedIn(currentPolygon)) {
							String[] splitName = entry.getKey().split("/");
							point.setMunicipality(splitName[0]);
							point.setProvince(splitName[1]);
							continue;
						}
					}
				}
			}
			if (point.getMunicipality().equals("")) {
				count++;
			}
		}
		System.out
				.println("Municipalities and Provinces assigned. Could not assign "
						+ count + " points.");

	}

	public static void outputToFile() {
		try {
			File file = new File(outputFile);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("id,latitude,longitude,municipality,province\n");
			String line;
			// Output all points for which we found the province and
			// municipality
			for (LatLongPoint point : points) {
				if (!point.getMunicipality().equals("")) {
					line = point.getId() + "," + point.getLatitude() + ","
							+ point.getLongitude() + ","
							+ point.getMunicipality() + ","
							+ point.getProvince() + "\n";
					bw.write(line);
				}
			}
			bw.close();
			System.out.println("Successfully wrote output.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
