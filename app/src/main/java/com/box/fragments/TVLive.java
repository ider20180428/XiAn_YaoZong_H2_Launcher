package com.box.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.box.background.TagConfig;
import com.box.launcher.Application;
import com.box.launcher.Constant;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class TVLive extends BaseFragment {
	String TAG = "YD12";
	String TAG_CONFIG_SERVICE = "ConfigService";
	List<TagConfig> configData;

	List<Application> customApps;

	public static TVLive newInstance(int layout) {
		TVLive fragment = new TVLive();
		Bundle args = new Bundle();
		args.putInt("layout", layout);
		fragment.setArguments(args);
		
		return fragment;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = super.onCreateView(inflater, container, savedInstanceState);
		initViews();
//		setListeners();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		configData = new ArrayList<TagConfig>();
		type = Constant.TYPE_TVLIVE;

	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	
	}

	@Override
	public void initViews() {
		super.initViews();
//		gridView.setFocusable(true);

	}
	

//	@Override
//	public void setListeners() {
//
//		super.setListeners();
//
//	}


}
