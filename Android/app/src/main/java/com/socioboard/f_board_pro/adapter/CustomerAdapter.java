package com.socioboard.f_board_pro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends ArrayAdapter<PagesSearch_Model> {
   public  List<PagesSearch_Model> mCustomers;
    ImageLoader imageloader;
	private Context context;
	RelativeLayout contents;
	ImageView addtoPageagon;
	
	private Handler handler = new Handler();
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((PagesSearch_Model)resultValue).getPgName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            
        	FilterResults results = new FilterResults();

            if (constraint != null) {
            	
                ArrayList<PagesSearch_Model> suggestions = new ArrayList<PagesSearch_Model>();
                
                for (PagesSearch_Model PagesSearch_Model : mCustomers) {
                    // Note: change the "contains" to "startsWith" if you only want starting matches
                    if (PagesSearch_Model.getPgName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(PagesSearch_Model);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }else
            {
            	
            	System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRR");
            	  ArrayList<PagesSearch_Model> suggestions = new ArrayList<PagesSearch_Model>();
                  
                   suggestions.addAll(mCustomers);
                   results.values = suggestions;
                   results.count = suggestions.size();
                    
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                // we have filtered results
                addAll((ArrayList<PagesSearch_Model>) results.values);
            } else {
                // no filter, add entire original list back in
                addAll(mCustomers);
            }
            notifyDataSetChanged();
        }
    };

    
    public CustomerAdapter(Context context, int textViewResourceId, List<PagesSearch_Model> customers) {
        super(context, textViewResourceId, customers);
        // copy all the customers into a master list
        this.mCustomers =customers;
        this.context = context;
		imageloader =  new ImageLoader(this.context);
		System.out.println("inside feed adapter constructer");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      
    	if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.pages_rowitem, parent, false);
		}
    	
		contents      = (RelativeLayout) convertView.findViewById(R.id.contents);
		addtoPageagon = (ImageView)       convertView.findViewById(R.id.addtoPageagon);
//		addtoAutoLiker = (ImageView)convertView.findViewById(R.id.addtoAutoLiker);

		final PagesSearch_Model rowItem = getItem(position);

		addtoPageagon.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {

				if(MainSingleTon.pageShareagonList.contains(rowItem.getPgID()))
				{

					MainActivity.makeToast("Already exists in Shareegon!!");
				}else
				{
					MainSingleTon.pageShareagonList.add(rowItem.getPgID());

					MainActivity.makeToast("Added to Shareagon_Page "+MainSingleTon.pageShareagonList.size());
				}

				if(MainSingleTon.autoLikerPageList.contains(rowItem.getPgID()))
				{
					MainActivity.makeToast("Already exists in autoliker !!");
				}
				else
				{
					MainSingleTon.autoLikerPageList.add(rowItem.getPgID());
					MainSingleTon.likePgID.add(rowItem.getPgID());
					MainActivity.makeToast("Added to Autoliker_Page "+MainSingleTon.autoLikerPageList.size());
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

    @Override
    public Filter getFilter() {
        return mFilter;
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
									
									System.out.println("postion"+postion);
									System.out.println("getCount()"+getCount());
									
									if(getCount()>postion)
									{
										System.out.println("tre");
										mCustomers.get(postion).setLikesCount(num);
									}else
									{
										//
									}
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