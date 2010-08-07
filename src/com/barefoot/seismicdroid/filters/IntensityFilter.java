package com.barefoot.seismicdroid.filters;

import android.content.Context;

import com.barefoot.seismicdroid.data.EarthQuake;

public class IntensityFilter extends BaseFilter {
	
	public IntensityFilter(Context context, String keyName) {
		super(context, keyName);
	}
	
	@Override
	public boolean matches(EarthQuake quake) {
		int minIntensity = getPreferences().getInt(keyName, 3);
		return Float.parseFloat(quake.getIntensity()) > minIntensity;
	}
}
