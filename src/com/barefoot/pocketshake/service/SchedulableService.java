package com.barefoot.pocketshake.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

abstract public class SchedulableService extends IntentService {

	private static final String LOCK_NAME_STATIC = "com.barefoot.pocketshake.service.SchedulableService";
	private static PowerManager.WakeLock lockStatic;

	public SchedulableService(String name) {
		super(name);
	}
	
	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
	}
	
	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);

			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}

		return(lockStatic);
	}

	public abstract void doServiceTask(Intent intent);
	
	final protected void onHandleIntent(Intent intent) {
		try {
			doServiceTask(intent);
		}
		finally {
			getLock(this).release();
		}
	}
}
