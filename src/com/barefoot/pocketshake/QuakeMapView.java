package com.barefoot.pocketshake;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.barefoot.pocketshake.ui.QuakeItemizedMapOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class QuakeMapView extends MapActivity {

	private List<Overlay> mapOverlays;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapOverlays = mapView.getOverlays();

	    fetchOverlayDetails(savedInstanceState);
	    
	    updateOverlaysList();
	}
	
	private void fetchOverlayDetails(Bundle savedInstanceState) {
		
	}

	private void updateOverlaysList() {
		Drawable drawable = this.getResources().getDrawable(R.drawable.quake);
		QuakeItemizedMapOverlay itemizedoverlay = new QuakeItemizedMapOverlay(drawable);
		createOverlayItem(itemizedoverlay);
		mapOverlays.add(itemizedoverlay);
	}
	
	private void createOverlayItem(QuakeItemizedMapOverlay itemizedoverlay) {
		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "4.7 Richter", "In Bloody Mexico City");
		itemizedoverlay.addOverlay(overlayitem);
	}
}
