package com.barefoot.pocketshake.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootServiceInvoker extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("BootInvoker", "Received Boot Notification. Will start service and will do a recurring schedule");
		(new AlarmScheduler(context)).updateAlarmSchedule();
	}
}
