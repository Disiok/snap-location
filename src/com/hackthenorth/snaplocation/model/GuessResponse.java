package com.hackthenorth.snaplocation.model;

import com.google.android.gms.maps.model.LatLng;

public class GuessResponse {
	private String distance;
	private String score;
	private String correct_lat;
	private String correct_lon;
	private String result;
	private String next_url;
	
	public LatLng getCoordinates() {
		return new LatLng(Double.parseDouble(correct_lat), Double.parseDouble(correct_lon));
	}
}
