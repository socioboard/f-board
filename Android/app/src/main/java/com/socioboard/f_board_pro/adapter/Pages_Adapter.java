package com.socioboard.f_board_pro.adapter;
//adapter for setting pages 
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.AllInOneSearchFeeds;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.imagelib.ImageLoader;
import com.socioboard.f_board_pro.models.PagesSearch_Model;
import com.squareup.picasso.Picasso;

public class Pages_Adapter  extends BaseAdapter
{
	ImageLoader imageloader;
	private Context context;
	private ArrayList<PagesSearch_Model> pageslist;
	private Handler handler = new Handler();
	RelativeLayout contents;
	ImageView addtoPageagon;

	public Pages_Adapter(Context context, ArrayList<PagesSearch_Model> pageslist) 
	{
		this.context = context;
		this.pageslist = pageslist;
		imageloader =new ImageLoader(this.context);
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
			convertView = mInflater.inflate(R.layout.pages_rowitem, parent, false);
		}
		contents      = (RelativeLayout) convertView.findViewById(R.id.contents);
		addtoPageagon = (ImageView)       convertView.findViewById(R.id.addtoPageagon);


		final PagesSearch_Model rowItem = getItem(position);

		addtoPageagon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(MainSingleTon.pageShareagonList.contains(rowItem.getPgID()))
				{

					MainActivity.makeToast("Already exists !!");
				}else
				{
					MainSingleTon.pageShareagonList.add(rowItem.getPgID());

					MainActivity.makeToast("Added to Shareagon_Page "+MainSingleTon.pageShareagonList.size());
				}
			}
		});

		contents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainSingleTon.pgID        = rowItem.getPgID();
				MainSingleTon.pgCategory  = rowItem.getPgCategory();
				MainSingleTon.pgNAME      = rowItem.getPgName();

				Intent intent = new Intent(context, AllInOneSearchFeeds.class);

				context.startActivity(intent);
			}
		});


		TextView mTitle=(TextView) convertView.findViewById(R.id.pageNAme);
		mTitle.setText(rowItem.getPgName());


		TextView likebtn = (TextView) convertView.findViewById(R.id.likebtn);
		TextView mTime=(TextView) convertView.findViewById(R.id.pagecategory);
		mTime.setText(rowItem.getPgCategory());

		ImageView pageImage = (ImageView) convertView.findViewById(R.id.pageImage);

		//imageloader.DisplayImage("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large", pageImage);
		Picasso.with(context).load("https://graph.facebook.com/"+rowItem.getPgID()+"/picture?type=large").into(pageImage);

		//setImageFromUrl(pageImage, rowItem.getPgID());

		if(rowItem.getLikesCount()!=null)
		{
			likebtn.setText(rowItem.getLikesCount()+" like this");
		}else
			setDetails(rowItem.getPgID(), likebtn  , position);

		return convertView;
	}

	public void setDetails(final String pageID, final TextView counterTxtView ,final int postion)
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
									//System.out.println("counterText===="+counterText);
								}

							}
						});
					}else
					{
						counterTxtView.setText("0 like this");
					}


				} catch (JSONException e) {

					e.printStackTrace();
				}



			}
		}).start();


	}


}
