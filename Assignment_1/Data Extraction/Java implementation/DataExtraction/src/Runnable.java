package datavis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runnable {

	public static String dataTestFileName = "master-zones-1-match.csv";
	public static String fullDataFileName = "master-zones.csv";

	public static int maxTime = 900;

	public static String exportFileName = "15minuteOpeningStrat.csv";

	public static void main(String[] args) {
		Runnable run = new Runnable();
		run.readFile(fullDataFileName);

	}

	public int matchCountFile(String fileName) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String currentMatch = "";
		int matchCount = 0;
		try {

			br = new BufferedReader(new FileReader(fileName));
			br.readLine();
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("[^a-zA-Z,12345678910]", "");
				// use comma as separator
				String[] splitLine = line.split(cvsSplitBy);
				if (!splitLine[3].equals(currentMatch)) {
					matchCount++;
					currentMatch = splitLine[3];
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return matchCount;
	}

	public String[][] createOutput(int matchCount) {
		String[][] rtn = new String[matchCount * 2 + 1][8];
		rtn[0][0] = "matchid";
		rtn[0][1] = "team";
		rtn[0][2] = "toplane";
		rtn[0][3] = "bottomlane";
		rtn[0][4] = "midlane";
		rtn[0][5] = "jungle";
		rtn[0][6] = "winner";
		rtn[0][7] = "tier";
		return rtn;
	}

	public String[][] setBasisOutputLastReadMatch(String[][] input,
			String currentMatch, String currentMatchWinner, String currentTier) {
		input[0][0] = currentMatch;
		input[0][1] = "Radiant";
		input[0][2] = "" + 0;
		input[0][3] = "" + 0;
		input[0][4] = "" + 0;
		input[0][5] = "" + 0;
		input[0][6] = currentMatchWinner;
		input[0][7] = currentTier;

		input[1][0] = currentMatch;
		input[1][1] = "Dire";
		input[1][2] = "" + 0;
		input[1][3] = "" + 0;
		input[1][4] = "" + 0;
		input[1][5] = "" + 0;
		input[1][6] = currentMatchWinner;
		input[1][7] = currentTier;
		return input;
	}

	public void readFile(String fileName) {
		// int matchCount = matchCountFile(fileName);
		int matchCount = 196;
		// Declare output arrays
		String[][] output = createOutput(matchCount);
		String[][] outputLastReadMatch = new String[2][8];

		// Declare variables needed for processing of file
		String currentMatch;
		String currentPlayer;
		String currentTeam;
		String currentTier;
		String currentMatchWinner;
		int midlaneTimes = 0;
		int jungleTimes = 0;
		int toplaneTimes = 0;
		int bottomlaneTimes = 0;
		int voidTimes = 0;
		int riverTimes = 0;
		int laneshopTimes = 0;
		int secretshopTimes = 0;
		int baseTimes = 0;
		int pitTimes = 0;
		int otherTimes = 0;
		int outputRuleNumber = 1;

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {

			br = new BufferedReader(new FileReader(fileName));
			String firstLine = br.readLine();
			String line2 = br.readLine()
					.replaceAll("[^a-zA-Z,12345678910]", "");
			String[] secondLine = line2.split(cvsSplitBy);
			currentMatch = secondLine[3];
			currentTeam = secondLine[4];
			currentPlayer = secondLine[5];
			currentTier = secondLine[10];
			currentMatchWinner = "";
			if (Integer.parseInt(secondLine[6]) == 1
					&& secondLine[4].equals("radiant")) {
				currentMatchWinner = "Radiant";
			} else if (Integer.parseInt(secondLine[6]) == 0
					&& secondLine[4].equals("dire")) {
				currentMatchWinner = "Radiant";
			} else {
				currentMatchWinner = "Dire";
			}
			outputLastReadMatch = setBasisOutputLastReadMatch(
					outputLastReadMatch, currentMatch, currentMatchWinner,
					currentTier);
			int i = 0;
			while ((line = br.readLine()) != null) {
				// use comma as separator
				line = line.replaceAll("[^a-zA-Z,12345678910]", "");
				String[] splitLine = line.split(cvsSplitBy);

				if (currentMatch.equals(splitLine[3])) {
					if (!currentPlayer.equals(splitLine[5])) {
						int totalTime = midlaneTimes + jungleTimes
								+ toplaneTimes + bottomlaneTimes + voidTimes
								+ riverTimes + laneshopTimes + secretshopTimes
								+ baseTimes + pitTimes + otherTimes;
						if (currentTeam.equals("radiant")) {
							if (midlaneTimes >= jungleTimes
									&& midlaneTimes >= toplaneTimes
									&& midlaneTimes >= bottomlaneTimes) {
								outputLastReadMatch[0][4] = Integer
										.parseInt(outputLastReadMatch[0][4])
										+ 1 + "";
							} else if (jungleTimes >= midlaneTimes
									&& jungleTimes >= toplaneTimes
									&& jungleTimes >= bottomlaneTimes) {
								outputLastReadMatch[0][5] = Integer
										.parseInt(outputLastReadMatch[0][5])
										+ 1 + "";
							} else if (bottomlaneTimes >= toplaneTimes
									&& bottomlaneTimes >= midlaneTimes
									&& bottomlaneTimes >= jungleTimes) {
								outputLastReadMatch[0][3] = Integer
										.parseInt(outputLastReadMatch[0][3])
										+ 1 + "";
							} else {
								outputLastReadMatch[0][2] = Integer
										.parseInt(outputLastReadMatch[0][2])
										+ 1 + "";
							}
						} else {
							if (midlaneTimes >= jungleTimes
									&& midlaneTimes >= toplaneTimes
									&& midlaneTimes >= bottomlaneTimes) {
								outputLastReadMatch[1][4] = Integer
										.parseInt(outputLastReadMatch[1][4])
										+ 1 + "";
							} else if (jungleTimes >= midlaneTimes
									&& jungleTimes >= toplaneTimes
									&& jungleTimes >= bottomlaneTimes) {
								outputLastReadMatch[1][5] = Integer
										.parseInt(outputLastReadMatch[1][5])
										+ 1 + "";
							} else if (bottomlaneTimes >= toplaneTimes
									&& bottomlaneTimes >= midlaneTimes
									&& bottomlaneTimes >= jungleTimes) {
								outputLastReadMatch[1][3] = Integer
										.parseInt(outputLastReadMatch[1][3])
										+ 1 + "";
							} else {
								outputLastReadMatch[1][2] = Integer
										.parseInt(outputLastReadMatch[1][2])
										+ 1 + "";
							}
						}
						currentPlayer = splitLine[5];
						currentTeam = splitLine[4];
						midlaneTimes = 0;
						jungleTimes = 0;
						toplaneTimes = 0;
						bottomlaneTimes = 0;
						voidTimes = 0;
						riverTimes = 0;
						laneshopTimes = 0;
						secretshopTimes = 0;
						baseTimes = 0;
						pitTimes = 0;
						otherTimes = 0;
					}
				} else {
					int totalTime = midlaneTimes + jungleTimes + toplaneTimes
							+ bottomlaneTimes + voidTimes + riverTimes
							+ laneshopTimes + secretshopTimes + baseTimes
							+ pitTimes + otherTimes;
					if (currentTeam.equals("radiant")) {
						if (midlaneTimes >= jungleTimes
								&& midlaneTimes >= toplaneTimes
								&& midlaneTimes >= bottomlaneTimes) {
							outputLastReadMatch[0][4] = Integer
									.parseInt(outputLastReadMatch[0][4])
									+ 1
									+ "";
						} else if (jungleTimes >= midlaneTimes
								&& jungleTimes >= toplaneTimes
								&& jungleTimes >= bottomlaneTimes) {
							outputLastReadMatch[0][5] = Integer
									.parseInt(outputLastReadMatch[0][5])
									+ 1
									+ "";
						} else if (bottomlaneTimes >= toplaneTimes
								&& bottomlaneTimes >= midlaneTimes
								&& bottomlaneTimes >= jungleTimes) {
							outputLastReadMatch[0][3] = Integer
									.parseInt(outputLastReadMatch[0][3])
									+ 1
									+ "";
						} else {
							outputLastReadMatch[0][2] = Integer
									.parseInt(outputLastReadMatch[0][2])
									+ 1
									+ "";
						}
					} else {
						if (midlaneTimes >= jungleTimes
								&& midlaneTimes >= toplaneTimes
								&& midlaneTimes >= bottomlaneTimes) {
							outputLastReadMatch[1][4] = Integer
									.parseInt(outputLastReadMatch[1][4])
									+ 1
									+ "";
						} else if (jungleTimes >= midlaneTimes
								&& jungleTimes >= toplaneTimes
								&& jungleTimes >= bottomlaneTimes) {
							outputLastReadMatch[1][5] = Integer
									.parseInt(outputLastReadMatch[1][5])
									+ 1
									+ "";
						} else if (bottomlaneTimes >= toplaneTimes
								&& bottomlaneTimes >= midlaneTimes
								&& bottomlaneTimes >= jungleTimes) {
							outputLastReadMatch[1][3] = Integer
									.parseInt(outputLastReadMatch[1][3])
									+ 1
									+ "";
						} else {
							outputLastReadMatch[1][2] = Integer
									.parseInt(outputLastReadMatch[1][2])
									+ 1
									+ "";
						}
					}
					currentPlayer = splitLine[5];
					currentTeam = splitLine[4];
					midlaneTimes = 0;
					jungleTimes = 0;
					toplaneTimes = 0;
					bottomlaneTimes = 0;
					voidTimes = 0;
					riverTimes = 0;
					laneshopTimes = 0;
					secretshopTimes = 0;
					baseTimes = 0;
					pitTimes = 0;
					otherTimes = 0;

					// Set output values
					output[outputRuleNumber][0] = outputLastReadMatch[0][0];
					output[outputRuleNumber][1] = outputLastReadMatch[0][1];
					output[outputRuleNumber][2] = outputLastReadMatch[0][2];
					output[outputRuleNumber][3] = outputLastReadMatch[0][3];
					output[outputRuleNumber][4] = outputLastReadMatch[0][4];
					output[outputRuleNumber][5] = outputLastReadMatch[0][5];
					output[outputRuleNumber][6] = outputLastReadMatch[0][6];
					output[outputRuleNumber][7] = outputLastReadMatch[0][7];
					outputRuleNumber++;
					output[outputRuleNumber][0] = outputLastReadMatch[1][0];
					output[outputRuleNumber][1] = outputLastReadMatch[1][1];
					output[outputRuleNumber][2] = outputLastReadMatch[1][2];
					output[outputRuleNumber][3] = outputLastReadMatch[1][3];
					output[outputRuleNumber][4] = outputLastReadMatch[1][4];
					output[outputRuleNumber][5] = outputLastReadMatch[1][5];
					output[outputRuleNumber][6] = outputLastReadMatch[1][6];
					output[outputRuleNumber][7] = outputLastReadMatch[1][7];
					outputRuleNumber++;
					i++;
					System.out
							.println(((double) i / (double) matchCount) * 100);
					// Reset values
					currentMatch = splitLine[3];
					currentPlayer = splitLine[5];
					currentTeam = splitLine[4];
					currentTier = splitLine[10];
					if (Integer.parseInt(splitLine[6]) == 1
							&& secondLine[4].equals("radiant")) {
						currentMatchWinner = "Radiant";
					} else if (Integer.parseInt(splitLine[6]) == 0
							&& secondLine[4].equals("dire")) {
						currentMatchWinner = "Radiant";
					} else {
						currentMatchWinner = "Dire";
					}

					outputLastReadMatch = setBasisOutputLastReadMatch(
							outputLastReadMatch, currentMatch,
							currentMatchWinner, currentTier);
				}
				if (Integer.parseInt(splitLine[8]) < maxTime) {
					String loc = splitLine[11];
					if (loc.equals("midlaneradiant")
							|| loc.equals("midlanedire")) {
						midlaneTimes++;
					} else if (loc.equals("jungleradiant")
							|| loc.equals("jungledire")) {
						jungleTimes++;
					} else if (loc.equals("toplaneradiant")
							|| loc.equals("toplanedire")) {
						toplaneTimes++;
					} else if (loc.equals("bottomlaneradiant")
							|| loc.equals("bottomlanedire")) {
						bottomlaneTimes++;
					} else if (loc.equals("voidradiant")
							|| loc.equals("voiddire")) {
						voidTimes++;
					} else if (loc.equals("river")) {
						riverTimes++;
					} else if (loc.equals("base1radiant")
							|| loc.equals("base2dire")) {
						baseTimes++;
					} else if (loc.equals("laneshopradiant")
							|| loc.equals("laneshopdire")) {
						laneshopTimes++;
					} else if (loc.equals("secretshopradiant")
							|| loc.equals("secretshopdire")) {
						secretshopTimes++;
					} else if (loc.equals("pitdire")) {
						pitTimes++;
					} else {
						otherTimes++;
					}
				}
			}
			// calculate values for last match
			int totalTime = midlaneTimes + jungleTimes + toplaneTimes
					+ bottomlaneTimes + voidTimes + riverTimes + laneshopTimes
					+ secretshopTimes + baseTimes + pitTimes + otherTimes;
			if (currentTeam.equals("radiant")) {
				if (midlaneTimes >= jungleTimes && midlaneTimes >= toplaneTimes
						&& midlaneTimes >= bottomlaneTimes) {
					outputLastReadMatch[0][4] = Integer
							.parseInt(outputLastReadMatch[0][4]) + 1 + "";
				} else if (jungleTimes >= midlaneTimes
						&& jungleTimes >= toplaneTimes
						&& jungleTimes >= bottomlaneTimes) {
					outputLastReadMatch[0][5] = Integer
							.parseInt(outputLastReadMatch[0][5]) + 1 + "";
				} else if (bottomlaneTimes >= toplaneTimes
						&& bottomlaneTimes >= midlaneTimes
						&& bottomlaneTimes >= jungleTimes
						&& bottomlaneTimes >= midlaneTimes) {
					outputLastReadMatch[0][3] = Integer
							.parseInt(outputLastReadMatch[0][3]) + 1 + "";
				} else {
					outputLastReadMatch[0][2] = Integer
							.parseInt(outputLastReadMatch[0][2]) + 1 + "";
				}
			} else {
				if (midlaneTimes >= jungleTimes && midlaneTimes >= toplaneTimes
						&& midlaneTimes >= bottomlaneTimes) {
					outputLastReadMatch[1][4] = Integer
							.parseInt(outputLastReadMatch[1][4]) + 1 + "";
				} else if (jungleTimes >= midlaneTimes
						&& jungleTimes >= toplaneTimes
						&& jungleTimes >= bottomlaneTimes) {
					outputLastReadMatch[1][5] = Integer
							.parseInt(outputLastReadMatch[1][5]) + 1 + "";
				} else if (bottomlaneTimes >= toplaneTimes
						&& bottomlaneTimes >= midlaneTimes
						&& bottomlaneTimes >= jungleTimes
						&& bottomlaneTimes >= midlaneTimes) {
					outputLastReadMatch[1][3] = Integer
							.parseInt(outputLastReadMatch[1][3]) + 1 + "";
				} else {
					outputLastReadMatch[1][2] = Integer
							.parseInt(outputLastReadMatch[1][2]) + 1 + "";
				}
			}

			// Set output values
			output[outputRuleNumber][0] = outputLastReadMatch[0][0];
			output[outputRuleNumber][1] = outputLastReadMatch[0][1];
			output[outputRuleNumber][2] = outputLastReadMatch[0][2];
			output[outputRuleNumber][3] = outputLastReadMatch[0][3];
			output[outputRuleNumber][4] = outputLastReadMatch[0][4];
			output[outputRuleNumber][5] = outputLastReadMatch[0][5];
			output[outputRuleNumber][6] = outputLastReadMatch[0][6];
			output[outputRuleNumber][7] = outputLastReadMatch[0][7];
			outputRuleNumber++;
			output[outputRuleNumber][0] = outputLastReadMatch[1][0];
			output[outputRuleNumber][1] = outputLastReadMatch[1][1];
			output[outputRuleNumber][2] = outputLastReadMatch[1][2];
			output[outputRuleNumber][3] = outputLastReadMatch[1][3];
			output[outputRuleNumber][4] = outputLastReadMatch[1][4];
			output[outputRuleNumber][5] = outputLastReadMatch[1][5];
			output[outputRuleNumber][6] = outputLastReadMatch[1][6];
			output[outputRuleNumber][7] = outputLastReadMatch[1][7];
			outputRuleNumber++;

			// Export to csv
			exportToCSV(output);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

	public void exportToCSV(String[][] output) {
		String exportString = "";
		for (int i = 0; i < output.length; i++) {
			for (int k = 0; k < output[i].length; k++) {
				if (k == output[i].length - 1) {
					exportString += output[i][k];
				} else {
					exportString += output[i][k] + ",";
				}
			}
			exportString += "\n";
		}
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(
					exportFileName));
			br.write(exportString);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
