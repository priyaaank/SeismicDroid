package com.barefoot.seismicdroid;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.barefoot.seismicdroid.data.EarthQuake;
import com.barefoot.seismicdroid.R;

public class QuakeCustomAdapter extends ArrayAdapter<EarthQuake> {

	private ArrayList<EarthQuake> quakeList = new ArrayList<EarthQuake>();
	private int[] intensityColorArray = new int[] {R.drawable.green, R.drawable.yellow, R.drawable.orange, R.drawable.red}; 

	public QuakeCustomAdapter(Context context, int textViewResourceId, ArrayList<EarthQuake> quakes) {
        super(context, textViewResourceId, quakes);
        this.quakeList = quakes;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View v = convertView;
		if(v==null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.quake, null);
		}
		EarthQuake eachQuake = quakeList.get(position);
		if(eachQuake != null) {
			TextView intensity = (TextView) v.findViewById(R.id.intensity_value);
			TextView location = (TextView) v.findViewById(R.id.location_value);
			TextView date = (TextView) v.findViewById(R.id.date_of_occurance);
			
			if(intensity != null) {
				intensity.setText(eachQuake.getIntensity());
				intensity.setBackgroundResource(getBackgroundColor(eachQuake.getIntensity()));
			}
			if(location != null) {
				location.setText(eachQuake.getLocation());
			}
			if(date != null) {
				date.setText(eachQuake.getLocalTime());
			}
		}
		return v;
	}

	private int getBackgroundColor(String intensity) {
		double doubleIntensity = Double.parseDouble(intensity);
		if(doubleIntensity < 3.5)
			return intensityColorArray[0];
		if(doubleIntensity >= 3.5 && doubleIntensity < 5.5)
			return intensityColorArray[1];
		if(doubleIntensity >= 5.5 && doubleIntensity < 6.5)
			return intensityColorArray[2];
		return intensityColorArray[3];
	}
}
