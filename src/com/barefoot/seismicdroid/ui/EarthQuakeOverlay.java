package com.barefoot.seismicdroid.ui;

import com.barefoot.seismicdroid.data.EarthQuake;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class EarthQuakeOverlay extends OverlayItem {

	private EarthQuake quakeDetails;

	public EarthQuakeOverlay(GeoPoint point, String title, String snippet, EarthQuake quake) {
		super(point, title, snippet);
		this.quakeDetails = quake;
	}
	
	public EarthQuake getQuakeDetails() {
		return quakeDetails;
	}
}
