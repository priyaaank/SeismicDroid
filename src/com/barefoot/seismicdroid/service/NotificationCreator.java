package com.barefoot.seismicdroid.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.barefoot.seismicdroid.SeismicDroid;
import com.barefoot.seismicdroid.R;

public class NotificationCreator {
	
	private Context notificationContext;
	private int notificationIcon;
	private NotificationManager notificationManager; 
	private PendingIntent notificationIntent;
	
	public NotificationCreator(Context context, int icon) {
		this.notificationContext= context;
		this.notificationIcon = icon == 0 ? R.drawable.quakestatus : icon;
	}

	public void createNotification(int id, String summary, String title, String text, long when, boolean isVibrate) {
		Intent notificationIntent = new Intent(notificationContext, SeismicDroid.class);
		this.notificationIntent = PendingIntent.getActivity(notificationContext, 0, notificationIntent, 0);
		this.notificationManager = (NotificationManager) notificationContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(notificationIcon, summary, when);
		notification.setLatestEventInfo(notificationContext, title, text, this.notificationIntent);

		if (isVibrate) {
			long[] vibrate = {0,100,200,300};
			notification.vibrate = vibrate;
		}
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(id, notification);
	}	
}
