package com.socioboard.f_board_pro.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.socioboard.f_board_pro.DetailsAboutPendingLinks;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.SharlinksBroadcastReceiver;
import com.socioboard.f_board_pro.adapter.SelectAccountAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.SchPostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ShareagonLinks extends Fragment{


	View rootview;
	RelativeLayout selectDate, select_time, timerRLt, account_Rlt;
	int currenthour;

	int currentminute;

	DatePicker datePicker;
	TimePicker timePicker;

	TextView textViewCount, closewarningdialog, total_selected_links,clear_total_links;
	TextView textViewDate;
	TextView textViewTime;
	AlarmManager alarmManagers;
	Button schedule_btn;

	F_Board_LocalData database;
	ArrayList<ModelUserDatas> navDrawerItems;
	public SparseBooleanArray sparseBooleanArray;
	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();
	EditText Editbox_sharesperminute;
	int count = 0;
	int year;
	int month;
	int day;
	
	long startTime;
	TextView showpendinlinkns;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.auto_share, container, false);

		LoadAd();

		alarmManagers = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
		showpendinlinkns = (TextView) rootview.findViewById(R.id.showpendinlinkns);

		database = new F_Board_LocalData(getActivity());

		schFeedArrlist = database.getAllSchedulledShares();

		showpendinlinkns.setText("Show pending links : "+schFeedArrlist.size());

		selectDate    = (RelativeLayout) rootview.findViewById(R.id.selectDate);
		select_time   = (RelativeLayout) rootview.findViewById(R.id.select_time);
		textViewDate  = (TextView) rootview.findViewById(R.id.textView1date);
		textViewTime  = (TextView) rootview.findViewById(R.id.textView1time);
		timerRLt      = (RelativeLayout) rootview.findViewById(R.id.timerRLt);

		Editbox_sharesperminute = (EditText) rootview.findViewById(R.id.Editbox_sharesperminute);

		account_Rlt   = (RelativeLayout) rootview.findViewById(R.id.account_Rlt);
		textViewCount = (TextView) rootview.findViewById(R.id.textView1Counted);
		schedule_btn = (Button) rootview.findViewById(R.id.button1);
		total_selected_links = (TextView) rootview.findViewById(R.id.total_selected_links);
		clear_total_links    = (TextView) rootview.findViewById(R.id.clear_total_links);

		//++++++++++++++++++++++++++++++++++++
		
 		navDrawerItems = database.getAllUsersDataArlist();

		  sparseBooleanArray = new SparseBooleanArray(navDrawerItems.size());

		  for (int i = 0; i < navDrawerItems.size(); ++i) {

		   sparseBooleanArray.put(i, false);

		   if (navDrawerItems.get(i).getUserid()
		     .contains(MainSingleTon.userid)) {

		    sparseBooleanArray.put(i, true);

		    count++;
		   }
		   
		  }

		  textViewCount.setText("Selected : " + count);
			//++++++++++++++++++++++++++++++++++++
		  
		  
		//textViewCount.setText("Selected : " + 0);

		total_selected_links.setText("Total selected links :  "+MainSingleTon.shareLinks.size());

		if(MainSingleTon.shareLinks.size()==0)
		{
			//schedule_btn.getBackground().setAlpha(45);

		}else
		{

		}

		initView();

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
		
		Editbox_sharesperminute.setEnabled(false);

		showpendinlinkns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), DetailsAboutPendingLinks.class);
				startActivity(intent);
			}
		});


		clear_total_links.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainSingleTon.shareLinks.clear();
				total_selected_links.setText("Total selected links :  "+MainSingleTon.shareLinks.size());
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

						new DatePickerDialog(getActivity(), pickerListener, cyear, cmonth, cday).show();

					}
				});

			}
		});


		select_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();

				final int currenthour = c.get(Calendar.HOUR_OF_DAY);

				final int currentminute = c.get(Calendar.MINUTE);

				getActivity().runOnUiThread(new Runnable() {

					public void run() {

						TimePickerDialog tdialog = new TimePickerDialog(getActivity(), timelistner,
								currenthour, currentminute, false);

						tdialog.show();

					}
				});


			}
		});



		 
		account_Rlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				openSelectDialog();
			}
		});



		schedule_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(MainSingleTon.shareLinks.size()==0)
				{
					openWarning();
				}else
				{
					Editbox_sharesperminute.setText("1");
					if(!Editbox_sharesperminute.getText().toString().isEmpty())
					{
						int shares_perminute = Integer.parseInt(Editbox_sharesperminute.getText().toString());
						
						System.out.println("********************= "+shares_perminute);

						if(shares_perminute>0&&shares_perminute<=1)
						{
							try {
								schedulleThisPost(shares_perminute);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else
						{
							Editbox_sharesperminute.setError("value must be 1");
						}
					}
					else
					{
						Editbox_sharesperminute.setError("value must be 1");
					}
				}
			}
		});

		
		Calendar calendar = Calendar.getInstance();
		 
 		calendar.add(Calendar.MINUTE, 2);
 		
 		currentminute = calendar.get(Calendar.MINUTE);

		currenthour = calendar.get(Calendar.HOUR_OF_DAY);

		day   = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year  = calendar.get(Calendar.YEAR);

		textViewDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

		textViewTime.setText(currenthour + ":" + currentminute);

		startTime = calendar.getTimeInMillis();
	}

	private OnTimeSetListener timelistner = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			timePicker = view;

			textViewTime.setText(hourOfDay + ":" + minute);

		}
	};


	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed,  below method will be called.

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

	protected void openSelectDialog() {

		final Dialog dialog;

		dialog = new Dialog(getActivity());

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_user_select);

		//Make transpernt background dialog

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

		Window window = dialog.getWindow();

		lp.copyFrom(window.getAttributes());

		lp.width = WindowManager.LayoutParams.MATCH_PARENT;

		lp.height = WindowManager.LayoutParams.MATCH_PARENT;

		window.setAttributes(lp);

		dialog.setCancelable(true);

		ListView listView = (ListView) dialog.findViewById(R.id.listView1select);

		final SelectAccountAdapter selectAccountAdapter;

		selectAccountAdapter = new SelectAccountAdapter(count, navDrawerItems,  getActivity(), textViewCount, sparseBooleanArray);

		listView.setAdapter(selectAccountAdapter);

		Button buttonDone, cancel;

		buttonDone = (Button) dialog.findViewById(R.id.button1);
		cancel= (Button) dialog.findViewById(R.id.cancel);
		
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
				
				textViewCount.setText("Selected account : "+count);
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

		dialog.setContentView(R.layout.sharewarning);

		//Make transpernt background dialog

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


	protected void schedulleThisPost(int shares_perminute) throws ParseException {

		String arrayList = null;

		try {

			JSONObject json = new JSONObject();
			json.put("sharelinks", new JSONArray(MainSingleTon.shareLinks));
			arrayList = json.toString();

		} catch (JSONException e) {

			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();

		if (datePicker != null && timePicker != null) {

			calendar.set(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
					timePicker.getCurrentMinute(), 0);

			 startTime = calendar.getTimeInMillis();

			if (startTime > System.currentTimeMillis()) {

				if(count>0) {

					for (int i = 0; i < navDrawerItems.size(); i++) {

						if (sparseBooleanArray.get(i)) {

							long feedTime = startTime + i * 5000;

							SchPostModel schTweetModel = new SchPostModel(navDrawerItems.get(i).getUserid(), arrayList, feedTime, shares_perminute);

							database.addNewShareScheduler(schTweetModel);

							setAlarmThisPost(schTweetModel);

							MainActivity.makeToast("Posts scheduled");

						} else {
							//MainActivity.makeToast("Please select atleast one user !");
						}
					}
				}else
				{
					MainActivity.makeToast("Please select atleast one user !");
				}


			} else {
			
				MainActivity.makeToast("Date & Time should be more than current Date & Time");

			}

		} else {

			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date1 = textViewDate.getText().toString();
			String tt = textViewTime.getText().toString();
			Date date = sdf.parse(date1+""+tt);

			long startTime = date.getTime()-(5*3600000+1800000);//when convert MM-dd-yyyy HH:mm to milliseceon it return 05:30 extra time so minus (5*3600000+1800000)

			if (startTime > System.currentTimeMillis()) {

				if(count>0) {

					for (int i = 0; i < navDrawerItems.size(); i++) {


						if (sparseBooleanArray.get(i)) {

							System.out.println(i * 5000 + ">>>>>>>>>>>>>>TIME IN COMPOSE = " + startTime);

							long feedTime = startTime + i * 5000;

							System.out.println("TIMMMMEEEEEEE =" + shares_perminute);

							SchPostModel schTweetModel = new SchPostModel(navDrawerItems.get(i).getUserid(), arrayList, feedTime, shares_perminute);

							database.addNewShareScheduler(schTweetModel);

							setAlarmThisPost(schTweetModel);

							MainActivity.makeToast("Posts schedulled");

						} else {
							//MainActivity.makeToast("Please select atleast one user !");
						}

					}
				}else {
					MainActivity.makeToast("Please select atleast one user !");
				}


			} else {

				MainActivity.makeToast("Date & Time should be more than current Date & Time");
			}

		//	MainActivity.makeToast("please select desire date or time");
		}
	}

	void setAlarmThisPost(SchPostModel schTweetModel) {

		// **************************************
		Intent myIntent = new Intent(getActivity(), SharlinksBroadcastReceiver.class);

		myIntent.putExtra("respcode", schTweetModel.getFeedId());

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), schTweetModel.getFeedId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManagers.set(AlarmManager.RTC_WAKEUP,
				schTweetModel.getFeedtime(), pendingIntent);

		// **************************************
		schFeedArrlist.clear();
		schFeedArrlist = database.getAllSchedulledShares();
		showpendinlinkns.setText("Show pending links : "+schFeedArrlist.size());
		
	}

}