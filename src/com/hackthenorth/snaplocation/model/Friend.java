package com.hackthenorth.snaplocation.model;

public class Friend {
	private String unique_name;
	private String display_name;
	private int score;
	private int num_rounds_pending;
	private boolean selected = false;
	
	public String getUniqueName() {
		return unique_name;
	}
	public String getDisplayName() {
		return display_name;
	}
	public int getNumberOfRoundsPending() {
		return num_rounds_pending;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isSelected() {
		return selected;
	}
}
