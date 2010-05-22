package com.barefoot.pocketshake.storage;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.barefoot.pocketshake.data.EarthQuake;
import com.barefoot.pocketshake.storage.EarthQuakeDatabase.EarthquakeCursor;

public class EarthQuakeDataWrapper {
	
	private EarthQuakeDatabase db;
	final private static String LOG_TAG = "EarthQuakeDataWrapper";
	private static ArrayList<EarthQuake> cachedEarthQuakeFeed = new ArrayList<EarthQuake>();
	
	public EarthQuakeDataWrapper(Context context) {
		db = new EarthQuakeDatabase(context);
	}
	
	public synchronized void refreshFeedCache(boolean force) {
		Log.i(LOG_TAG, "Refreshing the cache feed from db. The force flag value is ["+force+"] and the size of cache elements is :: " + cachedEarthQuakeFeed.size());
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
	
	public ArrayList<EarthQuake> getEarthQuakes() {
		Log.i(LOG_TAG, "Returning arraylist containing earthquake details");
		return cachedEarthQuakeFeed;
	}
	
	 public String getStringRepresentationForQuakeAtPosition(int index) {
		 if (index <= cachedEarthQuakeFeed.size()) {
			 return cachedEarthQuakeFeed.get(index).toString();
		 }
		 return null;
	 }
	
	public synchronized void clearCache() {
		Log.i(LOG_TAG, "Clearing the earthquake cache!!");
		cachedEarthQuakeFeed.clear();
	}
	
	protected void finalize() throws Throwable {
	    try {
	    	Log.i(LOG_TAG, "Closing Database connection");
	        db.close();        // close database
	    } finally {
	        super.finalize();
	    }
	}
}


