package com.hackthenorth.snaplocation.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.id;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.util.DisableableViewPager;
import com.hackthenorth.snaplocation.util.GPSTracker;
import com.hackthenorth.snaplocation.util.UploadMediaTask;
import com.hackthenorth.snaplocation.util.Utils;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();

	DisableableViewPager mViewPager;
	MainScreenPagerAdapter mPagerAdapter;
	boolean mBackLocked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//mImageInbox = (ImageView) findViewById(R.id.image_inbox);
		//mPolaroidBorder = findViewById(R.id.polaroid_border);

		mViewPager = (DisableableViewPager) findViewById(R.id.view_pager);
		mPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(1);

		// Set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public DisableableViewPager getViewPager() {
		return mViewPager;
	}
	
	public void setBackButtonLock(boolean lock) {
		mBackLocked = lock;
	}
	
	@Override
	public void onBackPressed() {
		if (!mBackLocked) {
			super.onBackPressed();
		}
	}
}
