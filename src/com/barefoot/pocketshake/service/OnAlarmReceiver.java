package com.barefoot.pocketshake.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("AlarmReceiver", "Received Alarm Notification, will invoke service");
		FeedSynchronizer.acquireStaticLock(context);
		context.startService(new Intent(context, FeedSynchronizer.class));
	}
	

}
