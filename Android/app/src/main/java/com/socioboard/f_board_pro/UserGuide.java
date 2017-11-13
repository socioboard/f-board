package com.socioboard.f_board_pro;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserGuide  extends Activity{
	TextView  closewarningdialog, seemore_shareagon_link, seemore_shareagon_page;
	RelativeLayout headerRlt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_guide);

		seemore_shareagon_link = (TextView) findViewById(R.id.seemore_shareagon_link);
		seemore_shareagon_page = (TextView) findViewById(R.id.seemore_shareagon_page);
		headerRlt  = (RelativeLayout) findViewById(R.id.headerRlt);

		seemore_shareagon_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				openWarning();

			}
		});

		seemore_shareagon_page.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openWarning1();

			}
		});

		headerRlt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	protected void openWarning() {

		final Dialog dialog;

		dialog = new Dialog(UserGuide.this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.sharewarning);

		// Make transpernt background dialog

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

	protected void openWarning1()
	{

		final Dialog dialog;

		dialog = new Dialog(UserGuide.this);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.sharewarning_page);

		// Make transpernt background dialog

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

		new Handler().post(new Runnable()
		{

			@Override
			public void run() {

				dialog.show();

			}
		});

	}
}
