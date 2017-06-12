package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.AlbumModel;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends BaseAdapter
{
	Context context;
	ArrayList<AlbumModel> albumList;
	ImageLoader imageloader;
	public AlbumAdapter(Context context,ArrayList<AlbumModel> albumList) 
	{
		this.albumList=albumList;
		this.context=context;
		imageloader=new ImageLoader(this.context);
	}
	@Override
	public int getCount()
	{
		
		return albumList.size();
	}

	@Override
	public Object getItem(int position)
	{
		
		return albumList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return albumList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = mInflater.inflate(R.layout.userphotolistitem, parent, false);
		}
		ImageView userphoto  = (ImageView)convertView.findViewById(R.id.userphoto_imageview);
		
		TextView nameCount   = (TextView)convertView.findViewById(R.id.name);
		
		//imageloader.DisplayImage(albumList.get(position).getAlbumCover(), userphoto);
		Picasso.with(context).load(albumList.get(position).getAlbumCover()).into(userphoto);
		
		if(albumList.get(position).getAlbumSize()>0)
		{
			nameCount.setText(albumList.get(position).getAlbumName()+" "+albumList.get(position).getAlbumSize());
		}
		else
		{
			nameCount.setText(albumList.get(position).getAlbumName());
		}
		return convertView;
	}

}
