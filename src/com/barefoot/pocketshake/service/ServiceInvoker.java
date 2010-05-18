package com.barefoot.pocketshake.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceInvoker extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(), FeedSynchronizer.class.getName());
		ComponentName service = context.startService(intent.setComponent(comp));
		if (null == service) {
			Log.e("Service Invoker", "Could not start service");
		} else {
			Log.i("Service Invoker", "Service Started Successfully");
		}
	}
}
