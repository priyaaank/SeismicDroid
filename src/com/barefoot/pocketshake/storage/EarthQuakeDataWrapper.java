package com.barefoot.pocketshake.storage;

import java.util.ArrayList;

import android.content.Context;

import com.barefoot.pocketshake.data.EarthQuake;
import com.barefoot.pocketshake.storage.EarthQuakeDatabase.EarthquakeCursor;

public class EarthQuakeDataWrapper {
	
	private EarthQuakeDatabase db;
	private static ArrayList<EarthQuake> cachedEarthQuakeFeed = new ArrayList<EarthQuake>();
	
	public EarthQuakeDataWrapper(Context context) {
		db = new EarthQuakeDatabase(context);
	}
	
	public synchronized void refreshFeedCache(boolean force) {
		if(cachedEarthQuakeFeed.size() == 0 || force) {
			EarthquakeCursor allEarthquakes = db.getEarthquakes();
			try {
				if(allEarthquakes != null && allEarthquakes.moveToFirst()) {
					cachedEarthQuakeFeed.clear();
					do {
						cachedEarthQuakeFeed.add(allEarthquakes.getEarthQuake());
					} while(allEarthquakes.moveToNext());
				}
			} finally {
				allEarthquakes.close();
			}
		}
	}

	public String[] getStringRepresentationForQuakes() {
		ArrayList<String> stringRepresntation = new ArrayList<String>(cachedEarthQuakeFeed.size());
		for(EarthQuake eachEarthquake : cachedEarthQuakeFeed) {
			stringRepresntation.add(eachEarthquake.toString());
		}
	
		return stringRepresntation.toArray(new String[stringRepresntation.size()]);
	}
	
	public String getStringRepresentationForQuakeAtPosition(int index) {
		if (index <= cachedEarthQuakeFeed.size()) {
			return cachedEarthQuakeFeed.get(index).toString();
		}
		return null;
	}
	
	public synchronized void clearCache() {
		cachedEarthQuakeFeed.clear();
	}
	
	protected void finalize() throws Throwable {
	    try {
	        db.close();        // close database
	    } finally {
	        super.finalize();
	    }
	}
}


