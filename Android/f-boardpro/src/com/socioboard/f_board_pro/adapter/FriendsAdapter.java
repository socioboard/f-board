package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.FriendModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter 
{
	Context context;
	ArrayList<FriendModel> friendList;
	 Handler handler= new Handler();
	 
	 ImageLoader imageLoader ;
	public  FriendsAdapter(Context context,ArrayList<FriendModel> friendList)
	{
		this.context=context;
		this.friendList=friendList;
		
		imageLoader = new ImageLoader(context);
		
	}

	@Override
	public int getCount()
	{
		return friendList.size();
	}

	@Override
	public FriendModel getItem(int position)
	{
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return friendList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView==null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.user_friend_item, parent, false);
		}
		TextView name=(TextView)convertView.findViewById(R.id.fname);
		name.setText(friendList.get(position).getFriendName());
		
		ImageView profilePic=(ImageView)convertView.findViewById(R.id.fpic);
		
	
		imageLoader.DisplayImage(friendList.get(position).getFriendPic(), profilePic);
		return convertView;
	}
	
}
