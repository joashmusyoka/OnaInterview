package com.ona.interview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
	static HashMap<String, Community> communities;

	public static void main(String[] args) {
		JsonProcessor jp = new JsonProcessor();
		ResultObject rs = jp.calculate();

		System.out.println("Total Functional: " + rs.getFunctional());
		System.out.println("Total Non Functional: " + rs.getNonFunctional());
		HashMap<String, Relation> relations = rs.getRelations();

		Iterator<Relation> it = relations.values().iterator();
		while(it.hasNext()){
			Relation relation = it.next();
			relation.printSelf(rs.getFunctional()+rs.getNonFunctional());
		}
		
		communities = rs.getCommunities();

		Iterator<Community> iterator = communities.values().iterator();
		ArrayList<Double> percentages = new ArrayList<Double>();

		while (iterator.hasNext()) {
			Community community = iterator.next();

			double percentNonFunctional = (double) community
					.getTotalNonFunctional()
					/ (community.getTotalNonFunctional() + community
							.getTotalFunctional()) * 100;

			community.setPercentNonFunctional(percentNonFunctional);
			percentages.add(community.getPercentNonFunctional());
		}

		rankCommunities(percentages);
	}

	private static void rankCommunities(ArrayList<Double> percentages) {
		Collections.sort(percentages, Collections.reverseOrder());

		for (int i = 0; i < percentages.size(); i++) {
			int k = i + 1;
			double percentage = percentages.get(i);

			if (k < percentages.size()) {
				double next = percentages.get(k);
				if (percentage == next) {
					// we have a duplicate, remove it.
					percentages.remove(i);
					// percentages.size() is one less, index should be
					// decrement;
					i--;
					k = i + 1;
				}
			}
		}

		Iterator<Community> it = communities.values().iterator();

		while (it.hasNext()) {
			Community community = it.next();
			// System.out.println("Searching for: "+community.getPercentNonFunctional());
			// int rank = Collections.binarySearch(percentages,
			// community.getPercentNonFunctional());
			int rank = Collections.binarySearch(percentages,
					community.getPercentNonFunctional(),
					Collections.reverseOrder());
			community.setRank(rank);

			System.out.println("--------" + community.getName() + "--------");
			System.out.println("Functional: " + community.getTotalFunctional());
			System.out.println("Non-Functional: "
					+ community.getTotalNonFunctional());
			System.out.println("Percent Non-Functional: "
					+ community.getPercentNonFunctional() + "%");
			System.out.println("Rank: " + community.getRank());
			System.out.println("------------------------\n");
		}
	}
}
