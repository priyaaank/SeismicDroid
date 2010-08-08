package com.barefoot.seismicdroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class About extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);		
		setContentView(R.layout.about);
		
		//update various parts of the view
		updateLicenseDetails();
	}

	private void updateLicenseDetails() {
		String htmlText = "<span xmlns:dc='http://purl.org/dc/elements/1.1/' href='http://purl.org/dc/dcmitype/InteractiveResource' property='dc:title' rel='dc:type'>SeismicDroid</span> by <a xmlns:cc='http://creativecommons.org/ns#' href='http://github.com/priyaaank/SeismicDroid' property='cc:attributionName' rel='cc:attributionURL'>Priyank Gupta</a> is licensed under a <a rel='license' href='http://creativecommons.org/licenses/by-sa/3.0/'>Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.";  
		TextView licenseText = (TextView) findViewById(R.id.license_text);
		licenseText.setText(Html.fromHtml(htmlText));
		licenseText.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	

}