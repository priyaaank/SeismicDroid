package com.barefoot.pocketshake.filters;

import android.content.Context;
import android.location.Location;

import com.barefoot.pocketshake.data.EarthQuake;
import com.barefoot.pocketshake.service.ReferencePointCalculator;

public class RadiusFilter extends BaseFilter {

	private ReferencePointCalculator refCalculator;
	
	public RadiusFilter(Context context, String keyName) {
		super(context, keyName);
		this.refCalculator = new ReferencePointCalculator(context);
	}

	@Override
	public boolean matches(EarthQuake quake) {
		Location userLocation = refCalculator.getReferencePoint();
		if( userLocation == null) 
			return true;
		Location quakeLocation = new Location("Quake Location");
		quakeLocation.setLatitude(quake.getMicroLongitudes()/10E5);
		quakeLocation.setLongitude(quake.getMicroLatitudes()/10E5);
		return ((quakeLocation.distanceTo(userLocation)/1000) < getCurrentRadius());
	}
	
	private int getCurrentRadius() {
		return getPreferences().getInt("radius_value", 19000);
	}

}
