package com.ona.interview;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Community implements Comparable<Community> {
	protected String name;
	protected ArrayList<WaterPoint> waterPoints;
	protected int totalFunctional = 0;
	protected int totalNonFunctional = 0;
	protected double percentNonFunctional = 0.0;
	protected int rank = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<WaterPoint> getWaterPoints() {
		return waterPoints;
	}

	public void setWaterPoints(ArrayList<WaterPoint> waterPoints) {
		this.waterPoints = waterPoints;
	}

	public int getTotalFunctional() {
		return totalFunctional;
	}

	public void setTotalFunctional(int totalFunctional) {
		this.totalFunctional = totalFunctional;
	}

	public int getTotalNonFunctional() {
		return totalNonFunctional;
	}

	public void setTotalNonFunctional(int totalNonFunctional) {
		this.totalNonFunctional = totalNonFunctional;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getPercentNonFunctional() {
		return percentNonFunctional;
	}

	public void setPercentNonFunctional(double percentNonFunctional) {

		DecimalFormat df = new DecimalFormat("#.##");
		this.percentNonFunctional = Double.valueOf(df
				.format(percentNonFunctional));
	}

	@Override
	public int compareTo(Community other) {
		if (rank == other.getRank())
			return 0;
		if (rank < other.getRank())
			return -1;
		if (rank > other.getRank())
			return 1;
		return 0;
	}

}
