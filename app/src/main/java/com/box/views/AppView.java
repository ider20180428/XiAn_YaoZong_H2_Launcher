package com.box.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.ider.launcher.R;

import java.io.File;
import java.util.List;

import box.database.DatabaseManager;
import box.utils.PreferenceManager;

public class AppView extends RelativeLayout {
	String title;
	ImageView icon;
	TextView label;
	Application app;
	File apkFile;
	DatabaseManager dbManager;
	PreferenceManager preManager;
	boolean hasleft = false;

	public AppView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.add_item, this);
		icon = (ImageView) findViewById(R.id.app_item_icon);
		label = (TextView) findViewById(R.id.app_item_label);
		preManager = PreferenceManager.getInstance(context);
	}

	public static AppView newInstance(Context context, Application app) {
		AppView view = new AppView(context);
		view.setFocusable(true);
		GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
		lp.width = 140;
		lp.height = 140;
		lp.setMargins(50, 0, 50, 0);
		view.setLayoutParams(lp);
		view.setApplication(app);
		return view;
	}

	public void setApplication(Application app) {
		this.app = app;
		icon.setImageDrawable(app.getIcon());
		label.setText(app.getLabel());
	}

	public Application getApplication() {
		return this.app;
	}

//	

	/**
	 * 设置应用程序
	 *
	 * @param app
	 */
	public void setApplicaton(Application app) {
		apkFile = null;
		this.app = app;
		// summary = null;
		if (app != null) {
			// setIconFull(false);
			this.title = app.getLabel();
			icon.setImageDrawable(app.getIcon());
			label.setText(title);
		}
	}

	// 安装apk
	public void startInstall() {
		if (apkFile != null) {
			ApplicationUtil.installApkFile(getContext(), apkFile);
		}
	}

	// 去除已添加的apk
	public List<String> insectApp(List<String> app1, List<String> app2) {
		for (int i = 0; i < app2.size(); i++) {
			for (int j = 0; j < app1.size(); j++) {
				if (app2.get(i).equals(app1.get(j))) {
					app1.remove(app1.get(j));
				}
			}
		}
		return app1;
	}

	public boolean isHasleft() {
		return hasleft;
	}

	public void setHasleft(boolean hasleft) {
		this.hasleft = hasleft;
	}

}