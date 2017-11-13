package com.socioboard.f_board_pro.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.DetailsAboutPendingGroupShareagon;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.SelectAccountAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.HomeFeedModel;
import com.socioboard.f_board_pro.models.SchPostModel;
import com.socioboard.f_board_pro.service_classes.GroupShareagonBroadcastReciver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class ShareagonGroup extends Fragment {

	View rootview;
	RelativeLayout selectDate, select_time, timerRLt, account_Rlt,
	cancel_schedulerRlt, service_running,add_post_schedulerRlt;

	DatePicker datePicker;
	TimePicker timePicker;

	int total_pager_minute, total_shareperMinute;
	TextView textViewCount, closewarningdialog, total_selected_links, total_selected_group,next_scheduling_time,
	clear_total_links,clear_total_groups,addPost_scheduler_image;
	TextView textViewDate;
	TextView textViewTime, completed_textview;
	AlarmManager alarmManagers;
	Button schedule_btn,runnig_button, pendingpost_button;
	List<String> arrlistSSS = Collections
			.synchronizedList(new ArrayList<String>());



	F_Board_LocalData database;
	ArrayList<ModelUserDatas> navDrawerItems;
	public SparseBooleanArray sparseBooleanArray;
	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();
	EditText Editbox_sharesperminute, toppost_countEdit;
	int count = 0;
	int year;
	int month;
	int day;
	Handler handler = new Handler();
	int complted_count, total_counts;
	int currenthour;
	boolean isShareagonGroupRunning=false;
	int shares_perminute = 1 ;
	int currentminute;

	JSONParseraa jsonParser = new JSONParseraa();
	ProgressDialog pd;

	ArrayList<String> userID_list = new ArrayList<String>();
	public Timer timersa, refreshTmer = new Timer();

	// ArrayList<String> user_accesstoken=new ArrayList<String>();
	ArrayList<String> pendingtimelist = new ArrayList<>();
	SharedPreferences sharedpreferences;
	long startTime;

	Context context;

	TextView total_pages_selected;
	TextView total_posts;
	TextView pending_shares;
	TextView start_time;

	long time=0;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		database = new F_Board_LocalData(getActivity());
		rootview = inflater.inflate(R.layout.shareagon_group, container, false);

		LoadAd();

		context = getActivity();
		total_pages_selected = (TextView) rootview.findViewById(R.id.total_pages_selected);
		total_posts = (TextView) rootview.findViewById(R.id.total_posts);
		pending_shares = (TextView) rootview.findViewById(R.id.pending_shares);
		start_time = (TextView) rootview.findViewById(R.id.start_time);
		service_running = (RelativeLayout) rootview
				.findViewById(R.id.service_runninggrp);
		cancel_schedulerRlt = (RelativeLayout) rootview.findViewById(R.id.cancel_schedulerRlt);
		add_post_schedulerRlt = (RelativeLayout) rootview.findViewById(R.id.addPost_schedulerRlt);
		completed_textview =  (TextView) rootview.findViewById(R.id.completed_textview);

		alarmManagers = (AlarmManager) getActivity().getSystemService(
				getActivity().ALARM_SERVICE);

		schFeedArrlist = database.getAllSchedulledGroupShareagon();

		selectDate = (RelativeLayout) rootview.findViewById(R.id.selectDate);
		select_time = (RelativeLayout) rootview.findViewById(R.id.select_time);
		textViewDate = (TextView) rootview.findViewById(R.id.textView1date);
		textViewTime = (TextView) rootview.findViewById(R.id.textView1time);
		timerRLt = (RelativeLayout) rootview.findViewById(R.id.timerRLt);

//		Editbox_sharesperminute = (EditText) rootview
//				.findViewById(R.id.Editbox_sharesperminute);
		toppost_countEdit = (EditText) rootview
				.findViewById(R.id.toppost_countEdit);

		//account_Rlt = (RelativeLayout) rootview.findViewById(R.id.account_Rlt);
		textViewCount = (TextView) rootview.findViewById(R.id.textView1Counted);
		schedule_btn = (Button) rootview.findViewById(R.id.buttonshgrp);
		//runnig_button = (Button)rootview.findViewById(R.id.buttonrunningserv);
		pendingpost_button = (Button) rootview.findViewById(R.id.pendingpostbutton);
		total_selected_links = (TextView) rootview
				.findViewById(R.id.total_selected_links);
		total_selected_group = (TextView)rootview.findViewById(R.id.total_selected_group);
		next_scheduling_time = (TextView)rootview.findViewById(R.id.next_scheduling_time);
		clear_total_links = (TextView) rootview
				.findViewById(R.id.clear_total_links);
		clear_total_groups = (TextView)rootview.findViewById(R.id.clear_total_group);
		addPost_scheduler_image = (TextView) rootview.findViewById(R.id.addPost_scheduler_image);


		initView();

		pendingpost_button.setText("Show pending links :"+schFeedArrlist.size());
		// ++++++++++++++++++++++++++++++++++++

		navDrawerItems = database.getAllUsersDataArlist();

		sparseBooleanArray = new SparseBooleanArray(navDrawerItems.size());

		for (int i = 0; i < navDrawerItems.size(); ++i)
		{

			sparseBooleanArray.put(i, false);

			if (navDrawerItems.get(i).getUserid()
					.contains(MainSingleTon.userid)) {

				sparseBooleanArray.put(i, true);

				count++;
			}

		}

		//textViewCount.setText("Selected : " + count);
		// ++++++++++++++++++++++++++++++++++++

		// textViewCount.setText("Selected : " + 0);

		total_selected_links.setText("Total selected pages :  "
				+ MainSingleTon.pageShareagonList.size());

		total_selected_group.setText("Total selected groups : "+MainSingleTon.groupsharegonList.size());

		if (MainSingleTon.pageShareagonList.size() == 0) {

			//schedule_btn.setAlpha(45);

		}

		return rootview;
	}


	void LoadAd()
	{
		MobileAds.initialize(getActivity(), getString(R.string.adMob_app_id));
		AdView mAdView = (AdView) rootview.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

	}

	private void initView() {
		pd = new ProgressDialog(getActivity());
		pd.setMessage("loading");
		pd.setCancelable(false);

		sharedpreferences = getActivity().getSharedPreferences("FacebookBoardShareagongrp", Context.MODE_PRIVATE);

		isShareagonGroupRunning = sharedpreferences.getBoolean(
				"isShareagonGroupRunning", false);


//		runnig_button.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				service_running.setVisibility(View.VISIBLE);
//			}
//		});

//This is used to show date in ShaeagonGroup Module
		if(schFeedArrlist.size()>0)
		{
			next_scheduling_time.setText("Next Scheduling Time After : "+getDate(schFeedArrlist.get(schFeedArrlist.size() - 1).getFeedtime() + (1 * (schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count()) * 1000 * 60 + 60000),"yyyy-MM-dd hh:mm:ss"));
		}

		clear_total_links.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainSingleTon.pageShareagonList.clear();
				total_selected_links.setText("Total selected pages :  "
						+ MainSingleTon.pageShareagonList.size());
			}
		});

		clear_total_groups.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainSingleTon.groupsharegonList.clear();
				total_selected_group.setText("Total selected groups : "+MainSingleTon.groupsharegonList.size());
				MainSingleTon.groupsharegonNameList.clear();//clear name of group store in groupshareagonNameList
			}
		});

		cancel_schedulerRlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sharedpreferences = getActivity().getSharedPreferences("FacebookBoardShareagongrp",
						Context.MODE_PRIVATE);

				sharedpreferences.edit().putBoolean("isShareagonGroupRunning", false).commit();
				database.deleteThisShareGroup(sharedpreferences.getInt("respcodepageagon", 12333));
				sharedpreferences.edit().clear().commit();

			}
		});

		add_post_schedulerRlt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				service_running.setVisibility(View.INVISIBLE);
			}
		});


		pendingpost_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), DetailsAboutPendingGroupShareagon.class);
				startActivity(intent);

				if(schFeedArrlist.size()>0)
				{
					next_scheduling_time.setText("Next Scheduling Time After : "+getDate(schFeedArrlist.get(schFeedArrlist.size() - 1).getFeedtime() + (1 * (schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count()) * 1000 * 60 + 60000),"yyyy-MM-dd hh:mm:ss"));
				}
			}
		});

		selectDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();
				final int cyear = c.get(Calendar.YEAR);
				final int cmonth = c.get(Calendar.MONTH);
				final int cday = c.get(Calendar.DAY_OF_MONTH);

				getActivity().runOnUiThread(new Runnable() {

					public void run() {

						new DatePickerDialog(getActivity(), pickerListener,
								cyear, cmonth, cday).show();

					}
				});

			}
		});

		select_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getActivity().runOnUiThread(new Runnable() {

					public void run() {

						TimePickerDialog tdialog = new TimePickerDialog(
								getActivity(), timelistner, currenthour,
								currentminute, false);

						tdialog.show();

					}
				});

			}
		});

		schedule_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{

				if (MainSingleTon.pageShareagonList.size() == 0 || MainSingleTon.groupsharegonList.size()==0)
				{
					openWarning();
				}
				else
				{
					if (!toppost_countEdit.getText().toString().isEmpty())
					{
						int toppost_Count = Integer
									.parseInt(toppost_countEdit.getText()
											.toString());

						if (toppost_Count >= 5 && toppost_Count <= 150)
						{

							total_shareperMinute = shares_perminute;
									
							System.out.println("shares_perminute**********************"+shares_perminute);
									
							total_pager_minute = toppost_Count;
									
							Calendar calendar = Calendar.getInstance();

							if (datePicker != null && timePicker != null)
							{
								calendar.set(datePicker.getYear(),
										datePicker.getMonth(),
										datePicker.getDayOfMonth(),
										timePicker.getCurrentHour(),
										timePicker.getCurrentMinute(),
										0);

								startTime = calendar.getTimeInMillis();

								if (schFeedArrlist.size() > 0)
								{
									time = schFeedArrlist.get(schFeedArrlist.size() - 1).getFeedtime() + ((schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count()) * 1 * 1000 * 60 + 60000);
								}

								if (startTime >= time) {

									if (startTime > System
											.currentTimeMillis()) {
										sharedpreferences
												.edit()
												.putInt("total_share_links",
														0).commit();
										sharedpreferences
												.edit()
												.putInt("completed_links",
														0).commit();

										new GetPagePostAsyncTask()
												.execute();

										//runnig_button.setText("Time : "+getDate((startTime + (total_pager_minute * count * 1000 * 60 + 60000)), "yyyy-MM-dd hh:mm:ss"));

									} else {
										MainActivity.makeToast("Date & Time should be more than current Date & Time");
									}
								} else {

									final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

									alert.setTitle("Date & Time Error");

									alert.setMessage("Scheduling Time Must Be Above Of: \n" + (getDate(time, "yyyy-MM-dd hh:mm:ss")));

									alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											alert.setCancelable(true);
										}
									});

									alert.show();
								}

							}
							else
							{
								SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");

								sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

								String date1 = textViewDate.getText().toString();

								String tt = textViewTime.getText().toString();

								Date date = null;

								try {
										date = sdf.parse(date1+""+tt);

									} catch (ParseException e)
										{
											e.printStackTrace();
										}

								startTime = date.getTime()-(5*3600000+1800000);//when convert MM-dd-yyyy HH:mm to milliseceon it return 05:30 extra time so minus (5*3600000+1800000)

								if(schFeedArrlist.size()>0)
								{
									time = schFeedArrlist.get(schFeedArrlist.size()-1).getFeedtime()+((schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count()) * 1 * 1000 * 60 + 60000);
								}

								if(startTime>=time)
								{

									if (startTime > System
													.currentTimeMillis())
									{

										sharedpreferences
														.edit()
														.putInt("total_share_links",
																0).commit();

										sharedpreferences
														.edit()
														.putInt("completed_links",
																0).commit();


										new GetPagePostAsyncTask()
														.execute();

										//runnig_button.setText("Time : "+getDate((startTime + (total_pager_minute * count * 1000 * 60 + 60000)), "yyyy-MM-dd hh:mm:ss"));

									} else {
												MainActivity.makeToast("Date & Time should be more than current Date & Time");
											}
								}else
								{
									final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
									alert.setTitle("Date & Time Error");
									alert.setMessage("Scheduling Time Must Be Above Of :\n "+(getDate(time,"yyyy-MM-dd hh:mm:ss")));
									alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
												alert.setCancelable(true);
												}
											});

									alert.show();
								}

							}

						} else {
									toppost_countEdit.setError("Enter a number between 5 to 150");
								}
					} else {
							toppost_countEdit
							.setError("Enter a number between 5 to 150");
						}
				}
			}
		});

		Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.MINUTE, 2);

		currentminute = calendar.get(Calendar.MINUTE);

		currenthour = calendar.get(Calendar.HOUR_OF_DAY);

		day = calendar.get(Calendar.DAY_OF_MONTH);

		month = calendar.get(Calendar.MONTH);

		year = calendar.get(Calendar.YEAR);

		textViewDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

		textViewTime.setText(currenthour + ":" + currentminute);

		startTime = calendar.getTimeInMillis();

	}

	private String getDate(long milliSeconds, String dateFormat)
	{
		// Create a DateFormatter object for displaying date in specified format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	private OnTimeSetListener timelistner = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			timePicker = view;

			textViewTime.setText(hourOfDay + ":" + minute);

		}
	};

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.

		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			datePicker = view;

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			textViewDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}

	};

	protected void openSelectDialog() {//used to select account

		final Dialog dialog;

		dialog = new Dialog(getActivity());

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_user_select);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

		Window window = dialog.getWindow();

		lp.copyFrom(window.getAttributes());

		lp.width = WindowManager.LayoutParams.MATCH_PARENT;

		lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		window.setAttributes(lp);

		dialog.setCancelable(true);

		ListView listView = (ListView) dialog
				.findViewById(R.id.listView1select);

		final SelectAccountAdapter selectAccountAdapter;

		selectAccountAdapter = new SelectAccountAdapter(count, navDrawerItems,
				getActivity(), textViewCount, sparseBooleanArray);

		listView.setAdapter(selectAccountAdapter);

		Button buttonDone, cancel;

		buttonDone = (Button) dialog.findViewById(R.id.button1);
		cancel = (Button) dialog.findViewById(R.id.cancel);

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.cancel();

			}
		});
		buttonDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sparseBooleanArray = selectAccountAdapter.sparseBooleanArray;

				count = selectAccountAdapter.count;

				dialog.cancel();

			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				count = 0;

				for (int i = 0; i < navDrawerItems.size(); ++i) {

					if (sparseBooleanArray.get(i)) {

						++count;
					}

				}

				textViewCount.setText("Selected account : " + count);
			}
		});

		new Handler().post(new Runnable() {

			@Override
			public void run() {

				dialog.show();

			}
		});

	}

	protected void openWarning() {

		final Dialog dialog;

		dialog = new Dialog(getActivity());

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.sharewarning_page);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

		Window window = dialog.getWindow();

		lp.copyFrom(window.getAttributes());

		lp.width = WindowManager.LayoutParams.MATCH_PARENT;

		lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		window.setAttributes(lp);

		dialog.setCancelable(true);

		closewarningdialog = (TextView) dialog.findViewById(R.id.close);

		closewarningdialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		new Handler().post(new Runnable() {

			@Override
			public void run() {

				dialog.show();

			}
		});

	}

	protected void schedulleThisPost(int shares_perminute, int toppost_count)
	{

		String arrayList = null;
		String name="";
        String groupName = null;
		groupName = MainSingleTon.groupsharegonNameList.toString();

		for (int i = 0; i < navDrawerItems.size(); i++) {

			userID_list.add(navDrawerItems.get(i).getUserid());
			if(sparseBooleanArray.get(i))
			{
				name=name+" "+MainSingleTon.userdetails.get(navDrawerItems.get(i).getUserid()).getUsername();
			}
		}

		try {

			JSONObject json = new JSONObject();
			json.put("sharelinks", new JSONArray(arrlistSSS));
			json.put("userlist", new JSONArray(userID_list));
			json.put("groupid", new JSONArray(MainSingleTon.groupsharegonList));
			arrayList = json.toString();

		} catch (JSONException e) {

			e.printStackTrace();
		}

		MainActivity.makeToast("Post Schedule");

		SchPostModel schTweetModel = new SchPostModel(name, arrayList, startTime, shares_perminute, toppost_count, groupName);

		database.addNewGroupShareagon(schTweetModel);

		setAlarmThisPost(schTweetModel);

		schFeedArrlist = database.getAllSchedulledGroupShareagon();

		pendingpost_button.setText("Show pending links :"+schFeedArrlist.size());

		if(schFeedArrlist.size()>0)
		{
			next_scheduling_time.setText("Next Scheduling Time After : "+getDate(schFeedArrlist.get(schFeedArrlist.size() - 1).getFeedtime() + ((schFeedArrlist.get(schFeedArrlist.size()-1).getTotal_count()) * 1 * 1000 * 60 + 60000),"yyyy-MM-dd hh:mm:ss"));
		}
	}

	void setAlarmThisPost(SchPostModel schTweetModel) {

		// **************************************

		System.out.println("shares_perminute==============="+schTweetModel.getInterval());
		Intent myIntent = new Intent(getActivity(), GroupShareagonBroadcastReciver.class);
		myIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
		myIntent.putExtra("respcode", schTweetModel.getFeedId());

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
				schTweetModel.getFeedId(), myIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManagers.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);

		// **************************************
		schFeedArrlist.clear();

		schFeedArrlist = database.getAllSchedulledGroupShareagon();

			sharedpreferences.edit().putInt("completed_links", 0).commit();
			sharedpreferences.edit().putInt("total_share_links", arrlistSSS.size()).commit();
			sharedpreferences.edit().putInt("total_pages_select",MainSingleTon.pageShareagonList.size()).commit();
			sharedpreferences.edit().putInt("respcodepageagon", schTweetModel.getFeedId()).commit();
			sharedpreferences.edit().putLong("startTime", startTime).commit();
			sharedpreferences.edit().putBoolean("isShareagonGroupRunning", true).commit();
			sharedpreferences.edit().putBoolean("nextpostrunning", false);
			schedullerInner();

	}


	class GetPagePostAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetSharelinksStoreInArraylist(total_pager_minute,
						total_shareperMinute);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			schedulleThisPost(total_shareperMinute, total_pager_minute);

			pd.dismiss();
		}

	}

	private void GetSharelinksStoreInArraylist(final int pagelimit,
			int shareperMinute) throws InterruptedException {

		System.out.println("arrlistSSS--------------" + arrlistSSS.size());

		arrlistSSS.clear();

		List<Thread> threadslist = new ArrayList<Thread>();

		for (int i = 0; i < MainSingleTon.pageShareagonList.size(); ++i) {

			System.out.println("pageShareagonList"
					+ MainSingleTon.pageShareagonList.size());

			final int indx = i;

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {

					String hitURL = null;

					try {
						hitURL = "https://graph.facebook.com/"
								+ MainSingleTon.pageShareagonList.get(indx)
								+ "/feed?fields=permalink_url&limit=" + pagelimit + "&access_token="
								+ MainSingleTon.accesstoken;

						System.out.println("HITTTTTTMEEEEEFEED1234" + hitURL);

						JSONObject jsonObject = jsonParser
								.getJSONFromUrl(hitURL);

						JSONArray jsonArray = jsonObject.getJSONArray("data");

						System.out.println("jsonarray length " + indx + " "
								+ jsonArray);

						if (jsonArray.length() != 0) {



							for (int j = 0; j < jsonArray.length(); j++) {
								HomeFeedModel feedModel = new HomeFeedModel();

								final JSONObject jsonObject2 = jsonArray
										.getJSONObject(j);

								if (jsonObject2.has("id")) {
									feedModel.setFeedId(jsonObject2
											.getString("id"));
								}

								if (jsonObject2.has("permalink_url")) {

									arrlistSSS.add(jsonObject2
											.getString("permalink_url"));
								} else {
									if (jsonObject2.has("actions")) {
										try {

											JSONArray jsonArray2 = jsonObject2
													.getJSONArray("actions");

											feedModel.setSharelink(jsonArray2
													.getJSONObject(2)
													.getString("link"));

											if (arrlistSSS.contains(jsonArray2
													.getJSONObject(2)
													.getString("link"))) {
												System.out
												.println("SAMEEEE******************");

											} else {
												System.out
												.println("NOT SAME******************");

												arrlistSSS.add(jsonArray2
														.getJSONObject(2)
														.getString("link"));
											}

										} catch (Exception e) {

											e.printStackTrace();
										}
									}
								}

							}

						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}

			});
			t.start();
			threadslist.add(t);

		}

		for (int i = 0; i < threadslist.size(); i++) {

			((Thread) threadslist.get(i)).join();

		}

		Collections.shuffle(arrlistSSS);
	}

	void schedullerInner() {

		refreshTmer.cancel();

		refreshTmer = new Timer();

		refreshTmer.schedule(new TimerTask() {

			@Override
			public void run() {

				handler.post(new Runnable() {

					@Override
					public void run() {

						System.out.println("share page timer");
						SharedPreferences lisharedpref = context
								.getSharedPreferences("FacebookBoardShareagongrp",
										Context.MODE_PRIVATE);

						if(lisharedpref.getBoolean("nextpostrunning",false)) {

						}

						complted_count = lisharedpref.getInt("completed_links",
								0);

						total_counts = lisharedpref.getInt("total_share_links",
								0);

						isShareagonGroupRunning = lisharedpref.getBoolean(
								"isShareagonGroupRunning", false);

						System.out.println("issharegonGouprRunning"+isShareagonGroupRunning);

						if (isShareagonGroupRunning) {

							System.out.println("Sharegon page is running");

							//service_running.setVisibility(View.VISIBLE);

							complted_count = lisharedpref.getInt(
									"completed_links", 0);
							total_counts = lisharedpref.getInt(
									"total_share_links", 0);

							total_pages_selected.setText("Total Pages assigned : "+lisharedpref.getInt(
									"total_pages_select", 0));
							total_posts.setText("Total Posts assigned : "+lisharedpref.getInt(
									"total_share_links", 0) + "");
							pending_shares
							.setText("Pending share : "+(total_counts - complted_count)
									+ "");

							completed_textview.setText("Shared : "+complted_count);
							
							if(lisharedpref.getBoolean("isShareagonGroupStarted", false))
							{
								start_time.setText("Started at : "
										+ DateUtils.getRelativeTimeSpanString(
												lisharedpref.getLong("startTime", 5454),
												System.currentTimeMillis(), 0));
							}
							else
							{
								start_time.setText("Starts at : "+ usingDateAndCalendar(lisharedpref.getLong("startTime", 5454)));
							}
							
							//schedule_btn.setEnabled(false);
						} else {

							System.out.println("Sharegon page is not running");

							service_running.setVisibility(View.GONE);

							complted_count = lisharedpref.getInt(
									"completed_links", 0);
							total_counts = lisharedpref.getInt(
									"total_share_links", 0);

							total_pages_selected.setText(lisharedpref.getInt(
									"total_pages_select", 0)+"");
							total_posts.setText(lisharedpref.getInt(
									"total_share_links", 0)+"");
							pending_shares
							.setText((total_counts - complted_count)
									+ "");
							start_time.setText("");
							schedule_btn.setEnabled(true);
						}
//						if (total_counts == complted_count) {
//
//							service_running.setVisibility(View.GONE);
//
//							lisharedpref.edit().clear().commit();
//
//						} else {
//
//						}
					}
				});

			}
		}, 0, 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		refreshTmer.cancel();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();
		schedullerInner();
	}

	private String usingDateAndCalendar(long input){
		Date date = new Date(input);
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return(cal.get(Calendar.YEAR)
				+ "/" + cal.get(Calendar.MONTH)
				+ "/" + cal.get(Calendar.DATE)
				+ " " + cal.get(Calendar.HOUR)
				+ ":" + cal.get(Calendar.MINUTE)
				+ ":" + cal.get(Calendar.SECOND)
				+ (cal.get(Calendar.AM_PM)==0?"AM":"PM")
				);

	}
	public class RunGraphRequest extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			System.out.println("started running888888888888888");
		}

		@Override
		protected String doInBackground(String... params1) {

			String postLink = params1[0];
			String MYAccessToken = params1[1];
			int postInterval = Integer.parseInt(params1[2]);
             
			String groupid=params1[3];
			
			System.out.println("groupid "+groupid);
			System.out.println("myacess tokekn " + MYAccessToken);
			System.out.println("postLink " + postLink);

			Bundle params = new Bundle();

			if (postLink.isEmpty()) {

				/** Do nothing becz user just want to post text not an image */

			} else {

				params.putString("link", postLink); // image to post
			}
			params.putString(AccessToken.ACCESS_TOKEN_KEY, MYAccessToken);

			new GraphRequest(MainSingleTon.dummyAccesstoken, "/"+groupid+"/feed", params,

					HttpMethod.POST, new GraphRequest.Callback() {

				@Override
				public void onCompleted(GraphResponse response) {
					
					System.out.println("response>>>>>>>>"+response);
					System.out.println("Scheduled response="
							+ response.getJSONObject());

					if (response.getJSONObject() != null) {
						
						if (response.getJSONObject().has("id")) {
							System.out.println("sucess");
							
						}
					}

				}

			}).executeAndWait();
 

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}

	}
}
