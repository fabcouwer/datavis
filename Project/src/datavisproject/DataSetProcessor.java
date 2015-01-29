package datavisproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

//Processes the datasets from OpenOV into a format usable in our visualization
public class DataSetProcessor {

	public static String fileName = "C:\\Users\\F\\Downloads\\stops.csv";
	public static String fileName2 = "C:\\Users\\F\\Downloads\\routes-at-stop.csv";
	public static HashMap<String, PTStop> busstops;

	public static void main(String[] args) {
		busstops = new HashMap<String, PTStop>();
		readPTStops();
		readRoutesAtStop();
	}

	// Parse stops.csv into PTStop objects
	public static void readPTStops() {
		// Read file
		BufferedReader br = null;
		String line = "";
		try {
			String id;
			String name;
			String town;
			String latitude;
			String longitude;
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
						//System.out.println(ptstop.toString());
					} else {
						//System.out.println("Omitted this entry.");
						count++;
					}
					sc.close();
				}
				//System.out.println("SEPARATOR");
			}
			System.out.println("Number of lines omitted: " + count);
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
					System.out.println(ptstop.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
