package com.hackthenorth.snaplocation.view;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity {
	ImageView mControlButton;
	ImageView mGameImage;

	GoogleMap mGoogleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mControlButton = (ImageView) findViewById(R.id.control_button);
		mGameImage = (ImageView) findViewById(R.id.game_image);

		mControlButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					mGameImage.setVisibility(View.VISIBLE);
					return true;
				} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					mGameImage.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
		mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				mGoogleMap.clear();
				mGoogleMap.addMarker(new MarkerOptions()
		        .position(arg0)
		        .title("Chosen location"));
				Toast.makeText(getBaseContext(), arg0.toString(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void getImage() {
		// TODO do something with server to get image and update the imageview
	}

	private void initilizeMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (mGoogleMap == null) {
				Toast.makeText(this, "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
