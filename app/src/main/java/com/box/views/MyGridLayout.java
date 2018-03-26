package com.box.views;

import java.util.List;

import com.box.launcher.Application;
import com.ider.launcher.R;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

public class MyGridLayout extends GridLayout {
	String TAG = "YD12";
	
	int[] backgrounds = {R.drawable.default_back1,
			 R.drawable.default_back2,
			 R.drawable.default_back3,
			 R.drawable.default_back4,
			 R.drawable.default_back5,
			 R.drawable.default_back6,
			 R.drawable.default_back7,};

	public MyGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridLayout(Context context) {
		super(context);
	}
	
	
	public void setApplications(List<Application> apps) {
		
		for(int i = 0; i < apps.size(); i++) {
			ATool tool = new ATool(getContext());
			LayoutParams lp = new LayoutParams();
			lp.width = 240;
			lp.height = 156;
			lp.setMargins(0, 0, 8, 8);
			tool.setLayoutParams(lp);
			tool.setApplicaton(apps.get(i));
			tool.setBackgroundResource(backgrounds[i % backgrounds.length]);
			addView(tool);
		}
		
		
	}
	
	public void addApplication(Application app) {
		
	}
	
	
	
}
