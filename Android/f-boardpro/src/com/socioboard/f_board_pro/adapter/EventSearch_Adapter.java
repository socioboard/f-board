package com.socioboard.f_board_pro.adapter;

//adapter for setting pages 
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.database.util.Utilsss;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.PagesSearch_Model;

public class EventSearch_Adapter extends BaseAdapter {
	ImageLoader imageloader;
	private Context context;
	int counterText = 0;
	public ArrayList<PagesSearch_Model> pageslist;
	TextView location;

	Handler handler;

	public EventSearch_Adapter(Context context,
			ArrayList<PagesSearch_Model> pageslist) {
		this.context = context;
		this.pageslist = pageslist;
		imageloader = new ImageLoader(this.context);

		handler = new Handler();
		System.out.println("inside feed adapter constructer");
	}

	@Override
	public int getCount() {
		return pageslist.size();
	}

	@Override
	public PagesSearch_Model getItem(int position) {
		return pageslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return pageslist.indexOf(getItem(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(
					R.layout.event_search_fragment_rowitem, parent, false);
		}

		PagesSearch_Model rowItem = getItem(position);

		TextView mTitle = (TextView) convertView.findViewById(R.id.eventname);
		mTitle.setText(rowItem.getPgName());

		location = (TextView) convertView.findViewById(R.id.location);

		if (rowItem.getLocation() != null) {
			location.setText(rowItem.getLocation());
		}

		TextView mTime = (TextView) convertView
				.findViewById(R.id.start_endtime);
		String showtime =null;
		boolean istextSet =false;
		if (rowItem.getEventStartTim() != null) {

			if (rowItem.getEventEndTim() != null) {

				mTime.setText("Starts at "+getDate(rowItem
						.getEventStartTim())
						+ "....."
						+ "Ends at "+getDate(rowItem.getEventEndTim()));

				istextSet =true;
			}

		}
		if(!istextSet)
		{
			if(rowItem.getEventStartTim() == null)
			{
				showtime ="";
			}
			else
			{
				showtime ="Starts at "+getDate(rowItem.getEventStartTim());
			}
			if(rowItem.getEventEndTim()==null)
			{

			}else
			{
				showtime= showtime+"....."
						+ "Ends at "+getDate(rowItem.getEventEndTim());
			}
			mTime.setText(showtime);
		}


		ImageView pageImage = (ImageView) convertView
				.findViewById(R.id.pageImage);

		imageloader.DisplayImage("https://graph.facebook.com/" + rowItem.getPgID()
				+ "/picture?type=large", pageImage);

		return convertView;
	}

	private String getDate(String date12312) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	     Date date = new Date();
		long when = 0;
		try  
		{
			try {
				when = dateFormat.parse(date12312).getTime();
				 // Calendar cal = Calendar.getInstance();
			       ///TimeZone tz = cal.getTimeZone();//get your local time zone.
			       SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
			       //sdf.setTimeZone(tz);//set time zone.
			       String localTime = sdf.format(new Date(when));
			  
			       try {
			            date = sdf.parse(localTime);//get local date
			        } catch (ParseException e) {
			            e.printStackTrace();
			        }
				


			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		return date.toString();
	}
}
