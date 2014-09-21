package com.hackthenorth.snaplocation.view;

import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class GameActivity extends Activity{
	ImageView mControlButton;
	ImageView mGameImage;
	
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
}
