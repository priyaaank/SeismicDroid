package com.barefoot.seismicdroid.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.barefoot.seismicdroid.data.EarthQuake;

public abstract class BaseFilter {
	
	private Context baseContext;
	protected String keyName;
	
	public BaseFilter(Context context, String keyName) {
		this.baseContext = context;
		this.keyName = keyName;
	}
	
	protected SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(baseContext);
	}
	
	public abstract boolean matches(EarthQuake quake);
}
