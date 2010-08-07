package com.barefoot.pocketshake.filters;

import android.content.Context;

import com.barefoot.pocketshake.data.EarthQuake;

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
