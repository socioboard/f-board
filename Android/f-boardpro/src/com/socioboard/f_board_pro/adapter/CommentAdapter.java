package com.socioboard.f_board_pro.adapter;

//adapter for setting comments
import java.util.ArrayList;

 



import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.CommentModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<CommentModel> commentList;
	Handler handler = new Handler();
	
	public CommentAdapter(Context context, ArrayList<CommentModel> commentList)
	{
		super();
		this.context = context;
		this.commentList = commentList;
	}

	@Override
	public int getCount() 
	{
		
		return commentList.size();
	}

	@Override
	public Object getItem(int position)
	{
		
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		
		return commentList.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.comment_item, parent, false);
		}
		
		TextView name=(TextView) convertView.findViewById(R.id.from);
		name.setText(commentList.get(position).getName());
		
		TextView datetime=(TextView) convertView.findViewById(R.id.datetime);
		datetime.setText(commentList.get(position).getDateTime());
		
		TextView comment=(TextView) convertView.findViewById(R.id.comment);
		comment.setText(commentList.get(position).getComment());
		
		ImageView image=(ImageView) convertView.findViewById(R.id.profilepic);
//		image.setBackgroundResource(R.drawable.profile);
		if(commentList.get(position).getProfilePic()!=null)
			getBitmap(image, commentList.get(position).getProfilePic());
		return convertView;
	}
	
	public void getBitmap(final ImageView profilePic,final String iconUrl)
    {
    	
    	new Thread(new Runnable() {

			@Override
			public void run() {
				
				handler.post(new Runnable()
				{
					
					Bitmap pfofile  = MainSingleTon.getBitmapFromURL(iconUrl);
					@Override
					public void run()
					{
						
						profilePic.setImageBitmap((pfofile));
					}
				});
			
			
			}
		}).start();
    }
}
