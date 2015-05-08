package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.GroupFeedAdapter.ViewHolder;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.ImageModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class UserPhotoAdapter extends BaseAdapter 
{
	ArrayList<ImageModel> list;
	Context context;
	ImageLoader imageloader;
	 Handler handler= new Handler();
	public  UserPhotoAdapter(Context context,ArrayList<ImageModel> list) 
	{
		this.context=context;
		this.list=list;
		imageloader=new ImageLoader(this.context);
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.userphotolistitem, parent, false);
			ImageView userphoto=(ImageView)convertView.findViewById(R.id.userphoto_imageview);
			imageloader.DisplayImage(list.get(position).getIcon(), userphoto);
//			getBitmap(userphoto, list.get(position).getIcon());
		}
		
		return convertView;
	}
	public void getBitmap(final ImageView profilePic,final String iconUrl)
    {
		
    	new Thread(new Runnable()
    	{

			@Override
			public void run()
			{
				
				handler.post(new Runnable()
				{
					
						Bitmap pfofile  = MainSingleTon.getBitmapFromURL(iconUrl);
					
					
					@Override
					public void run()
					{
						System.out.println("URL : "+iconUrl);
						profilePic.setImageBitmap((pfofile));
					}
				});
			
			
			}
		}).start();
    }

}
