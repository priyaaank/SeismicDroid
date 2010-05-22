package com.barefoot.pocketshake;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.barefoot.pocketshake.data.EarthQuake;

public class QuakeCustomAdapter extends ArrayAdapter<EarthQuake> {

	private ArrayList<EarthQuake> quakeList = new ArrayList<EarthQuake>();

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
			}
			if(location != null) {
				location.setText(eachQuake.getLocation());
			}
			if(date != null) {
				date.setText(eachQuake.getDate());
			}
		}
		return v;
	}
}
