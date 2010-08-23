package com.barefoot.seismicdroid.workers;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class TwitterPost {
	
	private static final String LOG_TAG = "TwitterUpdater";
	private String username;
	private String password;
	private boolean isReady;
	
	public TwitterPost(Context context) {
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.username = defaultSharedPreferences.getString("twitter_username", null);
		this.password = defaultSharedPreferences.getString("twitter_password", null);
		this.isReady = (this.username != null && this.username.trim().length() > 0 && this.password != null && this.password.trim().length() > 0);
	}
	
	public boolean postMessage(String message) {
        Twitter twitter = new TwitterFactory().getInstance(this.username,this.password);
        Status status = null;
        try {
			status = twitter.updateStatus(message);
			Log.i(LOG_TAG, "Successfully updated status to :: " + status.getText());
		} catch (TwitterException e) {
			Log.e(LOG_TAG, "Error occured :: " + e.getMessage());
		}
		
		return (status != null && status.getText() != null); 
	}
	
	public boolean isReady() {
		return this.isReady;
	}
}
