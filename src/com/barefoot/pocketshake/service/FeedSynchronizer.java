package com.barefoot.pocketshake.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.barefoot.pocketshake.R;

public class FeedSynchronizer extends Service {

	public static final String BROADCAST_ACTION = "com.barefoot.pocketshake.service.FeedSynchronizer";
	private HttpClient client;
	private String feedUrl;
	private final Binder binder = new LocalBinder();
	private String earthquakeFeed;
	private Intent broadcast=new Intent(BROADCAST_ACTION);
	private Timer timer = new Timer();

	@Override
	public void onCreate() {
		super.onCreate();

		client = new DefaultHttpClient();
		feedUrl = getString(R.string.feed_url);
	
		//startservice();		
		updateFeed();
	}
	
	
	private void startservice() {
		timer.scheduleAtFixedRate( new TimerTask() {
			public void run() {
				updateFeed();
			}
		}, 0, 60000L);
	}
	
	private void stopservice() {
		if (timer != null){
			timer.cancel();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		if(timer != null) {
//			stopservice();
//		}
		client.getConnectionManager().shutdown();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	synchronized public String getEarthquakeData() {
		return (earthquakeFeed);
	}

	private void updateFeed() {
		new FetchFeedTask().execute();
	}

	public class LocalBinder extends Binder {
		public FeedSynchronizer getService() {
			return (FeedSynchronizer.this);
		}
	}

	public String generateString(InputStream stream) {
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buffer = new BufferedReader(reader);
		StringBuilder sb = new StringBuilder();

		try {
			String cur;
			while ((cur = buffer.readLine()) != null) {
				sb.append(cur + "\n");
			}
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	class FetchFeedTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... unused) {
			HttpGet getMethod = new HttpGet(feedUrl);
			HttpResponse response = null;

			try {
				response = client.execute(getMethod);
				HttpEntity entity = response.getEntity();
				String data = generateString(entity.getContent());

				synchronized (this) {
					earthquakeFeed = data;
				}
				sendBroadcast(broadcast);
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return (null);
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
			// not needed here
		}

		@Override
		protected void onPostExecute(Void unused) {
			// not needed here
		}
	}

}
