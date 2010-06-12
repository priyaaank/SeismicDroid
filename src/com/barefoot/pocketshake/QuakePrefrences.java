package com.barefoot.pocketshake;

import com.barefoot.pocketshake.service.AlarmScheduler;
import com.barefoot.pocketshake.storage.EarthQuakeDataWrapper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class QuakePrefrences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.i("Preference Change Listener", "Change Listener has been called for key:: " + key);
		if("time_freq_unit".equalsIgnoreCase(key)) {
			Log.i("Preference Change Listener", "Updating service alarm to be run at new interval");
			new AlarmScheduler(this).updateAlarmSchedule();
		}
		
		if("intensity_setting".equalsIgnoreCase(key)) {
			Log.i("Preference Change Listener", "Updating min earthquake intensity and refreshing list");
			new EarthQuakeDataWrapper(this).refreshFeedCache(true);
		}
	}
}