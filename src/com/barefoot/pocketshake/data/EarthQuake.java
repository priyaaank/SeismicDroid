package com.barefoot.pocketshake.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.barefoot.pocketshake.exceptions.InvalidFeedException;

public class EarthQuake {
	
	private String id;
	private String latitude;
	private String longitude;
	private String location;
	private String intensity;
	private String date;
	
	private static String convertFrom = "yyyy-MM-dd HH:mm:ss";
	private static String convertTo = "dd-MM-yyyy HH:mm:ss";
	
	public EarthQuake(String id, String title, String cordinates, String dateTime) 
	throws InvalidFeedException {
		this.id = id;
		
		String[] intensityAndLocation = title.split(", ");
		this.location = intensityAndLocation[1].trim();
		this.intensity = intensityAndLocation[0].trim();
		this.intensity = intensity.substring(1).trim();
		
		String[] latitudeNLongitude = cordinates.split(" ");
		this.latitude = latitudeNLongitude[0].trim();
		this.longitude = latitudeNLongitude[1].trim();
		
		this.date = dateTime;
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

	public String getDisplayDate() {
		Date dateToConvert = null;
		String dateToReturn = this.date;
		SimpleDateFormat dateFormatter = new SimpleDateFormat(convertFrom);
		try {
			if(null != dateToReturn) {
				dateToReturn = dateToReturn.replace("T", " ");
				dateToReturn = dateToReturn.replace("Z", "");
				dateToConvert = dateFormatter.parse(dateToReturn);
				dateFormatter = new SimpleDateFormat(convertTo);
				dateToReturn = dateFormatter.format(dateToConvert);
				dateToReturn = dateToReturn + " UTC";
			}
		} catch (ParseException e) {
			Log.e("Tried Parsing date string.",e.getMessage());
		}
		return dateToReturn;
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
}

