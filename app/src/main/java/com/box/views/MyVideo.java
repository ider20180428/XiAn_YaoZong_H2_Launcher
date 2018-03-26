package com.box.views;

import com.ider.launcher.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class MyVideo extends FrameLayout{

	public MyVideo(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.view_video, this);
	}

}
