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

public class PocketShake extends ListActivity {
	
	private FeedSynchronizer appService=null;
	private ArrayAdapter<String> messageListAdapter = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bindService(new Intent(this, FeedSynchronizer.class),
				onService, BIND_AUTO_CREATE);
        
    }
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Do nothing for now
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
			messageListAdapter = new ArrayAdapter<String>(PocketShake.this, R.layout.quake, generateTempData(data));
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
	
	private String[] generateTempData(String data) {
		String[] stringss = new String[5];
		stringss[0] = "This is test data 1";
		stringss[1] = "This is test data 2";
		stringss[2] = "This is test data 3";
		stringss[3] = "This is test data 4";
		stringss[4] = data;
		return stringss;
	}
}