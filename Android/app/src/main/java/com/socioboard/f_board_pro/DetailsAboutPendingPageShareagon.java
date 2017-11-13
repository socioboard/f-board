package com.socioboard.f_board_pro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socioboard.f_board_pro.adapter.SharegonPageAdapter;
import com.socioboard.f_board_pro.database.util.F_Board_LocalData;
import com.socioboard.f_board_pro.database.util.ModelUserDatas;
import com.socioboard.f_board_pro.models.SchPostModel;

import java.util.ArrayList;

public class DetailsAboutPendingPageShareagon extends Activity {
	
	F_Board_LocalData database;
	ArrayList<ModelUserDatas> navDrawerItems;
	ArrayList<SchPostModel> schFeedArrlist = new ArrayList<SchPostModel>();
	SharegonPageAdapter shareagonAdapter;
	
	ImageView backImage ;
	ListView listView1;
	TextView no_items;
	RelativeLayout headerRlt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_pending_listlinks);
		
		backImage =  (ImageView) findViewById(R.id.backImage);
	    listView1 = (ListView) findViewById(R.id.listView1);
	    no_items  = (TextView) findViewById(R.id.no_items);
	    headerRlt = (RelativeLayout) findViewById(R.id.headerRlt);
	    
		database   = new F_Board_LocalData(getApplicationContext());

		schFeedArrlist = database.getAllSchedulledPageShareagon();

		shareagonAdapter = new SharegonPageAdapter(DetailsAboutPendingPageShareagon.this, schFeedArrlist);
		
		System.out.println("###################LIST ="+schFeedArrlist.size());
		
		if(schFeedArrlist.size()==0)
		{
			no_items.setVisibility(View.VISIBLE);
			listView1.setVisibility(View.GONE);
			
		}else
		{
			no_items.setVisibility(View.INVISIBLE);
		}
		
		listView1.setAdapter(shareagonAdapter);
		
		headerRlt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		backImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
	}

}
