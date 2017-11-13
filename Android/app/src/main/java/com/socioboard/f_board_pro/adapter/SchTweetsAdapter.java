package com.socioboard.f_board_pro.adapter;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.fragments.SchedulerFragment;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.SchPostModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SchTweetsAdapter extends BaseAdapter {

	private Context context;
	public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
	private ArrayList<SchPostModel> schTweetModels;
	private   final SimpleDateFormat monthDayYearformatter = new SimpleDateFormat("MM-dd-yyy HH:mm");
	ImageLoader imageloader;
	Dialog builder;

	F_Board_LocalData database;

	public SchTweetsAdapter(Context context,ArrayList<SchPostModel> schTweetModels) {
		this.context = context;
		this.schTweetModels = schTweetModels;
		imageloader =new ImageLoader(this.context);

		builder= new Dialog(this.context);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		database = new F_Board_LocalData(context);
	}

	@Override
	public int getCount() {
		return schTweetModels.size();
	}

	@Override
	public SchPostModel getItem(int position) {
		return schTweetModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		if (convertView == null) {

			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.sch_tweet_item2, parent,
					false);
		}

		final SchPostModel schfeedModel = getItem(position);

		TextView userName = (TextView) convertView.findViewById(R.id.Username);

		TextView txtTime = (TextView) convertView.findViewById(R.id.textViewTime);

		TextView txtDate = (TextView) convertView.findViewById(R.id.textViewDate);

		TextView feedText = (TextView) convertView.findViewById(R.id.feedText);

		ImageView feedImage = (ImageView) convertView.findViewById(R.id.feedImage); 

		ImageView deletePost = (ImageView) convertView.findViewById(R.id.deletePost);

		System.out.println(">>>>>>>>>>>>>>>>>>>>= "+schfeedModel.getFeedtime());

		monthDayYearformatter.format(schfeedModel.getFeedtime());

		Calendar calendar = monthDayYearformatter.getCalendar();
		System.out.println("SchTweetsAdopter----115 "+calendar.getTime().getHours());

		userName.setText("@" + MainSingleTon.userdetails.get(schfeedModel.getUserID()).getUsername());

		txtTime.setText(calendar.getTime().getHours() + " : "+ calendar.getTime().getMinutes());

		txtDate.setText(getDate(schfeedModel.getFeedtime()));

		feedText.setText(schfeedModel.getFeedText());

		if(schfeedModel.getFeedImagePath()!=null)
		{
			System.out.println("SchTweetsAdopter----127"+schfeedModel.getFeedImagePath());
			if(schfeedModel.getFeedImagePath().isEmpty())
			{

			/*	File file = new File(schfeedModel.getFeedImagePath());
				imageloader.clearCache();

				feedImage.setImageBitmap(Bitmap.createScaledBitmap(imageloader.decodeFile(file), 60, 60, true));*/
			}
			else {
				File file = new File(schfeedModel.getFeedImagePath());
				imageloader.clearCache();

				System.out.println("SchTweetsAdopter----140"+imageloader.decodeFile(file)+" "+file.getPath()+" "+file);
				feedImage.setImageBitmap(Bitmap.createScaledBitmap(imageloader.decodeFile(file), 60, 60, true));
				//Picasso.with(context).load(schfeedModel.getFeedImagePath()).into(imageloader);
			}
		}
		deletePost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteScheduleDialog(schfeedModel);
			}
		});

		return convertView;
	}

	private String getDate(long time) {
		Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		cal.setTimeInMillis(time);
		String date = DateFormat.format("dd-MM-yyyy", cal).toString();
		return date;
	}

	public void deleteScheduleDialog(final SchPostModel  schPostModel)
	{

		TextView dialogMessage;
		Button yesBtn, noBtn;


		builder.setContentView(R.layout.delete_schedule);

		dialogMessage = (TextView) builder.findViewById(R.id.dialogMessage);

		yesBtn = (Button) builder.findViewById(R.id.yesBtn);

		noBtn = (Button) builder.findViewById(R.id.noBtn);

		dialogMessage.setText("Would you like to delete this Schedule??");

		yesBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlarmManager alarmManagers;

				Intent myIntent = new Intent(context, MainActivity.class);

				myIntent.putExtra("respcode", schPostModel.getFeedId());

				alarmManagers = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, schPostModel.getFeedId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

				alarmManagers.set(AlarmManager.RTC_WAKEUP,	schPostModel.getFeedtime(), pendingIntent);

				alarmManagers.cancel(pendingIntent);

				database.deleteThisPost(schPostModel.getFeedId());

				Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

				Fragment schedulerFragment = new SchedulerFragment();

				MainActivity.swipeFragment(schedulerFragment);

				builder.dismiss();
			}
		});

		noBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				builder.dismiss();
			}
		});

		builder.show();


	}

}
