package com.socioboard.f_board_pro;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.socioboard.f_board_pro.adapter.SelectAccountAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.SchPostModel;

public class SchedulleComposeActivity extends Activity {

	Button schedulButton;
	EditText edttext;
	String feedString;
	F_Board_LocalData database;
	ArrayList<ModelUserDatas> navDrawerItems;
	TextView textViewCount;
	TextView textViewDate;
	TextView textViewTime;
	AlarmManager alarmManagers;
	TimePicker timePicker;
	DatePicker datePicker;
	CheckBox chkBox;
	ImageView imgimageView1Cal, ImagePost;
	ImageView imageView2Time, imageViewAddUsers;
	public SparseBooleanArray sparseBooleanArray;

	RelativeLayout selectDate, addUsersRlt,rlt, select_time,relout2;
	String imagePath = "";
	private static int RESULT_LOAD_IMG = 1;

	int count = 0;
	int year;
	int month;
	int day;
	int currenthour;

	int currentminute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.schdulle_compose);

		alarmManagers = (AlarmManager) getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);

		textViewCount = (TextView) findViewById(R.id.textView1Counted);

		imgimageView1Cal = (ImageView) findViewById(R.id.imageView1Cal);

		select_time   = (RelativeLayout) findViewById(R.id.select_time);

		selectDate = (RelativeLayout) findViewById(R.id.selectDate);
		
		relout2 = (RelativeLayout) findViewById(R.id.relout2);

		ImagePost = (ImageView) findViewById(R.id.ImagePost);

		ImagePost.setImageResource(R.drawable.upload_image);;

		imageViewAddUsers = (ImageView) findViewById(R.id.imageViewAddUsers);

		addUsersRlt =  (RelativeLayout) findViewById(R.id.addUsersRlt);

		imageView2Time = (ImageView) findViewById(R.id.imageView2Time);

		textViewDate = (TextView) findViewById(R.id.textView1date);

		textViewTime = (TextView) findViewById(R.id.textView1time);

		chkBox = (CheckBox) findViewById(R.id.checkBox1);


		rlt = (RelativeLayout) findViewById(R.id.rlt);

		rlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});


		relout2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				openSelectDialog();
			}
		});
		database = new F_Board_LocalData(getApplicationContext());
		/*
		 * textViewCount.setText("Selected : " + 0);
		navDrawerItems = database.getAllUsersDataArlist();

		sparseBooleanArray = new SparseBooleanArray(navDrawerItems.size());

		for (int i = 0; i < navDrawerItems.size(); ++i) {

			sparseBooleanArray.put(i, false);

		}*/

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

		myprint(navDrawerItems);

		addUsersRlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				openSelectDialog();

			}
		});


		ImagePost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
				MainSingleTon.isfrom_schedulefrag=true;

			}
		});



		schedulButton = (Button) findViewById(R.id.button1);

		edttext = (EditText) findViewById(R.id.editText1);

		schedulButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				feedString = edttext.getText().toString();

				/*	if (!imagePath.isEmpty()) {*/

				schedulleThisPost();

				/*}else
				{
					myToastS("You must choose photo");
				}

				 */
			}
		});

		selectDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();
				final int cyear = c.get(Calendar.YEAR);
				final int cmonth = c.get(Calendar.MONTH);
				final int cday = c.get(Calendar.DAY_OF_MONTH);

				runOnUiThread(new Runnable() {

					public void run() {

						new DatePickerDialog(SchedulleComposeActivity.this,
								pickerListener, cyear, cmonth, cday).show();

					}
				});

			}
		});

		select_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView2Time.callOnClick();

			}
		});

		imageView2Time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();

				final int currenthour = c.get(Calendar.HOUR_OF_DAY);

				final int currentminute = c.get(Calendar.MINUTE);

				runOnUiThread(new Runnable() {

					public void run() {

						TimePickerDialog tdialog = new TimePickerDialog(
								SchedulleComposeActivity.this, timelistner,
								currenthour, currentminute, false);

						tdialog.show();

					}
				});
			}
		});
		

		Calendar calendar = Calendar.getInstance();

		currenthour = calendar.get(Calendar.HOUR_OF_DAY);

		currentminute = calendar.get(Calendar.MINUTE);

		currentminute+=2;

		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);

		textViewDate.setText(new StringBuilder().append(month + 1).append("-")
				.append(day).append("-").append(year).append(" "));

		textViewTime.setText(currenthour + ":" + currentminute);

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null)
		{
			// Let's read picked image data - its URI
			Uri pickedImageuri = data.getData();
			// Let's read picked image path using content resolver

			String[] filePath = { MediaStore.Images.Media.DATA };

			Cursor cursor =  getContentResolver().query(pickedImageuri, filePath, null, null, null);

			cursor.moveToFirst();

			imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

			// Now we need to set the GUI ImageView data with data read from the
			// picked file.
			ImagePost.setImageBitmap(BitmapFactory.decodeFile(imagePath));

			// At the end remember to close the cursor or you will end with the
			// RuntimeException!

			cursor.close();
		}

	}


	protected void openSelectDialog() {

		final Dialog dialog;

		dialog = new Dialog(SchedulleComposeActivity.this);

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

		selectAccountAdapter = new SelectAccountAdapter(count, navDrawerItems,  getApplicationContext(), textViewCount, sparseBooleanArray);

		listView.setAdapter(selectAccountAdapter);

		Button buttonDone, cancel;

		cancel     = (Button) dialog.findViewById(R.id.cancel);
		buttonDone = (Button) dialog.findViewById(R.id.button1);

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

				myprint("buttonCancel");

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

	protected void schedulleThisPost() {

		// check everything filled by user or not

		Calendar calendar = Calendar.getInstance();

		if (datePicker != null && timePicker != null) {

			calendar.set(datePicker.getYear(), datePicker.getMonth(),
					datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
					timePicker.getCurrentMinute(), 0);

			long startTime = calendar.getTimeInMillis();

			if (startTime > System.currentTimeMillis()) {

				for (int i = 0; i < navDrawerItems.size(); i++) {

					if (sparseBooleanArray.get(i)) {

						System.out.println(i * 5000+">>>>>>>>>>>>>>TIME IN COMPOSE = "+startTime);

						long feedTime = startTime + i * 5000;

						myprint(i + " time = " + feedTime);

						SchPostModel schTweetModel = new SchPostModel(navDrawerItems.get(i).getUserid(),feedString, imagePath, feedTime);

						myprint(schTweetModel);

						database.addNewSchedulledTweet(schTweetModel);

						setAlarmThisPost(schTweetModel);

						myToastS("Posts Schedulled !");

						finish();

					}else
					{
						myToastS("Please select atleast one user !");
					}

				}


			} else {

				myToastS("picked Date & Time should be more than current Date & Time");

			}

		} else {

			myToastS("please select desire date or time");
		}
	}

	void myToastS(final String toastMsg) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toastMsg,
						Toast.LENGTH_SHORT).show();

			}
		});
	}

	void myToastL(final String toastMsg) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toastMsg,
						Toast.LENGTH_LONG).show();

			}
		});
	}

	public void myprint(Object msg) {

		System.out.println(msg.toString());

	}

	void setAlarmThisPost(SchPostModel schTweetModel) {

		// **************************************

		Intent myIntent = new Intent(SchedulleComposeActivity.this, SchedullerReceiver.class);

		myIntent.putExtra("respcode", schTweetModel.getFeedId());

		PendingIntent pendingIntent = PendingIntent.getBroadcast( SchedulleComposeActivity.this, schTweetModel.getFeedId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmManagers.set(AlarmManager.RTC_WAKEUP,
				schTweetModel.getFeedtime(), pendingIntent);

		// **************************************

	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed,  below method will be called.

		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			datePicker = view;

			year  = selectedYear;
			month = selectedMonth;
			day   = selectedDay;

			// Show selected date
			textViewDate.setText(new StringBuilder().append(month + 1)
					.append("-").append(day).append("-").append(year)
					.append(" "));

		}

	};

	private TimePickerDialog.OnTimeSetListener timelistner = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			timePicker = view;

			textViewTime.setText(hourOfDay + ":" + minute);

		}
	};

}
