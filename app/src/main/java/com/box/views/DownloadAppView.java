package com.box.views;

import com.box.background.TagConfig;
import com.ider.launcher.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DownloadAppView extends RelativeLayout {

	ImageView icon;
	TextView label;
	private TagConfig config;
	
	public DownloadAppView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.common_item, this);
		icon = (ImageView) findViewById(R.id.app_item_icon);
		label = (TextView) findViewById(R.id.app_item_label);
		
		
	}
	
	public void setAppConfig(TagConfig config) {
		this.config = config;
		label.setText(config.label);
		Picasso.with(getContext()).load(config.iconUrl).fit().placeholder(R.drawable.ider_replace).into(icon);
	}

	public String getLabel() {
		return config.label;
	}
	public String getDownloadUrl() {
		return config.apkUrl;
	}
	
}
