package com.ona.interview;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/*
 * Class represents a relationship between various response parameters
 * @property parameterRelations = HashMap<k, v> where
 * k = parameter value
 * v = percentage functioning for k
 * */
public class Relation {
	HashMap<String, Integer> parameterRelations;
	String relationName;
	String dependent;
	public Relation(String name, String dependent){
		this.relationName = name;
		this.dependent = dependent;
	}
	public HashMap<String, Integer> getParameterRelations() {
		return parameterRelations;
	}

	public void setParameterRelations(HashMap<String, Integer> parameterRelations) {
		this.parameterRelations = parameterRelations;
	}
	public void printSelf(int total){
		System.out.println("--------------------------------------");
		System.out.println(relationName);
		System.out.println(dependent+"?\t# Functional\t% Functional");
		for (Map.Entry<String, Integer> en : parameterRelations.entrySet()) {
			double percent = (double) (en.getValue() / (double) (total)) * 100;
			
			DecimalFormat df = new DecimalFormat("#.##");
			percent = Double.valueOf(df.format(percent));
			
			System.out.println(en.getKey() + "\t\t" + en.getValue()
					+ "\t\t" + percent + "%");
		}
		System.out.println();
	}

	public String getRelationDescription() {
		return relationName;
	}

	public void setRelationDescription(String relationDescription) {
		this.relationName = relationDescription;
	}
	
}
