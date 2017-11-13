package com.socioboard.f_board_pro.adapter;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShareagonAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<SchPostModel> schTweetModels;
	private   final SimpleDateFormat monthDayYearformatter = new SimpleDateFormat(
			"MM-dd-yyyy");
	ImageLoader imageloader;
	Dialog builder;

	F_Board_LocalData database;

	public ShareagonAdapter(Context context,
			ArrayList<SchPostModel> schTweetModels) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {


		if (convertView == null) {

			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.shareagon_item, parent,
					false);
		}

		final SchPostModel schfeedModel = getItem(position);

		TextView userName = (TextView) convertView.findViewById(R.id.Username);

		TextView txtTime = (TextView) convertView.findViewById(R.id.textViewTime);

		TextView txtDate = (TextView) convertView.findViewById(R.id.textViewDate);

		TextView feedText = (TextView) convertView.findViewById(R.id.Total_links);

		TextView likesper_minute = (TextView) convertView.findViewById(R.id.likesper_minute);

		ImageView deletePost = (ImageView) convertView.findViewById(R.id.deletePost);

		monthDayYearformatter.format(schfeedModel.getFeedtime());

		Calendar calendar = monthDayYearformatter.getCalendar();
		
		userName.setText("Account Name: " + MainSingleTon.userdetails.get(schfeedModel.getUserID()).getUsername());

		txtTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + " : "
				+ calendar.get(Calendar.MINUTE));

		txtDate.setText(getDate(schfeedModel.getFeedtime()));

		likesper_minute.setText(schfeedModel.getInterval()+" link post per minute ");

		try {
			
			JSONObject json = new JSONObject(schfeedModel.getFeedText());

			JSONArray links_array = json.optJSONArray("sharelinks");

			feedText.setText("Total links selected : "+links_array.length());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		deletePost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteScheduleDialog(schfeedModel, position);
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

	public void deleteScheduleDialog(final SchPostModel  schPostModel, final int positttn)
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

				database.deleteThisSharePost(schPostModel.getFeedId());

				Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();

				schTweetModels.remove(positttn);

				//Fragment schedulerFragment = new AutoShare();

				//MainActivity.swipeFragment(schedulerFragment);
				notifyDataSetChanged();

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
