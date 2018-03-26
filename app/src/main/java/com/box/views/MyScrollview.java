package com.box.views;

import android.content.Context;
import android.widget.ScrollView;

public class MyScrollview extends ScrollView{

	public MyScrollview(Context context) {
		super(context);
		scrollTo(0,0);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}

	
	
}
