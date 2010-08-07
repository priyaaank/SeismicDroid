package com.barefoot.seismicdroid.data;

import android.text.format.Time;
import android.util.Log;

import com.barefoot.seismicdroid.exceptions.InvalidFeedException;
import com.barefoot.seismicdroid.utils.StringUtils;

public class EarthQuake {
	
	private int id;
	private String identifier;
	private String latitude;
	private String longitude;
	private String location;
	private String intensity;
	private String date;
	private String detailLink;
	
	private static String localtimezone = new Time().timezone;
	
	public EarthQuake(int id, String identifier, String title, String cordinates, String dateTime, String detailLink) 
	throws InvalidFeedException {
		
		this.id = id;
		this.identifier = identifier;
		
		String[] intensityAndLocation = title.split(", ");
		this.location = intensityAndLocation[1].trim();
		this.intensity = intensityAndLocation[0].trim();
		this.intensity = intensity.substring(1).trim();
		
		String[] latitudeNLongitude = cordinates.split(" ");
		this.latitude = latitudeNLongitude[0].trim();
		this.longitude = latitudeNLongitude[1].trim();
		
		this.date = dateTime;
		
		this.detailLink = detailLink;
	}

	public int getId() {
		return this.id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getLocation() {
		return StringUtils.camelCase(location);
	}
	
	public String getIntensity() {
		return intensity;
	}
	
	public String getDate() {
		return date;
	}

	public int getMicroLatitudes() {
		if(null != this.getLatitude()) {
			Double value = Double.parseDouble(this.getLatitude()) * 10E5;
			return value.intValue();
		}
		return 0;
	}
	
	public int getMicroLongitudes() {
		if(null != this.getLongitude()) {
			Double value = Double.parseDouble(this.getLongitude()) * 10E5;
			return value.intValue();
		}
		return 0;
	}
	
	public String getLocalTime() {
		Time newTime = new Time("UTC");
		newTime.parse3339(getDate());
		newTime.switchTimezone(localtimezone);
		return  newTime.format("%d-%b-%Y %H:%M:%S");
	}
	
	public long getTimeInLong() {
		Time newTime = new Time("UTC");
		newTime.parse3339(getDate());
		newTime.switchTimezone(localtimezone);
		return newTime.toMillis(false);
	}
	
	@Override
	public String toString() {
		StringBuffer toReturn = new StringBuffer("");
		toReturn.append(this.intensity);
		toReturn.append("::");		
		toReturn.append(this.location);
		toReturn.append("::");
		toReturn.append(this.longitude);
		toReturn.append("::");
		toReturn.append(this.latitude);
		Log.v("EarthQuake String representation :: ",toReturn.toString());
		return toReturn.toString();
	}

	public String getDetailLink() {
		return this.detailLink;
	}

	public String getKeywords() {
		return ("earthquake in "+location).replace(" ", "+");
	}
	
}

