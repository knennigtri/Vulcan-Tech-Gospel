package com.nennig.vtglibrary.managers;

/**
 * These are the values that would be inserted for StatType
 */
public enum StatType {
	 DETAILS_PAGE("details.page")
	,VIDEOS_PAGE("videos.page")
	;
	
	private String strValue;
	
	StatType(String strValue) {
		this.strValue = strValue;
	}
	
	public String toString() {
		return this.strValue;
	}
}