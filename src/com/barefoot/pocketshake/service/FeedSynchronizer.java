package com.barefoot.pocketshake.service;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.barefoot.pocketshake.R;
import com.barefoot.pocketshake.data.EarthQuake;
import com.barefoot.pocketshake.filters.FilterStore;
import com.barefoot.pocketshake.storage.EarthQuakeDatabase;
import com.barefoot.pocketshake.workers.QuakeFeedParser;

public class FeedSynchronizer extends SchedulableService {

	public static final String BROADCAST_ACTION = "com.barefoot.pocketshake.service.FeedSynchronizer.broadcast";
	private static final String LOG_TAG = "FeedSynchronizerService";
	private HttpClient client;
	private String feedUrl;
	private final Binder binder = new LocalBinder();
	private Intent broadcast=new Intent(BROADCAST_ACTION);
	private ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();
	private QuakeFeedParser parser;
	private EarthQuakeDatabase db;
	private NotificationCreator notificationCreator;
	private FilterStore filterStore;
	
	private static boolean running = false;

	public FeedSynchronizer(String name) {
		super(name);
		notificationCreator = new NotificationCreator(this, 0);
		filterStore = new FilterStore(this);
	}
	
	public FeedSynchronizer() {
		this("FeedSynchronizer");
	}
	
	@Override
	public void onCreate() {
		Log.i(LOG_TAG,"Creating instance and opening earthquake database");
		super.onCreate();
		client = new DefaultHttpClient();
		feedUrl = getString(R.string.feed_url);
		db = new EarthQuakeDatabase(this);
	}
		
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(LOG_TAG,"Destroying instance and closing earthquake database");
		db.close();
		client.getConnectionManager().shutdown();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class LocalBinder extends Binder {
		public FeedSynchronizer getService() {
			return (FeedSynchronizer.this);
		}
	}
	
	protected ArrayList<EarthQuake> generateQuakes(InputStream content) {
		parser = new QuakeFeedParser(content);
		return parser.asParsedObject();
	}

	@Override
	public void doServiceTask(Intent intent) {
		if(!running) {
			running = true;
			Log.i(LOG_TAG,"Executing Service Task");
			HttpGet getMethod = new HttpGet(feedUrl);
			HttpResponse response = null;
			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			String purge_day = sharedPref.getString("purge_day", "30");
			boolean notifications = sharedPref.getBoolean("show_notifications", true);
			
			try {
				response = client.execute(getMethod);
				HttpEntity entity = response.getEntity();
				ArrayList<EarthQuake> generateQuakes = generateQuakes(entity.getContent());
				Log.i(LOG_TAG,"Parsing of XML done. Obtained "+generateQuakes.size()+" earthquake records");
				synchronized(earthQuakes) {
					earthQuakes = generateQuakes;
				}
				
				Log.i(LOG_TAG,"Saving enteries to database");
				//save entries to database
				EarthQuake[] newQuakes = db.saveNewEarthquakesOnly(earthQuakes.toArray(new EarthQuake[earthQuakes.size()]));
				db.deleteRecordsOlderThanDays(purge_day);
				
				if(notifications)
					generateNotifications(newQuakes);
				
				sendBroadcast(broadcast);
			} catch (Throwable t) {
				Log.e("FetchingNSaving Earthquakes", t.getMessage());
			}
			finally {
				running = false;
			}
		}
	}

	private void generateNotifications(EarthQuake[] newQuakes) {
		String message = null;
		EarthQuake eachQauake = null;
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean vibrate = sharedPref.getBoolean("vibrate_phone", true);
		for(int i = newQuakes.length-1; i >= 0; i--)
		{
			eachQauake = newQuakes[i];
			if(!filterStore.pass(eachQauake))
				continue;
			message = eachQauake.getIntensity() + " Richter earthquake at " + eachQauake.getLocation();
			notificationCreator.createNotification(938464326, "Quake Warning!", eachQauake.getLocation(), message, eachQauake.getTimeInLong(), vibrate);
		}
	}
}
