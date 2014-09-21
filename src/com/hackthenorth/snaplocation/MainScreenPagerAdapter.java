package com.hackthenorth.snaplocation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class MainScreenPagerAdapter extends FragmentPagerAdapter{
	public static final String TAG = MainScreenPagerAdapter.class.getSimpleName();
	public static final int FRAGMENT_INBOX = 0;
	public static final int FRAGMENT_CONTROL = 1;
	public static final int NUM_PAGES = 2;

	public MainScreenPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case FRAGMENT_CONTROL: {
			return new ControlFragment();
		}
		case FRAGMENT_INBOX: {
			return new FriendFragment();
		}
		default:
			Log.e(TAG, "Scrolling out of bound");
			return null;
		}
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}

}
