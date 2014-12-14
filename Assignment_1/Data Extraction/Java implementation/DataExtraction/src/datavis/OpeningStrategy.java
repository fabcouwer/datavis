package datavis; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OpeningStrategy {

	// Name and overall wins, losses and winrate
	private String name;
	private int totalWins;
	private int totalLosses;
	private double overallWinrate;

	// 3 hashmaps to keep track of wins, losses and winrate vs. every opponent
	private HashMap<String, Integer> wins;
	private HashMap<String, Integer> losses;
	private HashMap<String, Double> winrates;

	public OpeningStrategy(String name) {
		this.name = name;
		this.totalWins = 0;
		this.totalLosses = 0;
		this.overallWinrate = 0d;
		this.wins = new HashMap<String, Integer>();
		this.losses = new HashMap<String, Integer>();
		this.winrates = new HashMap<String, Double>();
	}

	// Increment win/loss counters
	public void incrementWins(String opponentName, String tier) {
		this.totalWins += 1;

		if (this.wins.get(opponentName) == null) {
			this.wins.put(opponentName, 0);
		}
		if (this.wins.get(tier) == null) {
			this.wins.put(tier, 0);
		}
		this.wins.put(opponentName, this.wins.get(opponentName) + 1);
		this.wins.put(tier, this.wins.get(tier) + 1);
	}

	public void incrementLosses(String opponentName, String tier) {
		this.totalLosses += 1;

		if (this.losses.get(opponentName) == null) {
			this.losses.put(opponentName, 0);
		}
		if (this.losses.get(tier) == null) {
			this.losses.put(tier, 0);
		}
		this.losses.put(opponentName, this.losses.get(opponentName) + 1);
		this.losses.put(tier, this.losses.get(tier) + 1);
	}

	// Update total winrate
	public void updateTotalWinrate() {
		this.overallWinrate = (double) this.totalWins
				/ (this.totalLosses + this.totalWins);
	}

	// Update all individual winrates
	public void updateWinrateHashmaps() {

		// Get names of all opponents
		Set<String> keys = new HashSet<String>(wins.keySet());
		keys.addAll(losses.keySet());

		ArrayList<String> opponents = new ArrayList<String>();
		opponents.addAll(keys);

		for (String opponent : opponents) {
			int w, l;

			if (wins.get(opponent) == null) {
				w = 0;
				wins.put(opponent, 0);
			} else {
				w = wins.get(opponent);
			}
			if (losses.get(opponent) == null) {
				l = 0;
				losses.put(opponent, 0);
			} else {
				l = losses.get(opponent);
			}
			winrates.put(opponent, (double) w / (w + l));
		}
	}

	// Returns a CSV-ready String with overall stats.
	// Example: "2t1m1b,3,3,0.5"
	public String toStringOverall() {
		String overall = this.name + ",Overall," + this.totalWins + ","
				+ this.totalLosses + "," + this.overallWinrate;
		for (Map.Entry<String, Double> entry : winrates.entrySet()) {
			String tier = entry.getKey();
			// Include data from the tiers
			if (tier.equals("Pro") || tier.equals("VeryHigh")
					|| tier.equals("High") || tier.equals("Normal")) {
				overall += "\n" + this.name + "," + tier + "," + wins.get(tier)
						+ "," + losses.get(tier) + "," + entry.getValue();
			}
		}
		return overall;
	}

	// Returns a CSV-ready String with individual statistics.
	// Example: "A,B,3,3,0.5\nA,C,4,1,0.8\n"
	public String toStringIndividual() {
		String s = "";
		for (Map.Entry<String, Double> entry : winrates.entrySet()) {
			String opponent = entry.getKey();
			// Do not include data from tiers
			if (!(opponent.equals("Pro") || opponent.equals("VeryHigh")
					|| opponent.equals("High") || opponent.equals("Normal"))) {
				s += this.name + "," + opponent + "," + wins.get(opponent)
						+ "," + losses.get(opponent) + "," + entry.getValue()
						+ "\n";
			}
		}
		return s;
	}

}
