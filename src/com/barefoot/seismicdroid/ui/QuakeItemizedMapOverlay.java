package com.barefoot.seismicdroid.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Gravity;
import android.widget.Toast;

import com.barefoot.seismicdroid.R;
import com.barefoot.seismicdroid.workers.TwitterPost;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class QuakeItemizedMapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private TwitterPost twitterPost;
	
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
	  dialog.setItems(new String[] {"Tweet This", "More Details","Search News","Cancel"}, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//optimize the if loops
			if (which == 0) {
				twitterPost = new TwitterPost(QuakeItemizedMapOverlay.this.mContext);
				boolean status = twitterPost.postMessage(item.getQuakeDetails().getTwitterMessage()); 
				showNotification(status);
			}
			
			if(which == 1) {
				Uri uri = Uri.parse(item.getQuakeDetails().getDetailLink());
				mContext.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
			}
			
			if(which == 2) {
				//redirect to google news with keywords
				String googleSearchURI = mContext.getString(R.string.newsQueryUri) + item.getQuakeDetails().getKeywords();
				Uri uri = Uri.parse(googleSearchURI);
				mContext.startActivity(new Intent( Intent.ACTION_VIEW, uri));
			}
			
			if(which == 3) {
				dialog.dismiss();
			}
		}

		private void showNotification(boolean status) {
			String text = status ? "Tweeted!!" : "Nah.. Something isn't working. Try Later.";
			Toast updateNotification = Toast.makeText(QuakeItemizedMapOverlay.this.mContext, text, Toast.LENGTH_SHORT);
			updateNotification.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
			updateNotification.show();
		}
		
	  });
	  dialog.show();
	  return true;
	}
}
