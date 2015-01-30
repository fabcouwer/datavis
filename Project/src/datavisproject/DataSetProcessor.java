package datavisproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//Processes the datasets from OpenOV into a format usable in our visualization
public class DataSetProcessor {

	public static String fileName = "C:\\Users\\F\\Downloads\\stops.csv";
	public static String fileName2 = "C:\\Users\\F\\Downloads\\routes-at-stop.csv";
	public static String fileName3 = "C:\\Users\\F\\Downloads\\gemeenten.csv";
	public static HashMap<String, PTStop> busstops;

	// public static ArrayList<String> longlats;

	public static void main(String[] args) {
		busstops = new HashMap<String, PTStop>();
		// Read
		readPTStops();
		readRoutesAtStop();
		// setProvinces();
	}

	// Parse stops.csv into PTStop objects
	public static void readPTStops() {
		// Read file
		BufferedReader br = null;
		String line = "";
		// longlats = new ArrayList<String>();
		try {
			String id;
			String name;
			String town;
			String latitude;
			String longitude;
			// String longlat;
			Scanner sc;
			int count = 0;

			br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			while ((line = br.readLine()) != null) {
				// If line ends with f, it can be ignored
				if (line.endsWith(",f")) {
					count++;
				} else {
					sc = new Scanner(line);
					sc.useDelimiter(",");
					// Scan id, skip operator_id
					id = sc.next();
					sc.next();

					// Read name: if it is in quotes there is at least one comma
					// In that case, keep adding parts until we find the end
					name = sc.next();
					if (name.startsWith("\"")) {
						name = name.substring(1);
						boolean done = false;
						while (!done) {
							name += "," + sc.next();
							if (name.endsWith("\""))
								name = name.substring(0, name.length() - 1);
							done = true;
						}
					}

					// Scan remaining data for this object
					town = sc.next();
					// Ignore train station platforms
					if (!(town.equals(""))) {
						// Skip platform code
						sc.next();
						latitude = sc.next();
						longitude = sc.next();

						PTStop ptstop = new PTStop(id, name, town, latitude,
								longitude);
						busstops.put(id, ptstop);
						// System.out.println(ptstop.toString());

						// longlat = id + "," + latitude + "," + longitude;
						// longlats.add(longlat);
					} else {
						// System.out.println("Omitted this entry.");
						count++;
					}
					sc.close();

				}
			}
			br.close();
			System.out.println("Reading stops - done.");
			System.out.println("Number of lines omitted: " + count + ".");

			// Output points as (id, lat, long) to check for province
			// Write longlats to a file
			/*
			 * FileWriter fileWriter = new FileWriter(
			 * "C:\\Users\\F\\Downloads\\longlat.txt"); BufferedWriter
			 * bufferedWriter = new BufferedWriter(fileWriter);
			 * bufferedWriter.write("id,latitude,longitude");
			 * bufferedWriter.newLine(); for (String s : longlats) {
			 * bufferedWriter.write(s); bufferedWriter.newLine(); }
			 * bufferedWriter.flush(); bufferedWriter.close();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Given PTStop objects, add the routes that stop at them as listed in
	// routes-at-stop
	private static void readRoutesAtStop() {
		// Read file
		BufferedReader br = null;
		String line = "";
		PTStop ptstop;
		String[] routes;
		try {

			br = new BufferedReader(new FileReader(fileName2));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split(",");
				ptstop = busstops.get(splitLine[0]);
				if (ptstop != null) {
					routes = splitLine[1].split(";");
					for (int i = 0; i < routes.length; i++) {
						ptstop.addLine(routes[i]);
					}
					// System.out.println(ptstop.toString());
				}
			}
			br.close();
			System.out.println("Combining stops and routes - done.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * private static void setProvinces() { System.out.println("There are " +
	 * busstops.size() + " distinct bus stops."); int count = 0; HashMap<String,
	 * String> townProvince = new HashMap<String, String>(); // Create
	 * town-province hashmap try { BufferedReader br = new BufferedReader(new
	 * FileReader(fileName3)); String line = br.readLine(); while ((line =
	 * br.readLine()) != null) { String[] splitLine = line.split(";");
	 * townProvince.put(splitLine[0], splitLine[3]); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * // Add province to every PTStop PTStop current; String province;
	 * for(Map.Entry<String, PTStop> entry : busstops.entrySet()){ current =
	 * entry.getValue(); province = townProvince.get(current.getTown());
	 * if(province != null){ entry.getValue().setProvince(province); } else{
	 * System.out.println(current.getTown()); count++; } }
	 * System.out.println("Final count: " + count); }
	 */
}
