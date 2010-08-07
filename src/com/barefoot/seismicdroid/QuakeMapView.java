package com.barefoot.seismicdroid;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.barefoot.seismicdroid.data.EarthQuake;
import com.barefoot.seismicdroid.storage.EarthQuakeDataWrapper;
import com.barefoot.seismicdroid.ui.EarthQuakeOverlay;
import com.barefoot.seismicdroid.ui.QuakeItemizedMapOverlay;
import com.barefoot.seismicdroid.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class QuakeMapView extends MapActivity {

	private List<Overlay> mapOverlays;
	private EarthQuakeDataWrapper dbWrapper;
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    dbWrapper = new EarthQuakeDataWrapper(this);
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapOverlays = mapView.getOverlays();

	    int pos = getIntent().getIntExtra("POS", 1);
	    EarthQuake quakeDetails = dbWrapper.getQuakeAtPosition(pos);

	    focusOnQuakeArea(mapView, quakeDetails);
	    updateOverlaysList(quakeDetails);
	}

	private void focusOnQuakeArea(MapView mapView, EarthQuake quakeDetails) {
		GeoPoint point = new GeoPoint(quakeDetails.getMicroLongitudes(), quakeDetails.getMicroLatitudes());
		MapController mapViewController = mapView.getController();
		mapViewController .animateTo(point);
		mapViewController.setZoom(8); 
        mapView.postInvalidate();
	}
	
	private void updateOverlaysList(EarthQuake currentQuakeDetails) {
		Drawable drawable = this.getResources().getDrawable(R.drawable.zone);
		QuakeItemizedMapOverlay itemizedoverlay = new QuakeItemizedMapOverlay(drawable, this);
		createOverlayItem(itemizedoverlay, currentQuakeDetails);
		mapOverlays.add(itemizedoverlay);
	}
	
	private void createOverlayItem(QuakeItemizedMapOverlay itemizedoverlay, EarthQuake currentQuakeDetails) {
			Log.i("Longitude and Latitude :: ", "Longitude ["+ currentQuakeDetails.getMicroLongitudes() +"], Latitude ["+currentQuakeDetails.getMicroLongitudes()+"]");
			GeoPoint point = new GeoPoint(currentQuakeDetails.getMicroLongitudes(), currentQuakeDetails.getMicroLatitudes());
			EarthQuakeOverlay overlayitem = new EarthQuakeOverlay(point, currentQuakeDetails.getIntensity() + " Richters", currentQuakeDetails.getLocation(), currentQuakeDetails);
			itemizedoverlay.addOverlay(overlayitem);
	}
}
