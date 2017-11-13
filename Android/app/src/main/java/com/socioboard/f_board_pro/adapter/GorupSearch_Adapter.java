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

import java.util.ArrayList;

public class GorupSearch_Adapter  extends BaseAdapter
{
	ImageLoader imageloader;
	private Context context;
	private ArrayList<PagesSearch_Model> pageslist;
	
	Handler handler ;

	public GorupSearch_Adapter(Context context, ArrayList<PagesSearch_Model> pageslist) 
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
			convertView = mInflater.inflate(R.layout.group_search_fragment_rowitem, parent, false);
		}

		PagesSearch_Model rowItem = getItem(position);
		TextView mTitle=(TextView) convertView.findViewById(R.id.pageNAme);
		mTitle.setText(rowItem.getPgName());
		
		TextView likesCount = (TextView) convertView.findViewById(R.id.likesCount);
		
		if(rowItem.getLikesCount()!=null)
		{
			likesCount.setText(rowItem.getLikesCount()+" Group");
		}else
		setDetails(rowItem.getPgID(), likesCount, position);

		TextView mTime=(TextView) convertView.findViewById(R.id.pagecategory);
		mTime.setText(rowItem.getPgCategory());

		ImageView pageImage = (ImageView) convertView.findViewById(R.id.pageImage);

		//imageloader.DisplayImage("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large", pageImage);
		Picasso.with(context).load("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large").into(pageImage);
		

		return convertView;
	}
	public void setDetails(final String pageID, final TextView counterTxtView , final int postion)
	{
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				String hitURL = "https://graph.facebook.com/?ids="+pageID+"&access_token="+MainSingleTon.accesstoken;
				 
				JSONParseraa jsonParser = new JSONParseraa();

				JSONObject jsonObject = jsonParser.getJSONFromUrl(hitURL);

				//System.out.println("---------setDetails------------------inside SsSSSessearch= "+jsonObject);

				try {
					JSONObject jsonObject2 =  jsonObject.getJSONObject(pageID);

					if(jsonObject2.has("privacy"))
					{
						final String countertext = jsonObject2.getString("privacy") ;
						
						handler.post(new Runnable() {

							@Override
							public void run() {
								
								if(countertext==null)
								{
									//do nothing
									//likesCount.setVisibility(View.INVISIBLE);
									
								}else
								{ 
									counterTxtView.setText(countertext+" Group");
									//System.out.println("counterText===="+counterText);
								}

							}
						});
					}else
					{
						((Activity)context).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								counterTxtView.setText(" ");
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
