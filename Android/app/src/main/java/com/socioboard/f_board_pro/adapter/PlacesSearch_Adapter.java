package com.socioboard.f_board_pro.adapter;
//adapter for setting pages 
import android.app.Activity;
import android.content.Context;
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
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.PagesSearch_Model;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PlacesSearch_Adapter  extends BaseAdapter
{
	ImageLoader imageloader;
	private Context context;
	int counterText =0;
	public ArrayList<PagesSearch_Model> pageslist;
	TextView likesCount, mWereHere ;

	Handler handler ;

	public PlacesSearch_Adapter(Context context, ArrayList<PagesSearch_Model> pageslist) 
	{
		this.context = context;
		this.pageslist = pageslist;
		imageloader =new ImageLoader(this.context);
		
		handler = new Handler();
		System.out.println("inside feed adapter constructer");
	}
	@Override
	public int getCount()
	{
		return pageslist.size();
	}

	@Override
	public PagesSearch_Model getItem(int position)
	{
		return pageslist.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return pageslist.indexOf(getItem(position));
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.places_search_fragment_rowitem, parent, false);
		}

		PagesSearch_Model rowItem = getItem(position);
		
		
		TextView mTitle=(TextView) convertView.findViewById(R.id.pageNAme);
		mTitle.setText(rowItem.getPgName());

		likesCount = (TextView) convertView.findViewById(R.id.likebtn);
		mWereHere  = (TextView) convertView.findViewById(R.id.wereHere);
		
		if(rowItem.getLikesCount()!=null)
		{
			likesCount.setText(rowItem.getLikesCount()+" likes");
		}else
		setDetails(rowItem.getPgID(), likesCount, mWereHere, position);
		
		TextView placeCategory = (TextView) convertView.findViewById(R.id.placeCategory);
		
		placeCategory.setText(rowItem.getPgCategory());
		
		TextView mlocation = (TextView) convertView.findViewById(R.id.location);

		mlocation.setText(rowItem.getLocation());

		ImageView pageImage = (ImageView) convertView.findViewById(R.id.pageImage);

		//imageloader.DisplayImage("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large", pageImage);
		Picasso.with(context).load("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large").into(pageImage);

		return convertView;
	}

	public void setDetails(final String pageID, final TextView counterTxtView ,final TextView werehereTxtV ,final int postion)
	{

		new Thread(new Runnable() {

			@Override
			public void run() {

				String hitURL = "https://graph.facebook.com/?ids="+pageID+"&access_token="+MainSingleTon.accesstoken;

				JSONParseraa jsonParser = new JSONParseraa();

				JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

				 System.out.println("---------setDetails------------------inside SsSSSessearch= "+jsonObject);

				try {
					JSONObject jsonObject2 =  jsonObject.getJSONObject(pageID);

					if(jsonObject2.has("likes"))
					{
						final int countertext = jsonObject2.getInt("likes") ;
						
						handler.post(new Runnable() {

							@Override
							public void run() {
								
								if(countertext==0)
								{
									//do nothing
									//likesCount.setVisibility(View.INVISIBLE);
									
								}else
								{
									String num = "";
									DecimalFormat formatter = new DecimalFormat("#,###,###");

									num = 	formatter.format(countertext);
									pageslist.get(postion).setLikesCount(num);
									counterTxtView.setText(num+" like this");
									System.out.println("counterText===="+counterText);
								}

							}
						});
					}else
					{
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {

								counterTxtView.setText("0 like this");
							}
						});

					}
					
					if(jsonObject2.has("were_here_count"))
					{

						final int countertext = jsonObject2.getInt("were_here_count") ;
						
						handler.post(new Runnable() {

							@Override
							public void run() {
								
								if(countertext==0)
								{
									//do nothing
									//likesCount.setVisibility(View.INVISIBLE);
									
								}else
								{
									String num = "";
									DecimalFormat formatter = new DecimalFormat("#,###,###");

									num = 	formatter.format(countertext);
									pageslist.get(postion).setLikesCount(num);
									werehereTxtV.setText(num+" were here");
									//System.out.println("counterText===="+counterText);
								}

							}
						});
					
					}else
					{
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {

								werehereTxtV.setText("0 were here");
							}
						});

					}


				} catch (JSONException e) {

					e.printStackTrace();
				}


				
			}
		}).start();


	}
}
