package com.hackthenorth.snaplocation.model;

public class Friend {
	private String unique_name;
	private String display_name;
	private int num_rounds_pending;
	
	public String getUniqueName() {
		return unique_name;
	}
	public String getDisplayName() {
		return display_name + "(" + num_rounds_pending + ")";
	}
}
