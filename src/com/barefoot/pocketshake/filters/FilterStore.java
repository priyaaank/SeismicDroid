package com.barefoot.pocketshake.filters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.barefoot.pocketshake.data.EarthQuake;

public class FilterStore {
	
	List<BaseFilter> filterList = new ArrayList<BaseFilter>();
	
	public FilterStore(Context context) {
		filterList.add(new IntensityFilter(context, "intensity_setting"));
		filterList.add(new RadiusFilter(context, "radius_value"));
	}
	
	public boolean pass(EarthQuake quake) {
		for (BaseFilter filter : filterList) {
			if (!filter.matches(quake))
				return false;
		}
		return true;
	}
}
