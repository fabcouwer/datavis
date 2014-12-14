package datavis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

//Reads a file with opening strategies and outputs:
// - A file with winrates for every distinct opening strategy 
// - A file for every strategy's performance against other strategies
public class WinRateExtractor {

	public static String inputFile = "5minuteOpeningStrat.csv";
	public static String outputFile1 = "ResultGeneral.csv";
	public static String outputFile2 = "ResultMatchups.csv";
	public static HashMap<String, OpeningStrategy> strats;

	public static void main(String[] args) {
		strats = new HashMap<String, OpeningStrategy>();

		// Read file
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		try {

			br = new BufferedReader(new FileReader(inputFile));
			br.readLine();
			// Extract 2 lines at a time for processing
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("[^a-zA-Z,12345678910]", "");
				// use comma as separator
				String[] radiant = line.split(csvSplitBy);
				System.out.println(line);
				line = br.readLine();
				System.out.println(line);
				String[] dire = line.split(csvSplitBy);
				processMatch(radiant, dire);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Update all opening strategies' winrates
		for (OpeningStrategy os : strats.values()) {
			os.updateTotalWinrate();
			os.updateWinrateHashmaps();
			System.out.println("Overall Stats:");
			System.out.println(os.toStringOverall());
			System.out.println("Individual Stats:");
			System.out.println(os.toStringIndividual());
		}

		// Output opening strategies to file
		writeResultsToCSV();

	}

	public static void processMatch(String[] radiant, String[] dire) {
		// Generate the name string for both strategies
		String radiantStrategy = extractStrategy(radiant);
		String direStrategy = extractStrategy(dire);

		// If both teams used the same strategy, handle them as one
		if (radiantStrategy.equals(direStrategy)) {
			OpeningStrategy strat = strats.get(radiantStrategy);
			if (strat == null) {
				strat = new OpeningStrategy(radiantStrategy);
			}

			strat.incrementWins(radiantStrategy, radiant[7]);
			strat.incrementLosses(radiantStrategy, radiant[7]);
			strats.put(radiantStrategy, strat);

		} else {
			// See if the strategies are already in the hashmap
			OpeningStrategy radiantOS = strats.get(radiantStrategy);
			OpeningStrategy direOS = strats.get(direStrategy);

			if (radiantOS == null) {
				radiantOS = new OpeningStrategy(radiantStrategy);
			}
			if (direOS == null) {
				direOS = new OpeningStrategy(direStrategy);
			}

			// Update win/loss
			if (radiant[6].equals("Radiant")) {
				// radiant victory
				radiantOS.incrementWins(direStrategy, radiant[7]);
				direOS.incrementLosses(radiantStrategy, dire[7]);
			} else {
				// dire victory
				direOS.incrementWins(radiantStrategy, dire[7]);
				radiantOS.incrementLosses(direStrategy, radiant[7]);
			}

			// Update strats hashmap
			strats.put(radiantStrategy, radiantOS);
			strats.put(direStrategy, direOS);
		}
	}

	// Returns a String of the format "NtNbNmNj"
	public static String extractStrategy(String[] line) {
		String strategy = "";
		strategy += line[2] + "t" + line[3] + "b" + line[4] + "m" + line[5]
				+ "j";
		return strategy;
	}

	public static void writeResultsToCSV() {
		//Output general results
		try {
			FileWriter writer = new FileWriter(outputFile1);
			
			writer.append("Strategy,Tier,Wins,Losses,Winrate\n");
			
			for (OpeningStrategy os : strats.values()) {
				writer.append(os.toStringOverall());
				writer.append("\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Output matchup information
		try {
			FileWriter writer = new FileWriter(outputFile2);
			
			writer.append("Strategy,Opponent,Wins,Losses,Winrate\n");
			
			for (OpeningStrategy os : strats.values()) {
				writer.append(os.toStringIndividual());
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
