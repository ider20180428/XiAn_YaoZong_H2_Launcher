package com.box.views;

import box.database.DatabaseManager;
import box.utils.NetUtil;
import box.utils.PreferenceManager;
import box.utils.WeatherUtil;

import com.readystatesoftware.viewbadger.BadgeView;
import com.box.launcher.MainActivity;
import com.ider.launcher.R;
import com.yunos.tv.launchercust.api.LctsEvent;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
public class StateBar extends RelativeLayout implements OnFocusChangeListener,
		OnClickListener {
	Context context;
	ImageView netState;
	FlyImageView focusBord;
	Weather weather;
	WeatherUtil weatherUtil;
	static final int LOCATE_OVER = 100;
	static final int WEATHER_OVER = 101;
	static final int LOCATING = 102;
	public static final String NOTIFY_COUNT = "system.ui.notification.count";
	BadgeView badge; // 角标
	PreferenceManager preManager;
	DatabaseManager dbManager;

	public StateBar(Context context) {
		super(context);
	}

	public StateBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dbManager = DatabaseManager.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.top_bar, this);
		weatherUtil = new WeatherUtil(context);
		preManager = PreferenceManager.getInstance(context);
		initViews();
	}

	Handler handler = new Handler();

	public void initViews() {
		netState = (ImageView) findViewById(R.id.net_state);
		weather = (Weather) findViewById(R.id.weather);
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}

	@Override
	public void onClick(View v) {
	}

	/**
	 * view被绘制到窗体时调用，在onDraw()之前
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}



	public void getWeather() {
		weather.refresh(context);
	}



	public void updateNetState() {
		if (NetUtil.isNetworkAvailable(getContext())) {
			/**
			 * if (NetUtil.isWifiConnect(getContext())) {
			 * netState.setImageResource(R.drawable.wifi_level);
			 * netState.setImageLevel(NetUtil.wifiLevel(getContext())); } else {
			 * netState.setImageResource(R.drawable.ethernet_enable); }
			 */
			if (NetUtil.isEthernetConnect(getContext())) {
				netState.setImageResource(R.drawable.ethernet_on);
			} else {
				netState.setImageResource(R.drawable.wifi_level);
				netState.setImageLevel(NetUtil.wifiLevel(getContext()));
			}
		} else {
			netState.setImageResource(R.drawable.wifi_no);
		}
	}

	// 打开通知栏
	public void openstatusbar() {
		try {
			Object obj = getContext().getSystemService("statusbar");
			Class.forName("android.app.StatusBarManager")
					.getMethod("expandNotificationsPanel", new Class[0])
					.invoke(obj, (Object[]) null);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}




}
