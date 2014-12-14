package com.ona.interview;

import java.util.HashMap;

public class ResultObject {
	protected int functional;
	protected int nonFunctional;
	protected HashMap<String, Community> communities;
	protected HashMap<String, Relation> relations;
	public int getFunctional() {
		return functional;
	}
	public void setFunctional(int functional) {
		this.functional = functional;
	}
	public int getNonFunctional() {
		return nonFunctional;
	}
	public void setNonFunctional(int nonFunctional) {
		this.nonFunctional = nonFunctional;
	}
	public HashMap<String, Community> getCommunities() {
		return communities;
	}
	public void setCommunities(HashMap<String, Community> communities) {
		this.communities = communities;
	}
	public HashMap<String, Relation> getRelations() {
		return relations;
	}
	public void setRelations(HashMap<String, Relation> relations) {
		this.relations = relations;
	}
}
