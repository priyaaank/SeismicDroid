package com.barefoot.pocketshake;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.barefoot.pocketshake.service.FeedSynchronizer;
import com.barefoot.pocketshake.storage.EarthQuakeDataWrapper;

public class PocketShake extends ListActivity {
	
	private ArrayAdapter<String> messageListAdapter = null;
	private EarthQuakeDataWrapper dbWrapper;
	private ProgressDialog dialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.dialog = new ProgressDialog(this);
        dbWrapper = new EarthQuakeDataWrapper(this);
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
		registerReceiver(receiver, new IntentFilter(FeedSynchronizer.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}
	
	private void updateQuakeFeed() {
		this.dialog.setMessage("Fetching...");
		this.dialog.show();
		dbWrapper.refreshFeedCache(true);
		messageListAdapter = new ArrayAdapter<String>(PocketShake.this, R.layout.quake, fetchLatestFeeds());
		setListAdapter(messageListAdapter);
		this.dialog.dismiss();
	}
	
	private BroadcastReceiver receiver=new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			updateQuakeFeed();
		}
	};
	
	private String[] fetchLatestFeeds() {
		return dbWrapper.getStringRepresentationForQuakes();
	}
}