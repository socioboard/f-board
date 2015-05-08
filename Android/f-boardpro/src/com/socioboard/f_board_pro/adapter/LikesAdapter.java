package com.socioboard.f_board_pro.adapter;
//adapter for setting like list
import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.LikeModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LikesAdapter extends BaseAdapter 
{
	
	private Context context;
	private ArrayList<LikeModel> likeList;
	private Handler handler = new Handler();

	public LikesAdapter(Context context, ArrayList<LikeModel> likeList) 
	{
		super();
		this.context = context;
		this.likeList = likeList;
	}

	@Override
	public int getCount()
	{
		return likeList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return likeList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return likeList.indexOf(getItem(position));
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.like_item, parent, false);
		}
		LikeModel likeModel=new LikeModel();
		likeModel=likeList.get(position);
		
		ImageView profilePic = (ImageView) convertView.findViewById(R.id.profilepic);
		getBitmap(profilePic, "https://graph.facebook.com/"+likeModel.getUserId()+"/picture?type=small");
		
		
		TextView name = (TextView) convertView.findViewById(R.id.name);
		name.setText(likeModel.getUserName());
		
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
