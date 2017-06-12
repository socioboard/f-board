package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.ImageModel;
import com.squareup.picasso.Picasso;

public class CustomPagerAdapter extends PagerAdapter
{
	Context mContext;
	LayoutInflater mLayoutInflater;
	boolean showDeatils=true;
	ImageLoader imageloader;
	ArrayList<ImageModel> imageList;
	public CustomPagerAdapter(Context context,ArrayList<ImageModel> imageList)
	{
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageList=imageList;
		imageloader=new ImageLoader(this.mContext);
	}

	@Override
	public int getCount()
	{
		return imageList.size();
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		View itemView        = mLayoutInflater.inflate(R.layout.pager_item, container, false);
		
		ImageModel model     = imageList.get(position);

		ImageView imageView   = (ImageView) itemView.findViewById(R.id.imageView);
		
		final RelativeLayout header=(RelativeLayout)itemView.findViewById(R.id.header);
		
		TextView mName=(TextView)itemView.findViewById(R.id.photo_name);
		
		if(model.getImageName()!=null)
		{
			mName.setText(model.getImageName());
		}
		else
		{
			mName.setVisibility(View.INVISIBLE);
		}
		TextView mDate=(TextView)itemView.findViewById(R.id.added_date);
		if(model.getImageDate()!=null)
		{
			mDate.setText(model.getImageDate());
		}
		else
		{
			mDate.setVisibility(View.INVISIBLE);
		}

		final RelativeLayout footer=(RelativeLayout)itemView.findViewById(R.id.footer);

		TextView mLikes=(TextView)itemView.findViewById(R.id.likes);

		if(model.getImageLikes()>0)
		{
			mLikes.setText("Likes "+model.getImageLikes());
		}
		else
		{
			mLikes.setVisibility(View.INVISIBLE);
		}
		TextView mComments=(TextView)itemView.findViewById(R.id.comments);

		if(model.getImageComments()>0)
		{
			mComments.setText("Comments "+model.getImageComments());
		}
		else
		{
			mComments.setVisibility(View.INVISIBLE);
		}
		showDeatils=true;
		setView(header,footer);
		//imageloader.DisplayImage(model.getImageUrl(), imageView);
		Picasso.with(mContext).load(model.getImageUrl()).into(imageView);

		imageView.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{
				setView(header,footer);
			}
		});
		container.addView(itemView);

		return itemView;
	}
	public void setView(View header,View footer)
	{
		if(showDeatils)
		{
			header.setVisibility(View.VISIBLE);
			footer.setVisibility(View.VISIBLE);
			showDeatils=false;
		}
		else
		{
			header.setVisibility(View.INVISIBLE);
			footer.setVisibility(View.INVISIBLE);
			showDeatils=true;
		}
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		container.removeView((RelativeLayout) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view == ((RelativeLayout) object);
	}
}
