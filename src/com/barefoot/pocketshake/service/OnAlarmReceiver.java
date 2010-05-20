package com.barefoot.pocketshake.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		FeedSynchronizer.acquireStaticLock(context);
		context.startService(new Intent(context, FeedSynchronizer.class));
	}
	

}
