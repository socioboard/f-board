package com.socioboard.f_board_pro.adapter;
//adapter for setting pagefeeds
import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.GroupFeedModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PagesFeed_Adpater extends   BaseAdapter
{
	private Context context;
	private ArrayList<GroupFeedModel> groupList;
    Handler handler= new Handler();
	
	public PagesFeed_Adpater(Context context, ArrayList<GroupFeedModel> groupList)
	{
		super();
		this.context = context;
		this.groupList = groupList;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return groupList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return groupList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.pages_feed_rowitem, parent, false);
		}
		TextView from=(TextView) convertView.findViewById(R.id.from);
		from.setText(groupList.get(position).getFrom());
		
		TextView dateTime=(TextView) convertView.findViewById(R.id.datetime);
		dateTime.setText(groupList.get(position).getDateTime());
		
		TextView message=(TextView) convertView.findViewById(R.id.message);
		message.setText(groupList.get(position).getMessage());
		
		TextView likes=(TextView) convertView.findViewById(R.id.likes);
		if(groupList.get(position).getLikes()>0)
		{
			System.out.println("aa no of likes "+groupList.get(position).getLikes());
			likes.setText("Likes "+groupList.get(position).getLikes());
		}
		else
		{
			System.out.println("aa no  likes "+groupList.get(position).getLikes());
			likes.setText("Like");
		}
		
		TextView comments=(TextView) convertView.findViewById(R.id.comments);
		if(groupList.get(position).getComments()>0)
		{
			comments.setText("Comments "+groupList.get(position).getComments());
		}
		else
		{
			comments.setText("Comment");
		}
		
		TextView share=(TextView) convertView.findViewById(R.id.share);
		share.setText("Share");
		
		
		ImageView picture=(ImageView) convertView.findViewById(R.id.picture);
		
//		picture.setsrc;
		if(groupList.get(position).getPicture()!=null)
			getBitmap(picture, groupList.get(position).getPicture());
		else
		{
			picture.setBackgroundResource(R.drawable.photo);
			picture.setVisibility(View.GONE);
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
