package com.barefoot.pocketshake.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class BootServiceInvoker extends BroadcastReceiver {
	
	private static final int PERIOD=120000; 	// 2 minutes

	@Override
	public void onReceive(Context context, Intent intent) {
		AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i=new Intent(context, OnAlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);

		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
						SystemClock.elapsedRealtime()+60000,
						PERIOD,
						pi);
	}
}
