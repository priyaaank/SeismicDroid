package com.barefoot.pocketshake;

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
import com.barefoot.pocketshake.storage.EarthQuakeDataWrapper;

public class PocketShake extends ListActivity {
	
	private FeedSynchronizer appService=null;
	private ArrayAdapter<String> messageListAdapter = null;
	private EarthQuakeDataWrapper dbWrapper;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dbWrapper = new EarthQuakeDataWrapper(this);
        
        bindService(new Intent(this, FeedSynchronizer.class),
				onService, BIND_AUTO_CREATE);
        
        updateQuakeFeed();
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(PocketShake.this, QuakeMapView.class);
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
			dbWrapper.refreshFeedCache(true);
			messageListAdapter = new ArrayAdapter<String>(PocketShake.this, R.layout.quake, fetchLatestFeeds());
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
	
	private String[] fetchLatestFeeds() {
		return dbWrapper.getStringRepresentationForQuakes();
	}
}