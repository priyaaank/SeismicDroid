package com.barefoot.pocketshake.data;

public class EarthQuake {
	
	private String id;
	private String latitude;
	private String longitude;
	private String location;
	private String intensity;
	private String date;
	private String time;
	
	public EarthQuake(String id, String title, String cordinates, String dateTime) {
		this.id = id;
		
		String[] intensityAndLocation = title.split(", ");
		this.location = intensityAndLocation[1].trim();
		this.intensity = intensityAndLocation[0].trim();
		this.intensity = intensity.substring(1).trim();
		
		String[] dateNTime = dateTime.split("T");
		this.date = dateNTime[0].trim();
		this.time = dateNTime[1].trim();
		this.time = time.replace("Z", "").trim();
	}

	public String getId() {
		return id;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getIntensity() {
		return intensity;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getTime() {
		return time;
	}
}

