package com.barefoot.pocketshake;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class QuakePrefrences extends PreferenceActivity {
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}