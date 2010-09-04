package com.parag.clap;

import java.util.ArrayList;

import com.parag.clap.R;
import com.google.ads.GoogleAdView;
import com.google.ads.AdSenseSpec;
import com.google.ads.AdSenseSpec.AdType;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
public class ClapClap extends Activity implements OnTouchListener
{	
	private static final String CLIENT_ID = "ca-mb-pub-8882846910513907";
	private static final String COMPANY_NAME = "parag-arora";
	private static final String APP_NAME = "ClapClap";
	private static final String CHANNEL_ID = "8425259817";
	private int numClaps = 6;
	private int numClapsThreshold = 12;
	private int thresholdTime = 1600;
	private volatile int interval = 0;
	private long lastMillis;
	private int clapCount = 0;
	private ArrayList<MediaPlayer> singleClaps = new ArrayList<MediaPlayer>(numClaps);
	private MediaPlayer crowdClap;
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		GoogleAdView adView = (GoogleAdView) findViewById(R.id.adview);
		AdSenseSpec adSenseSpec =
	        new AdSenseSpec(CLIENT_ID)
	        .setCompanyName(COMPANY_NAME)
	        .setAppName(APP_NAME)
	        .setChannel(CHANNEL_ID)
	        .setAdType(AdType.TEXT)
	        .setAdTestEnabled(true); // Set to true while testing.
	    adView.showAds(adSenseSpec);
	    lastMillis = System.currentTimeMillis();
	    ImageView i = (ImageView) findViewById(R.id.handview);
	    i.setOnTouchListener(this);
	    for(int k=0; k<numClaps; k++)
	    	singleClaps.add(MediaPlayer.create(getBaseContext(), R.raw.singleclap));
	    crowdClap= MediaPlayer.create(getBaseContext(), R.raw.app);
	}

	public boolean onTouch(View v, MotionEvent event) 
	{
		if(crowdClap.isPlaying())
			return true;
		
		
		if(interval>thresholdTime)
		{
			resetTimer();
		}
		else
		{
			interval = (int) (System.currentTimeMillis() - lastMillis);
			if(clapCount>=numClapsThreshold)
			{
				resetTimer();
				crowdClap.start();
				return true;
			}
		}
		
		clapCount++;
		playClap();
		return true;
	}
	
	private void playClap()
	{
		for(int k=0; k<numClaps; k++)
		{
			if(!singleClaps.get(k).isPlaying())
			{
				singleClaps.get(k).start();
				return;
			}
		}
	}
	
	private void resetTimer()
	{
		clapCount = 0;
		interval = 0;
		lastMillis = System.currentTimeMillis();
	}
}