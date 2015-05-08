package com.socioboard.f_board_pro.adapter;
//adapter for setting home feed list

import java.util.ArrayList;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.HomeFeedModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

 
public class HomeFeedAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<HomeFeedModel> feedList;
	private Handler handler = new Handler();

	public HomeFeedAdapter(Context context, ArrayList<HomeFeedModel> feedList) 
	{
		this.context = context;
		this.feedList = feedList;	}
	@Override
	public int getCount()
	{
		return feedList.size();
	}

	@Override
	public HomeFeedModel getItem(int position)
	{
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return feedList.indexOf(getItem(position));
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.home_feed_item, parent, false);
		}


		HomeFeedModel rowItem = getItem(position);
		
		TextView mTitle=(TextView) convertView.findViewById(R.id.from);
		mTitle.setText(rowItem.getFrom());
		
		TextView mMessage=(TextView) convertView.findViewById(R.id.message);
		mMessage.setText(rowItem.getMessage());

		TextView mTime=(TextView) convertView.findViewById(R.id.datetime);
		mTime.setText(rowItem.getDateTime());

		TextView mDescription=(TextView) convertView.findViewById(R.id.description);
		mDescription.setText(rowItem.getDescription());

		ImageView mImage=(ImageView) convertView.findViewById(R.id.image);
		mImage.setBackgroundResource(R.drawable.photo);
		if(rowItem.getPicture()!=null)
			setImageFromUrl(mImage, position, rowItem.getPicture());
		else
			mImage.setVisibility(View.GONE);
		ImageView profilepic=(ImageView) convertView.findViewById(R.id.profilepic);
		
		if(rowItem.getProfilePic()!=null)
			setImageFromUrl(profilepic, position, rowItem.getProfilePic());
		else
			profilepic.setBackgroundResource(R.drawable.profile);
		
		TextView likes=(TextView) convertView.findViewById(R.id.likes);
		if(rowItem.getLikes()>0)
		{
			likes.setText("Likes "+rowItem.getLikes());
		}
		else
		{
			likes.setText("Like");
		}
		
		TextView comments=(TextView) convertView.findViewById(R.id.comments);
		if(rowItem.getComments()>0)
		{
			comments.setText("Comments "+rowItem.getComments());
		}
		else
		{
			comments.setText("Comment");
		}
		
		TextView share=(TextView) convertView.findViewById(R.id.share);
		share.setText("Share");
		return convertView;
	}
	private void setImageFromUrl(final ImageView mImage, final int positon, final String imageURL)
	{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					final Bitmap imageBitmap = MainSingleTon.getBitmapFromURL(imageURL);
					handler.post(new Runnable() 
					{
						@Override
						public void run() 
						{
							mImage.setImageBitmap(imageBitmap);
						}
					});
				}
			}).start();
	}
}
