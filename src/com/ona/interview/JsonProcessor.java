package com.ona.interview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonProcessor {
	protected int functional = 0;
	protected int nonFunctional = 0;
	protected Community currentCommunity;
	protected WaterPoint currentWaterPoint;
	protected HashMap<String, Community> communities = new HashMap<String, Community>();
	protected HashMap<String, Relation> relations = new HashMap<String, Relation>();

	public ResultObject calculate() {
		String remoteUrl = "https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json";
		//String fileUrl = "/home/musyoka/workspace/OnaInterview/src/com/ona/interview/water_points.json";
		JsonFactory factory = new JsonFactory();
		ResultObject result = new ResultObject();
		try {

			InputStream stream = new URL(remoteUrl).openStream();
			
			//JsonParser parser = factory.createJsonParser(new File(fileUrl));
			@SuppressWarnings("deprecation")
			JsonParser parser = factory.createJsonParser(stream);

			// loop until token equal to "]"
			while (parser.nextToken() != JsonToken.END_ARRAY) {
				currentCommunity = new Community();
				currentWaterPoint = new WaterPoint();
				BufferObject bo = new BufferObject();
				// loop through each object
				while (parser.nextToken() != JsonToken.END_OBJECT) {
					String fieldname = parser.getCurrentName();
					if ("communities_villages".equals(fieldname)) {
						// current token is "communities_villages",
						// move to next, which is "communities_villages"'s value
						parser.nextToken();
						String communityName = parser.getText();
						bo.communityName = communityName;

					}
					if ("water_functioning".equals(fieldname)) {

						parser.nextToken();
						String waterFunctional = parser.getText();
						bo.waterFunctional = waterFunctional;

					}
					if ("road_available".equals(fieldname)) {

						parser.nextToken();
						String roadAvailable = parser.getText();
						bo.roadAvailable = roadAvailable;

					}
					if ("water_pay".equals(fieldname)) {

						parser.nextToken();
						String waterPay = parser.getText();
						bo.waterPay = waterPay;

					}
					if ("water_used_season".equals(fieldname)) {

						parser.nextToken();
						String waterUsedSeason = parser.getText();
						bo.waterUsedSeason = waterUsedSeason;
					}
					if ("animal_point".equals(fieldname)) {

						parser.nextToken();
						String animalPoint = parser.getText();
						bo.animalPoint = animalPoint;
					}
				}
				processResponse(bo);
			}

		} catch (JsonParseException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		result.setFunctional(functional);
		result.setNonFunctional(nonFunctional);
		result.setRelations(relations);
		result.setCommunities(communities);

		return result;
	}

	private void processWaterPoint(BufferObject bo) {
		boolean waterFunctional = false;

		if ("yes".equalsIgnoreCase(bo.waterFunctional)) {
			functional++;
			waterFunctional = true;
		} else if ("no".equalsIgnoreCase(bo.waterFunctional)) {
			nonFunctional++;
			waterFunctional = false;
		} else {
			waterFunctional = false;
		}
		
		currentWaterPoint.setFunctional(waterFunctional);

		updateRelations(waterFunctional, "road_available", bo.roadAvailable,
				"Water Functioning dependency on Road Availability");
		updateRelations(waterFunctional, "water_pay", bo.waterPay,
				"Water Functioning dependency on Whether water is paid or free");
		updateRelations(waterFunctional, "animal_point", bo.animalPoint,
				"Water Functioning dependency on whether animal point or not");
	}

	/*
	 * @parameter: dependant - The parameter that depends on another parameter
	 * 
	 * @parameter: dependent - The parameter whose value controls the value(s)
	 * of another parameter
	 */
	private void updateRelations(boolean waterFunctional, String dependent,
			String dependentValue, String relationName) {

		Relation relation = relations.get(dependent);

		if (relation != null) {
			HashMap<String, Integer> parameterRelations = relation
					.getParameterRelations();
			Integer previousYesFrequency = parameterRelations
					.get(dependentValue);
			int newYesFrequency = 0;
			if (waterFunctional) {
				// we have a functional water point for dependentValue,
				if (previousYesFrequency != null) {
					// increment frequency
					newYesFrequency = previousYesFrequency + 1;

				} else {
					// This is a new dependentValue
					newYesFrequency = 1;
				}
				parameterRelations.put(dependentValue, newYesFrequency);
				relation.setParameterRelations(parameterRelations);
				relations.put(dependent, relation);
			} else {
				// we have a 'no' for this value; ignore
			}
		} else {
			relation = new Relation(relationName, dependent);
			HashMap<String, Integer> parameterRelations = new HashMap<String, Integer>();
			if (waterFunctional) {
				// we have a 'yes' for this value
				parameterRelations.put(dependentValue, 1);
				relation.setParameterRelations(parameterRelations);
				relations.put(dependent, relation);
			} else {
				// we have a 'no' for this value; ignore
			}
		}
	}

	private void processResponse(BufferObject bo) {
		processWaterPoint(bo);
		currentCommunity.setName(bo.communityName);
		if (!communities.containsKey(bo.communityName)) {
			// we've not met this community yet, lets add it for the 1st time
			ArrayList<WaterPoint> waterPoints = new ArrayList<WaterPoint>();
			waterPoints.add(currentWaterPoint);
			if (currentWaterPoint.isFunctional()) {
				currentCommunity.setTotalFunctional(currentCommunity
						.getTotalFunctional() + 1);
			} else {
				currentCommunity.setTotalNonFunctional(currentCommunity
						.getTotalNonFunctional() + 1);
			}
			currentCommunity.setWaterPoints(waterPoints);
			communities.put(bo.communityName, currentCommunity);

		} else {
			/*
			 * we've already met this community, let's update it 1. Get the
			 * community from the HashMap 2. Get its water points 3. update its
			 * water points - By adding current water point to it 4.
			 */
			currentCommunity = communities.get(bo.communityName);
			ArrayList<WaterPoint> commuityWaterPoints = currentCommunity
					.getWaterPoints();
			commuityWaterPoints.add(currentWaterPoint);
			if (currentWaterPoint.isFunctional()) {
				currentCommunity.setTotalFunctional(currentCommunity
						.getTotalFunctional() + 1);
			} else {
				currentCommunity.setTotalNonFunctional(currentCommunity
						.getTotalNonFunctional() + 1);
			}
			communities.put(bo.communityName, currentCommunity);
		}
	}

	class BufferObject {
		String communityName;
		String waterFunctional;
		String roadAvailable;
		String waterPay;
		String waterUsedSeason;
		String animalPoint;
	}

}
