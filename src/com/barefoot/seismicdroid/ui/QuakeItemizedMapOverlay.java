package com.barefoot.seismicdroid.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.barefoot.seismicdroid.R;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class QuakeItemizedMapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public QuakeItemizedMapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public QuakeItemizedMapOverlay(Drawable defaultMarker, Context context) {
		  this(defaultMarker);
		  mContext = context;
		}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
	  final EarthQuakeOverlay item = (EarthQuakeOverlay)mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle()+"\n"+item.getSnippet());
	  dialog.setItems(new String[] {"More Details","Search News","Cancel"}, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//optimize the if loops
			if(which == 0) {
				Uri uri = Uri.parse(item.getQuakeDetails().getDetailLink());
				mContext.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
			}
			
			if(which == 1) {
				//redirect to google news with keywords
				String googleSearchURI = mContext.getString(R.string.newsQueryUri) + item.getQuakeDetails().getKeywords();
				Uri uri = Uri.parse(googleSearchURI);
				mContext.startActivity(new Intent( Intent.ACTION_VIEW, uri));
			}
			
			if(which == 2) {
				dialog.dismiss();
			}
		}
	  });
	  dialog.show();
	  return true;
	}
}
