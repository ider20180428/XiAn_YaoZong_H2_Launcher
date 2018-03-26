package com.box.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import box.utils.NetUtil;
import box.utils.PreferenceManager;
import box.utils.WeatherUtil;

public class Weather extends TextView {

	WeatherUtil weatherUtil;
	static final int WEATEHR_FINISHED = 1;
	String city;
	PreferenceManager pm;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case WEATEHR_FINISHED:
					String highTmp = weatherUtil.getHighTem();
					String lowTmp = weatherUtil.getLowTem();
					setText(lowTmp+"~"+highTmp+"℃");
					break;

				default:
					break;
			}

		}

	};

	public Weather(Context context, AttributeSet attrs) {
		super(context, attrs);
		weatherUtil = new WeatherUtil(context);
		pm = PreferenceManager.getInstance(context);
		// 初始化时更新天气
		if(NetUtil.isNetworkAvailable(context)){
			new GetWeather().start();
		}
	}


	class GetWeather extends Thread {

		@Override
		public void run() {
			city = pm.getManuCity();
			if(city == null) {
				city = weatherUtil.getCity();
			}

			if(city != null) {
				weatherUtil.getweather(city, WeatherUtil.TODAY);
				handler.sendEmptyMessage(WEATEHR_FINISHED);

			}
		}
	}

	/**
	 * 获取当前城市
	 * @return
	 */
	public String getCurrentCity() {
		return city;
	}



	/**
	 * 更新天气
	 */
	public void refresh(Context context) {

		new GetWeather().start();

	}
 	

}
