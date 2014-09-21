package com.hackthenorth.snaplocation.view;

import java.util.ArrayList;

import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.id;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.model.Friend;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter{
	ArrayList<Friend> mFriends;
	Context mContext;
	
	public FriendListAdapter(Context context, ArrayList<Friend> friends) {
		mContext = context;
		mFriends = friends;
	}

	@Override
	public int getCount() {
		return mFriends.size() + 2;
	}

	@Override
	public Object getItem(int position) {
		return mFriends.get(position - 2);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
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
			if (convertView == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.view_friend, parent, false);
			} else {
				view = convertView;
			}
			
			Friend friend = (Friend) getItem(position);
			((TextView) view.findViewById(R.id.friend_display_name)).setText(friend.getDisplayName());
			((TextView) view.findViewById(R.id.friend_unique_name)).setText("@" + friend.getUniqueName());
			((TextView) view.findViewById(R.id.friend_pending_rounds)).setText("" + friend.getNumberOfRoundsPending());
			if (friend.getNumberOfRoundsPending() > 0) {
				((TextView) view.findViewById(R.id.friend_pending_rounds)).setTextColor(Color.parseColor("#EEEEEE"));
			} else {
				((ImageView) view.findViewById(R.id.friend_counter_background)).setColorFilter(Color.parseColor("#FFFFFF"));
			}
	        return view;
		}
	}

}
