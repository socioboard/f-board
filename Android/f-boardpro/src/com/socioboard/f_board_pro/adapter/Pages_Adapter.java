package com.socioboard.f_board_pro.adapter;
//adapter for setting pages 
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.socioboard.f_board_pro.models.PagesModel;

public class Pages_Adapter  extends BaseAdapter
{

	private Context context;
	private ArrayList<PagesModel> pageslist;
	private Handler handler = new Handler();

	public Pages_Adapter(Context context, ArrayList<PagesModel> pageslist) 
	{
		this.context = context;
		this.pageslist = pageslist;
		System.out.println("inside feed adapter constructer");
	}
	@Override
	public int getCount()
	{
		return pageslist.size();
	}

	@Override
	public PagesModel getItem(int position)
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


		PagesModel rowItem = getItem(position);
		TextView mTitle=(TextView) convertView.findViewById(R.id.pageNAme);
		mTitle.setText(rowItem.getPgName());

		TextView mTime=(TextView) convertView.findViewById(R.id.pagecategory);
		mTime.setText(rowItem.getPgCategory());

		ImageView pageImage = (ImageView) convertView.findViewById(R.id.pageImage);

		setImageFromUrl(pageImage, rowItem.getPgID());
		
		return convertView;
	}
	private void setImageFromUrl(final ImageView mImage, final String FBiD) {

		 
		System.out.println("IMAGE is not null= "+FBiD);

		new Thread(new Runnable() {

			@Override
			public void run() {
				 

				String FbHitUrl ="https://graph.facebook.com/"+FBiD+"/picture?type=large";
				System.out.println("FbHitUrl = "+FbHitUrl);

				final Bitmap imageBitmap = MainSingleTon.getBitmapFromURL(FbHitUrl);

				handler.post(new Runnable() {

					@Override
					public void run() {

						mImage.setImageBitmap(imageBitmap);
					}
				});


			}
		}).start();
		 
	}


}
