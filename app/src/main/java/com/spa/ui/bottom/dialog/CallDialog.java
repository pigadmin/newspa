package com.spa.ui.bottom.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.spa.R;
import com.spa.app.App;

public class CallDialog {
	Context context;
	Handler handler;
	App apc;

	public CallDialog(Handler handler, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
		apc = (App) context.getApplicationContext();
	}

	static AlertDialog call_dialog;

	public void crt() {
		// TODO Auto-generated method stub
		call_dialog = new AlertDialog.Builder(context).create();
		if (call_dialog != null && call_dialog.isShowing()) {
			call_dialog.dismiss();
		} else {
			call_dialog.show();
		}
		call_dialog.setContentView(R.layout.dialog_call);

		Button call_ok = (Button) call_dialog.findViewById(R.id.ok);
		call_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();


			}
		});
		Button call_cancle = (Button) call_dialog
				.findViewById(R.id.cancle);
		call_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				close();
			}
		});

	}

	public static void close() {
		if (call_dialog != null && call_dialog.isShowing()) {
			call_dialog.dismiss();
		}
	}
}
