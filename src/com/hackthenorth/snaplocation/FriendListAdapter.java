package com.hackthenorth.snaplocation;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
		return mFriends.size();
	}

	@Override
	public Object getItem(int position) {
		return mFriends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.view_friend, parent, false);
		} else {
			view = convertView;
		}
		
		Friend friend = (Friend) getItem(position);
		((TextView) view.findViewById(R.id.friend_unique_id)).setText(friend.getDisplayName());
        return view;
	}

}
