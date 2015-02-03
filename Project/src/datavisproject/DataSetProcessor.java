package datavisproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

//Processes the datasets from OpenOV and our own province/municipality dataset into a format usable in our visualization
public class DataSetProcessor {

	static String prefix = "C:\\Users\\F\\Dropbox\\Studie\\Data Visualization\\Project\\datasets\\";

	private static String stopsFile = prefix + "stops.csv";
	private static String routesAtStopFile = prefix + "routes-at-stop.csv";
	private static String muniProviFile = prefix + "ids_mp.csv";
	private static String populationFile = prefix + "gemeenten_inwoners.csv";
	private static String outputFile = prefix + "output.csv";
	private static HashMap<String, PTStop> busstops;
	private static HashMap<String, Municipality> municipalities;
	private static HashMap<String, Province> provinces;

	// public static ArrayList<String> longlats;

	public static void main(String[] args) {
		busstops = new HashMap<String, PTStop>();
		readPTStops();
		readRoutesAtStop();
		readMandP();
		// outputStopsToFile();
		readMunicipalities();
		readPopulationFile();
		outputMunicipalitiesToFile();
	}

	// Parse stops.csv into PTStop objects
	private static void readPTStops() {
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
			String operator;
			Scanner sc;
			int count = 0;

			br = new BufferedReader(new FileReader(stopsFile));
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
					operator = sc.next().split(":")[0];

					// Read name: if it is in quotes there is at least one comma
					// In that case, keep adding parts until we find the end
					name = sc.next();
					if (name.startsWith("\"")) {
						name = name.substring(1);
						boolean done = false;
						while (!done) {
							name += ":" + sc.next();
							if (name.endsWith("\"")) {
								name = name.substring(0, name.length() - 1);
								done = true;
							}
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
						ptstop.setOperator(operator);
						busstops.put(id, ptstop);
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

			br = new BufferedReader(new FileReader(routesAtStopFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split(",");
				ptstop = busstops.get(splitLine[0]);
				if (ptstop != null) {
					routes = splitLine[1].split(";");
					for (int i = 0; i < routes.length; i++) {
						ptstop.addRoute(routes[i]);
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

	// Read municipalities and provinces for bus stops from our own file
	private static void readMandP() {

		try {
			String line;
			BufferedReader br;
			br = new BufferedReader(new FileReader(muniProviFile));
			br.readLine();
			String[] splitLine;
			PTStop currentStop;
			while ((line = br.readLine()) != null) {
				splitLine = line.split(",");
				currentStop = busstops.get(splitLine[0]);
				currentStop.setMunicipality(splitLine[3]);
				currentStop.setProvince(splitLine[4]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Reading municipalities and provinces - done.");

	}

	private static void outputStopsToFile() {
		try {
			File file = new File(outputFile);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("id,operator,name,latitude,longitude,town,municipality,province,routes\n");
			String line;
			PTStop current;
			// Output all points for which we found the province and
			// municipality
			for (Entry<String, PTStop> entry : busstops.entrySet()) {
				current = entry.getValue();
				if (!current.getMunicipality().equals("")) {
					line = current.getId() + "," + current.getOperator() + ","
							+ current.getName() + "," + current.getLatitude()
							+ "," + current.getLongitude() + ","
							+ current.getTown() + ","
							+ current.getMunicipality() + ","
							+ current.getProvince() + ","
							+ current.routesToString() + "\n";
					bw.write(line);
				}
			}

			bw.close();
			System.out.println("Successfully wrote output.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read municipalities and provinces population file, and add PTStops to
	// municipalities
	public static void readMunicipalities() {
		municipalities = new HashMap<String, Municipality>();
		provinces = new HashMap<String, Province>();
		PTStop currentStop;
		String mName;
		String pName;
		Municipality currentM;

		for (Entry<String, PTStop> entry : busstops.entrySet()) {
			currentStop = entry.getValue();
			if (!currentStop.getMunicipality().equals("")) {
				mName = currentStop.getMunicipality();
				pName = currentStop.getProvince();

				// Add municipality if it does not exist yet
				if (municipalities.get(mName) == null) {
					Municipality newM = new Municipality(mName, pName, 0, 0);
					newM.addStop(currentStop);
					currentM = newM;
					municipalities.put(mName, newM);
				} else {
					currentM = municipalities.get(mName);
					currentM.addStop(currentStop);
				}
				// Same for province
				if (provinces.get(pName) == null) {
					Province newP = new Province(pName);
					newP.addMunicipality(currentM);
					provinces.put(pName, newP);
				} else {
					provinces.get(pName).addMunicipality(currentM);
				}
			}

		}
		System.out.println("Made municipality and province objects.");

	}

	public static void readPopulationFile() {
		// Read the population file
		try {
			BufferedReader br;
			String line = "";
			Municipality currentM;

			br = new BufferedReader(new FileReader(populationFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split(";");
				currentM = municipalities.get(splitLine[0]);
				if (currentM != null) {
					currentM.setPopulation(Integer.parseInt(splitLine[1]));
					currentM.setPopPerSquareKm(Integer.parseInt(splitLine[2]));
				} else
					System.out.println("Could not find data in memory for "
							+ splitLine[0]);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Added population data to municipalities.");
	}

	public static void outputMunicipalitiesToFile() {
		try {
			File file = new File(outputFile);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write("type,name,province,population,populationPerKm2,numberOfStops,numberOfRoutes\n");
			String line;
			Municipality currentM;
			for (Entry<String, Municipality> entry : municipalities.entrySet()) {
				currentM = entry.getValue();
				line = "municipality," + currentM.getName() + ","
						+ currentM.getProvince() + ","
						+ currentM.getPopulation() + ","
						+ currentM.getPopPerSquareKm() + ","
						+ currentM.getBusStops().size() + ","
						+ currentM.getBusRoutes().size() + "\n";
				bw.write(line);
			}

			Province currentP;
			for (Entry<String, Province> entry : provinces.entrySet()) {
				currentP = entry.getValue();
				line = "province," + entry.getKey() + "," + entry.getKey()
						+ "," + currentP.getTotalPopulation() + ","
						+ currentP.getTotalPopPerSquareKm() + ","
						+ currentP.getTotalStops() + ","
						+ currentP.getTotalRoutes() + "\n";
				bw.write(line);
			}

			bw.close();
			System.out.println("Successfully wrote output.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
