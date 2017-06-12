package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.share.widget.GameRequestDialog;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.FriendModel;
import com.squareup.picasso.Picasso;

public class InvitFriend_Adapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener 
{
	Context context;
	ArrayList<FriendModel> friendList;
	Handler handler= new Handler();
	public SparseBooleanArray sparseBooleanArray;
	public TextView counterText;
	public int count ;
	CallbackManager callbackManager;

	ImageLoader imageLoader;
	GameRequestDialog	 requestDialog;

	public  InvitFriend_Adapter(Context context,ArrayList<FriendModel> friendList,int count,TextView counterText, SparseBooleanArray sparseBooleanArray)
	{
		this.context=(MainActivity)context;
		this.friendList=friendList;
		this.sparseBooleanArray = sparseBooleanArray;
		this.counterText = counterText;
		this.count = count ;
		imageLoader = new ImageLoader(this.context);
		callbackManager = CallbackManager.Factory.create();
		requestDialog = new GameRequestDialog((Activity) context);
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
			convertView              = mInflater.inflate(R.layout.invite_friend_item, parent, false);
		}
		TextView name         = (TextView)convertView.findViewById(R.id.fname);
		ImageView profilePic  = (ImageView)convertView.findViewById(R.id.fpic);



		final FriendModel model = getItem(position);

		name.setText(model.getFriendName());

		//imageLoader.DisplayImage(model.getFriendPic(), profilePic);
		Picasso.with(context).load(model.getFriendPic()).into(profilePic);

		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
		checkBox.setTag(position);
		checkBox.setOnCheckedChangeListener(this);
		checkBox.setChecked(sparseBooleanArray.get(position));


		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(checkBox.isChecked())
				{
					checkBox.setChecked(false);
					count--;
					System.out.println("Unchedk count="+count);
					System.out.println("Uchedk count="+count+" NAME="+model.getFriendName());

				}else
				{
					checkBox.setChecked(true);
					count++;
					System.out.println("chedk count="+count+" NAME="+model.getFriendName());
					
				}
			}
		});

		return convertView;
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		sparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);

		if (isChecked) {


		} else {

		}
		//counterText.setText	("Selected : "+count);
	}

	
}
