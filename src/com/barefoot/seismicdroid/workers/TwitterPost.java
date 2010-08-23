package com.barefoot.seismicdroid.workers;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class TwitterPost {
	
	private static final String LOG_TAG = "TwitterUpdater";
	private String username;
	private String password;
	private boolean isReady;
	private Context context;
	
	public TwitterPost(Context context) {
		this.context = context;
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.username = defaultSharedPreferences.getString("twitter_username", null);
		this.password = defaultSharedPreferences.getString("twitter_password", null);
		this.isReady = (this.username != null && this.password != null);
	}
	
	public boolean postMessage(String message) {
		if(!isReady) {
			showMissingCredentialsNotification();
			return true;
		}
		
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

	private void showMissingCredentialsNotification() {
		Log.i(LOG_TAG, "Showing twitter credentials missing notification");
		Toast updateNotification = Toast.makeText(context, "Twitter Credentials are not set in preferences!", Toast.LENGTH_SHORT);
		updateNotification.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
		updateNotification.show();
	}
}
