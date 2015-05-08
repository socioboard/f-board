package com.socioboard.f_board_pro.fragments;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.MainSingleTon;

public class Scheduler_Fragment extends Fragment
{

	private static int RESULT_LOAD_IMG = 1;
	String imgDecodableString;
	ImageView pickedImage;
	String imagePath = "";
	EditText caption;
	int year, month, day;
	TextView schedulbtn, timebtn;
	int hourOfDay, minute;
	TimePicker timePicker;;
	DatePicker datePicker;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.schedule_layout, container,
				false);

		ImageView uploadbtn = (ImageView) rootView.findViewById(R.id.bottombtn);
		ImageView sendbtn   = (ImageView) rootView.findViewById(R.id.sendbtn);
		pickedImage         = (ImageView) rootView.findViewById(R.id.image);
		caption             = (EditText) rootView.findViewById(R.id.captionText);
		schedulbtn          = (TextView) rootView.findViewById(R.id.date);
		timebtn             = (TextView) rootView.findViewById(R.id.time);

		pickedImage.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
				MainSingleTon.isfrom_schedulefrag=true;

			}
		});

		sendbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar calendar = Calendar.getInstance();

				if(datePicker!=null&&timePicker!=null)
				{

					System.out.println("-------dat");
					calendar.set(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),
					timePicker.getCurrentMinute(), 0);

					long startTime = calendar.getTimeInMillis();

					if (startTime > System.currentTimeMillis())
					{
						String captionS = "";

						captionS=  caption.getText().toString();

						if((imagePath.isEmpty())&(captionS.isEmpty()))
						{
							/** Do nothing becz user want to post empty on his wall*/
							Toast.makeText(getActivity(),"Please add atleast one content to schedule a post on your Facebook Timeline!!!", Toast.LENGTH_LONG).show();

						}else
						{
							if(!imagePath.isEmpty())
							{
								MainActivity.alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, MainActivity.pendingIntent);
								SharedPreferences sharedpref = getActivity().getSharedPreferences("SavePhoto", Context.MODE_PRIVATE);
								SharedPreferences.Editor editor=sharedpref.edit();
								editor.putString("photostring", imagePath);
								editor.putString("caption", caption.getText().toString());
								editor.putString("accestokednPref", MainSingleTon.accesstoken);
								editor.commit();
								Toast.makeText(getActivity(),"Succes", Toast.LENGTH_LONG).show();
								resetScheduler();
								
							}else
							{
								Toast.makeText(getActivity(),"Please select photo ", Toast.LENGTH_LONG).show();
							}
						}
					}

					else 
					{
						Toast.makeText(getActivity(),"picked time should be greater than current time",Toast.LENGTH_LONG).show();
					}



				}else
				{
					Toast.makeText(getActivity(),"Please set Date and Time",Toast.LENGTH_LONG).show();

					
				}

			}
		});

		schedulbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				Calendar c = Calendar.getInstance(); 
				int cyear = c.get(Calendar.YEAR);
				int cmonth=c.get(Calendar.MONTH);
				int cday=c.get(Calendar.DAY_OF_MONTH);
				new DatePickerDialog(getActivity(), pickerListener, cyear,cmonth, cday).show();

			}
		});

		timebtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Calendar c = Calendar.getInstance(); 
				int currenthour = c.get(Calendar.HOUR_OF_DAY);
				int currentminute=c.get(Calendar.MINUTE);

				System.out.println();

				TimePickerDialog tdialog=new TimePickerDialog(getActivity(), timelistner, currenthour, currentminute, false);

				tdialog.show();

			}
		});
		return rootView;
	}


public void resetScheduler() 
{

 	pickedImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.cameraicon));
 	
	caption.setText("Caption");     
	schedulbtn.setText("Date");    
	timebtn.setText("Time");  
	
}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay)
		{

			datePicker = view;

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// Show selected date
			schedulbtn.setText(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year)
					.append(" "));
		}

	};

	private TimePickerDialog.OnTimeSetListener timelistner = new OnTimeSetListener() 
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
			timePicker = view;

			timebtn.setText(hourOfDay + ":" + minute);

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && data != null)
		{
			// Let's read picked image data - its URI
			Uri pickedImageuri = data.getData();
			// Let's read picked image path using content resolver
			String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(pickedImageuri, filePath, null, null, null);
			cursor.moveToFirst();
			imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

			// Now we need to set the GUI ImageView data with data read from the
			// picked file.
			pickedImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));

			// At the end remember to close the cursor or you will end with the
			// RuntimeException!
			cursor.close();
		}

	}

	public Uri getImageUri(Context inContext, Bitmap inImage)
	{

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}


}
