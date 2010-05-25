package com.barefoot.pocketshake.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmScheduler {

	private Context mContext;
	
	public AlarmScheduler(Context mContext) {
		this.mContext = mContext;
	}
	
	public void updateAlarmSchedule() {
		AlarmManager mgr=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(mContext, OnAlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(mContext, 0, i, 0);
		
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		//default value is an hour
		String period = defaultSharedPreferences.getString("time_freq_unit", "3600000");
		
		if(period != null) {
			try {
				mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
					SystemClock.elapsedRealtime()+5000,
					Integer.parseInt(period),
					pi);
				Log.i("AlarmScheduler rescheduling the alarm","Alarm scheduled to go off at an interval of "+period+" millisec");
			} catch(NumberFormatException e) {
				Log.e("AlarmScheduler trying to convert preference value into alarm interval", e.getMessage());
			}
		}
	}
}
