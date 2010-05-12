package com.barefoot.pocketshake;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.barefoot.pocketshake.service.FeedSynchronizer;
import com.barefoot.pocketshake.storage.EarthQuakeDatabase;
import com.barefoot.pocketshake.storage.EarthQuakeDatabase.EarthquakeCursor;

public class PocketShake extends ListActivity {
	
	private FeedSynchronizer appService=null;
	private ArrayAdapter<String> messageListAdapter = null;
	private EarthQuakeDatabase db;
	private ArrayList<String> allEarthquakesList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new EarthQuakeDatabase(this);
        
        bindService(new Intent(this, FeedSynchronizer.class),
				onService, BIND_AUTO_CREATE);
        
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String eString = fetchCurrentQuakeDetails(position);
		Intent intent = new Intent(PocketShake.this, QuakeMapView.class);
		intent.putExtra("QUAKE_STRING", eString);
        startActivity(intent);
	}
    
    @Override
	public void onResume() {
		super.onResume();

		registerReceiver(receiver,
					new IntentFilter(FeedSynchronizer.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();

		unregisterReceiver(receiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unbindService(onService);
	}
	
	private void updateQuakeFeed() {
		if (appService != null) {
			String data = appService.getEarthquakeData();
			messageListAdapter = new ArrayAdapter<String>(PocketShake.this, R.layout.quake, fetchLatestFeeds(true));
			setListAdapter(messageListAdapter);
		}
	}
	
	private BroadcastReceiver receiver=new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateQuakeFeed();
		}
	};
    
    private ServiceConnection onService=new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder rawBinder) {
			appService=((FeedSynchronizer.LocalBinder)rawBinder).getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			appService=null;
		}
	};
	
	private String[] fetchLatestFeeds(boolean forceFetch) {
		if(allEarthquakesList == null || forceFetch) {
			populateEarthquakeList();
		}
		return allEarthquakesList == null ? new String[0] : allEarthquakesList.toArray(new String[allEarthquakesList.size()]);
	}
	
	private void populateEarthquakeList() {
		EarthquakeCursor allEarthquakes = db.getEarthquakes();
		try {
			if(allEarthquakes != null && allEarthquakes.moveToFirst()) {
				allEarthquakesList = new ArrayList<String>();
				StringBuffer earthquakeInfo = null;
				do {
					earthquakeInfo = new StringBuffer();
					earthquakeInfo.append(allEarthquakes.getIntensity());
					earthquakeInfo.append("::");
					earthquakeInfo.append(allEarthquakes.getLocation());
					allEarthquakesList.add(earthquakeInfo.toString());
				} while(allEarthquakes.moveToNext());
			}
		} finally {
			allEarthquakes.close();
		}
	}
	
	private String fetchCurrentQuakeDetails(int index) {
		if(allEarthquakesList == null) {
			populateEarthquakeList();
		}
		
		if(allEarthquakesList.size() > index) {
			return allEarthquakesList.get(index);
		}
		return null;
	}
}