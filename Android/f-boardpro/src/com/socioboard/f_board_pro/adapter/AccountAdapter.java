package com.socioboard.f_board_pro.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification.Action;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;

//adapter for setting accounts
public class AccountAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<ModelUserDatas> navDrawerItems;
	Handler handler = new Handler();
	FragmentManager fragmentManager;
	private Dialog builder1;
	
	MainActivity mainActivity =null;

	public AccountAdapter(Context context, ArrayList<ModelUserDatas> navDrawerItems) 
	{
		this.context = context;
		this.navDrawerItems = navDrawerItems;
		
		 mainActivity =(MainActivity) context;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{


		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.account_item, parent, false);
		}


		ImageView profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
		ImageView settingspic = (ImageView) convertView.findViewById(R.id.settings);

		TextView text = (TextView) convertView.findViewById(R.id.user_name);

		getBitmap(profilePic,position);

		text.setText(navDrawerItems.get(position).getUsername());
		settingspic.setImageResource(R.drawable.ic_settings);

		if(position!=0)
		{
			settingspic.setOnClickListener(new OnClickListener() {


				@Override
				public void onClick(View v) {
					builder1 = new Dialog(mainActivity);

					builder1.requestWindowFeature(Window.FEATURE_NO_TITLE);

					builder1.setContentView(R.layout.layout_dialog);

					TextView alermsg =(TextView) builder1.findViewById(R.id.dialogMessage);
					Button	yesBtn  =(Button) builder1.findViewById(R.id.yesBtn);
					Button noBtn = (Button) builder1.findViewById(R.id.noBtn);

					alermsg.setText("Are you sure want to delete \n"+navDrawerItems.get(position).getUsername()+" Account??? ");

					yesBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							F_Board_LocalData fBoardLocalData = new F_Board_LocalData(context);

							fBoardLocalData.deleteThisUserData(navDrawerItems.get(position).getUserid());

							fBoardLocalData.getAllUsersData();

							if(MainSingleTon.useridlist.get(0)!=null) 
							{
								ModelUserDatas model      =MainSingleTon.userdetails.get(MainSingleTon.useridlist.get(0));
								MainSingleTon.username    =model.getUsername();
								MainSingleTon.userimage   =model.getUserimage();
								MainSingleTon.accesstoken =model.getUserAcessToken();
								MainSingleTon.userid     = model.getUserid();

								Intent in=new Intent(context, MainActivity.class);
								in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.getApplicationContext().startActivity(in);
								
								builder1.dismiss();

							}

						}
					});

					noBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							builder1.dismiss();
						}
					});
					builder1.show();
				}
			});
		}else
		{
			settingspic.setVisibility(View.INVISIBLE);

		}
		return convertView;
	}
	public void getBitmap(final ImageView profilePic, final  int position)
	{

		new Thread(new Runnable() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					Bitmap pfofile  = MainSingleTon.getBitmapFromURL(navDrawerItems.get(position).getUserimage());
					@Override
					public void run() {

						profilePic.setImageBitmap((pfofile));
					}
				});


			}
		}).start();
	}
}
