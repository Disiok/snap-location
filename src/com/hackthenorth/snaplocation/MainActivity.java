package com.hackthenorth.snaplocation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
	
	ViewPager mViewPager;
	MainScreenPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		// Set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
}
