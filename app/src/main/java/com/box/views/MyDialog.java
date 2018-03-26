package com.box.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class MyDialog extends Dialog {
	public MyDialog(Context context, int layout, int style){
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		window.setGravity(Gravity.TOP);
		params.y = 60;
		params.alpha = 1.0f;
		window.setAttributes(params);
		
	}
}
