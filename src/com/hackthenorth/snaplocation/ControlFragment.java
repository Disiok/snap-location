package com.hackthenorth.snaplocation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ControlFragment extends Fragment {
	public static final String TAG = ControlFragment.class.getSimpleName();
	
	ImageView mCaptureButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.fragment_control, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		// Resolve view elements
		mCaptureButton = (ImageView) getView().findViewById(R.id.capture_button);
		mCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewPager viewPager = ((MainActivity) getActivity()).getViewPager();
				viewPager.setCurrentItem(1);
			}
		});
	}
	
}
