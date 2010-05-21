package com.barefoot.pocketshake.service;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.barefoot.pocketshake.R;
import com.barefoot.pocketshake.data.EarthQuake;
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

	public FeedSynchronizer(String name) {
		super(name);
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
		Log.i(LOG_TAG,"Executing Service Task");
		HttpGet getMethod = new HttpGet(feedUrl);
		HttpResponse response = null;

		try {
			response = client.execute(getMethod);
			HttpEntity entity = response.getEntity();
			ArrayList<EarthQuake> generateQuakes = generateQuakes(entity.getContent());
			Log.i(LOG_TAG,"Parsing of XML done. Obtained "+generateQuakes.size()+" earthquake records");
			synchronized(this) {
				earthQuakes = generateQuakes;
			}
			
			Log.i(LOG_TAG,"Saving enteries to database");
			//save entries to database
			db.saveNewEarthquakesOnly(earthQuakes.toArray(new EarthQuake[earthQuakes.size()]));
			sendBroadcast(broadcast);
		} catch (Throwable t) {
			Log.e("FetchingNSaving Earthquakes", t.getMessage());
		}
	}
}
