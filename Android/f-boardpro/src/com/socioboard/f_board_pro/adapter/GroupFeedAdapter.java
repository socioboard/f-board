package com.socioboard.f_board_pro.adapter;
//adapter for setting group feed list

import java.util.ArrayList;

 






import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.fragments.Display_User_Details;
import com.socioboard.f_board_pro.models.GroupFeedModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupFeedAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<GroupFeedModel> groupList;
    Handler handler= new Handler();
	
	public GroupFeedAdapter(Context context, ArrayList<GroupFeedModel> groupList)
	{
		super();
		this.context = context;
		this.groupList = groupList;
	}

	@Override
	public int getCount()
	{
		return groupList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return groupList.indexOf(getItem(position));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		 ViewHolder holder;
		
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.group_feed_item, parent, false);
			 holder = new ViewHolder();
			 convertView.setTag(holder);
		}
		else
		{
			   holder = (ViewHolder) convertView.getTag();
		}
		 holder.from=(TextView) convertView.findViewById(R.id.from);
		 holder.from.setText(groupList.get(position).getFrom());
		
		 holder.from.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				/*MainActivity.swipeFragment(new Display_User_Details(groupList.get(position).getFromID()));
				System.out.println();*/
				
			}
		});
		
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
		
		ImageView profilPicture=(ImageView) convertView.findViewById(R.id.profilepic);
		
		if(groupList.get(position).getProfilePic()!=null)
			getBitmap(profilPicture, groupList.get(position).getProfilePic());
		else
			profilPicture.setBackgroundResource(R.drawable.profile);
		
		ImageView picture=(ImageView) convertView.findViewById(R.id.picture);
		picture.setBackgroundResource(R.drawable.photo);
		
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
	public class ViewHolder
	{		  
		  TextView from;
	}
}
