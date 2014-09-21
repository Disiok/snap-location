package com.hackthenorth.snaplocation.view;

import java.util.ArrayList;

import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.id;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.model.Friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{
	ArrayList<Friend> mFriends;
	Context mContext;
	boolean mFriendsSelectable = false;
	
	public FriendListAdapter(Context context, ArrayList<Friend> friends, boolean friendsSelectable) {
		mContext = context;
		mFriends = friends;
		mFriendsSelectable = friendsSelectable;
	}

	@Override
	public int getCount() {
		if (mFriendsSelectable) {
			return mFriends.size();
		} else {
			return mFriends.size() + 2;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mFriendsSelectable) {
			return mFriends.get(position);
		} else {
			return mFriends.get(position - 2);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getViewForFriend(final Friend friend, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.view_friend, parent, false);
		} else {
			view = convertView;
		}
		
		((TextView) view.findViewById(R.id.friend_display_name)).setText(friend.getDisplayName());
		((TextView) view.findViewById(R.id.friend_unique_name)).setText("@" + friend.getUniqueName());
		((TextView) view.findViewById(R.id.friend_pending_rounds)).setText("" + friend.getNumberOfRoundsPending());
		if (friend.getNumberOfRoundsPending() > 0) {
			((TextView) view.findViewById(R.id.friend_pending_rounds)).setTextColor(Color.parseColor("#EEEEEE"));
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, GameActivity.class);
					intent.putExtra(GameActivity.EXTRA_USER, "htn");
					intent.putExtra(GameActivity.EXTRA_OTHER, friend.getUniqueName());
					mContext.startActivity(intent);
				}
			});
		} else {
			((ImageView) view.findViewById(R.id.friend_counter_background)).setColorFilter(Color.parseColor("#FFFFFF"));
		}
        return view;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (mFriendsSelectable) {
			Friend friend = (Friend) getItem(position);
			view = getViewForFriend(friend, convertView, parent);
			((FrameLayout) view.findViewById(R.id.friend_checkbox)).setVisibility(View.VISIBLE);
			((FrameLayout) view.findViewById(R.id.friend_pending_counter)).setVisibility(View.GONE);
			return view;
		} else {
			switch (position) {
			case 0:
				if (convertView == null) {
					view = LayoutInflater.from(mContext).inflate(R.layout.view_profile, parent, false);
				} else {
					view = convertView;
				}
				((TextView) view.findViewById(R.id.profile_display_name)).setText("Hack The North");
				((TextView) view.findViewById(R.id.profile_unique_name)).setText("@htn");
				return view;
			case 1:
				if (convertView == null) {
					view = LayoutInflater.from(mContext).inflate(R.layout.view_friends_seperator, parent, false);
				} else {
					view = convertView;
				}
				return view;
			default:
				Friend friend = (Friend) getItem(position);
		        view = getViewForFriend(friend, convertView, parent);
		        ((FrameLayout) view.findViewById(R.id.friend_checkbox)).setVisibility(View.GONE);
				((FrameLayout) view.findViewById(R.id.friend_pending_counter)).setVisibility(View.VISIBLE);
		        return view;
			}
		}
	}

}
