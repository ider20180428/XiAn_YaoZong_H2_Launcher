package com.box.views;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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

import com.box.launcher.WifiApManager;
import com.box.launcher.WifiHost;
import com.ider.launcher.R;
import com.readystatesoftware.viewbadger.BadgeView;

import box.database.DatabaseManager;
import box.utils.NetUtil;
import box.utils.PreferenceManager;
import box.utils.WeatherUtil;

@SuppressLint("NewApi")
public class StateBar2 extends RelativeLayout  {
	private Context context;
	//ImageView notify;
	private ImageView weather_image;
	private ImageView netState;
	private FlyImageView focusBord;
//	AlwaysMarqueeTextView location;
    private TextView temperature,temperaturetext;
	private WeatherUtil weatherUtil;
	private static final int LOCATE_OVER = 100;
	private static final int WEATHER_OVER = 101;
	private static final int LOCATING = 102;
	public static final String NOTIFY_COUNT = "system.ui.notification.count";
	private BadgeView badge; // 角标
	private PreferenceManager preManager;
	private DatabaseManager dbManager;
	private  TextView wifiHotSSID,wifiHotPwd;

	public StateBar2(Context context) {
		super(context);
	}

	public StateBar2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dbManager = DatabaseManager.getInstance(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.top_bar2, this);
		weatherUtil = new WeatherUtil(context);
		preManager = new PreferenceManager(context);
		initViews();
		setListeners();
	}



	public void initViews() {
		//	notify = (ImageView) findViewById(R.id.notifycation);
		weather_image = (ImageView) findViewById(R.id.weather);
//		location = (AlwaysMarqueeTextView) findViewById(location);
		wifiHotSSID= (TextView) findViewById(R.id.temperature);
		wifiHotPwd = (TextView) findViewById(R.id.temperaturetext);


        SharedPreferences sp=context.getSharedPreferences("wifi_ssid",Context.MODE_WORLD_READABLE+Context.MODE_WORLD_WRITEABLE);
        String ssid=sp.getString("Ssid","");


//		WifiManager mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//		WifiInfo mWifiInfo =mWifiManager.getConnectionInfo();
//		String wifi=mWifiInfo.getSSID();

		netState = (ImageView) findViewById(R.id.net_state);
		netState.setOnClickListener(onclicker);
//		netState.setOnKeyListener(onKeyListener);
		//	notify.setFocusable(false);
//		location.setFocusable(false);
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//			//	notify.setFocusable(true);
//			//	location.setFocusable(true);
//			}
//		}, 3000);
		registeservers();
	}

	public static final int WIFI_AP_STATE_DISABLING = 10;
	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLING = 12;
	public static final int WIFI_AP_STATE_ENABLED = 13;
	public static final int WIFI_AP_STATE_FAILED = 14;

	private WifiManager mWifiManager;
	private WifiApManager wifiApManager;
	private WifiConfiguration wifiConfiguration;
	private final static String WIFI_AP_SSID="IDER";
	private final static String WIFI_AP_PWD="123456789";

	public void updateWifiApInfo(){

		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiApManager=new WifiApManager(context);
		wifiConfiguration=wifiApManager.getWifiApConfiguration();
		boolean wifiEnable=mWifiManager.isWifiEnabled();

		if(null!=wifiConfiguration){
			String wifiApSSIDString=String.format(wifiConfiguration.SSID);
			String wifiAPPwdString=String.format(wifiConfiguration.preSharedKey);
			wifiHotSSID.setText("wifi账号："+wifiApSSIDString);
			wifiHotPwd.setText("wifi密码："+wifiAPPwdString);
		}else {
			wifiConfiguration=new WifiConfiguration();
			wifiConfiguration.SSID=WIFI_AP_SSID;
			wifiConfiguration.preSharedKey=WIFI_AP_PWD;
			wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wifiHotSSID.setText("wifi账号："+WIFI_AP_SSID);
			wifiHotPwd.setText("wifi密码："+WIFI_AP_PWD);
		}

		if (wifiApManager.getWifiApState()!=WIFI_AP_STATE_ENABLED){
			boolean wifiConfigAble= wifiApManager.setWifiApConfiguration(wifiConfiguration);
			boolean wifihotEnable=wifiApManager.setWifiApEnabled(wifiConfiguration,true);
//			Log.d("zhaoyf++++++","wifi热点设置：SSID="+wifiConfiguration.SSID+"密码="+wifiConfiguration.preSharedKey);
//			Log.d("zhaoyf++++++++++++++","wifi热点设置好了吗？"+wifiConfigAble);
//			Log.d("zhaoyf+++++++++++","wifi热点打开了吗？"+wifihotEnable);
		}

	}




	OnClickListener onclicker=new OnClickListener() {
		@Override
		public void onClick(View view) {
//			Intent intent =getContext().getPackageManager().getLaunchIntentForPackage("android.settings.SETTINGS");
//			if(intent!=null) {
//				getContext().startActivity(intent);
//			}
//			Intent intent_more = new Intent("android.settings.SETTINGS");
			Intent intent_more = new Intent();
			intent_more.setComponent(new ComponentName("com.zxy.idersettings","com.rk_itvui.settings.Settings"));
			if(intent_more!=null)
			getContext().startActivity(intent_more);
		}
	};



	public void setListeners() {
		//	location.setOnFocusChangeListener(this);
		//notify.setOnFocusChangeListener(this);
		//	notify.setOnClickListener(this);

	}

//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		if (focusBord == null) {
//			//focusBord = ((MainActivity) getContext()).getFocusBord();
//		}
//		switch (v.getId()) {
//		case R.id.location:
//			if (hasFocus) {
//				focusBord.hide();
//			}
//			break;
//
//		default:
//			break;
//		}
//	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//
//		default:
//			break;
//		}
//	}

	/**
	 * view被绘制到窗体时调用，在onDraw()之前
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	private int getWeatherCounts=0;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOCATE_OVER:
//					location.setText(msg.obj.toString()+context.getString(R.string.welcome));
					getWeatherCounts++;
					Log.d("zhaoyf++++++++定位==",msg.obj.toString());
					if (getWeatherCounts>3){
						weather.stop();
					}
					break;
				case WEATHER_OVER:
					Bundle bundle=msg.getData();
					Log.d("zhaoyf++++++++天气==",bundle.getString(weatherUtil.STATUS)+bundle.getString(WeatherUtil.TEMPERATURE));
					displayWeather(msg.getData());
					stopGetWeather();
					break;
				case LOCATING:
//					if (location.getText() == null)
//						location.setText(R.string.Locating);

					break;
				default:
					break;
			}
		}

	};

	private WeatherRunnable weather;

	public void getWeather() {
		if (weather == null) {
			weather = new WeatherRunnable();
		} else {
			weather.stop();
		}

		weather.start();
	}

	public void stopGetWeather() {
		if (weather != null) {
			weather.stop();
		}
	}

	class WeatherRunnable implements Runnable {
		boolean go = false;

		private void start() {
			go = true;
			new Thread(this).start();
		}

		private void stop() {
			this.go = false;
		}

		@Override
		public synchronized void run() {
			while (go) {
				Log.i("statebar_weather", "开始获取天气");
				String city = null;
				if (city == null) {
					handler.sendEmptyMessage(LOCATING);
					city = weatherUtil.getCity();
				}
				if (city != null) { // 获取地理信息成功
					Message locateMsg = new Message();
					locateMsg.obj = city;
					locateMsg.what = LOCATE_OVER;
					handler.sendMessage(locateMsg); // 发送定位成功消息
					weatherUtil.getweather(city, WeatherUtil.TODAY); // 开始获取天气信息
					String weatherState = weatherUtil.getWeatherStatus() == null ? weatherUtil
							.getWeatherStatus() : weatherUtil
							.getWeatherStatus2();
					String temperature = weatherUtil.getHighTem() + " ~ "
							+ weatherUtil.getLowTem() + "℃";
					if (weatherState != null && temperature != null) {
						Bundle bundle = new Bundle();
						bundle.putString(WeatherUtil.STATUS, weatherState);
						bundle.putString(WeatherUtil.TEMPERATURE, temperature);
						Message msg = new Message();
						msg.what = WEATHER_OVER;
						msg.setData(bundle);
						handler.sendMessage(msg); // 发送天气消息
					}
				}

				try {
					Thread.sleep(10000); // 如果获取天气失败每十秒获取一次天气
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void displayWeather(Bundle bundle) {
		String str_weather_status = bundle.getString(WeatherUtil.STATUS);
//		String temperature = bundle.getString(WeatherUtil.TEMPERATURE);
//		if (temperature != null) {
//			this.temperature.setText(temperature);
//			this.temperaturetext.setText(str_weather_status);
//		}
		if (str_weather_status != null) {
			if (str_weather_status.contains(getContext().getString(
					R.string.str_cloudy))) {
				weather_image.setImageResource(R.drawable.weather_cloudy);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sunny))) {
				weather_image.setImageResource(R.drawable.weather_sunny);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_shade))) {
				weather_image.setImageResource(R.drawable.weather_dust);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_smoke))) {
				weather_image.setImageResource(R.drawable.weather_fog);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_sand_storm))) {
				weather_image.setImageResource(R.drawable.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sand_storm))) {
				weather_image.setImageResource(R.drawable.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_sand_blowing))) {
				weather_image.setImageResource(R.drawable.weather_wind);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_s_snow))) {
				weather_image.setImageResource(R.drawable.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_m_snow))) {
				weather_image.setImageResource(R.drawable.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_l_snow))) {
				weather_image.setImageResource(R.drawable.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_snow))) {
				weather_image.setImageResource(R.drawable.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_snow_shower))) {
				weather_image.setImageResource(R.drawable.weather_snow);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_s_rain))) {
				weather_image.setImageResource(R.drawable.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_m_rain))) {
				weather_image.setImageResource(R.drawable.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_l_rain))) {
				weather_image.setImageResource(R.drawable.weather_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_h_rain))) {
				weather_image.setImageResource(R.drawable.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_hh_rain))) {
				weather_image.setImageResource(R.drawable.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_hhh_rain))) {
				weather_image.setImageResource(R.drawable.weather_heavy_rain);
			} else if (str_weather_status.contains(getContext().getString(
					R.string.str_shower))) {
				weather_image.setImageResource(R.drawable.weather_shower);
			} else if (str_weather_status.equals(getContext().getString(
					R.string.str_thunder_shower))) {
				weather_image.setImageResource(R.drawable.weather_thunderstorm);
			}
		}
	}
//
//	public void updateNetState() {
//		if (NetUtil.isNetworkAvailable(getContext())) {
//			/**
//			 * if (NetUtil.isWifiConnect(getContext())) {
//			 * netState.setImageResource(R.drawable.wifi_level);
//			 * netState.setImageLevel(NetUtil.wifiLevel(getContext())); } else {
//			 * netState.setImageResource(R.drawable.ethernet_enable); }
//			 */
//			if (NetUtil.isEthernetConnect(getContext())) {
//				netState.setImageResource(R.drawable.ethernet_on);
//			} else {
//				netState.setImageResource(R.drawable.wifi_level);
//				netState.setImageLevel(NetUtil.wifiLevel(getContext()));
//			}
//		} else {
//			netState.setImageResource(R.drawable.wifi_no);
//		}
//	}

//	// 打开通知栏
//	public void openstatusbar() {
//		try {
//			Object obj = getContext().getSystemService("statusbar");
//			Class.forName("android.app.StatusBarManager")
//					.getMethod("expandNotificationsPanel", new Class[0])
//					.invoke(obj, (Object[]) null);
//
//			// 埋点
//			LctsEvent event = new LctsEvent();
//			event.setType(LctsEvent.TYPE_ITEM_CLICK);
//			Bundle data = new Bundle();
//			data.putString(Constant.TAG_NOFITY, "通知");
//			event.setData(data);
//			MainActivity.sendMessage(event);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 更新通知数量
	 *
	 * @param count
	 */
	public void updateNotifyCount(int count) {
		if (badge == null) {
			//	badge = new BadgeView(getContext(), notify);
			badge.setTextSize(11f);
			badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		}
		if (count != 0) {
			badge.setText(String.valueOf(count));
			badge.show();
		} else {
			badge.hide();
		}
	}

	private void registeservers() {
		IntentFilter filter;

		filter = new IntentFilter();
		filter.addAction(NetUtil.CONNECTIVITY_CHANGE);
		filter.addAction(NetUtil.RSSI_CHANGE);
		filter.addAction(NetUtil.WIFI_STATE_CHANGE);
		context.registerReceiver(netReceiver, filter);


	}

	BroadcastReceiver netReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {
				updateNetState(context);
			}
		}
	};

	private void  updateNetState(Context context){
		if (NetUtil.isNetworkAvailable(context)) {
			if(NetUtil.isEthernetConnect(context)) {
				//开启热点
				updateWifiApInfo();
			} else {
				//WiFi已连接
				wifiHotSSID.setText("Wifi已连接");
				wifiHotPwd.setText("");
			}

		} else {
			//提示有线网络未连接
			wifiHotSSID.setText("有线网络未连接");
			wifiHotPwd.setText("");
		}
	}

	public void destory(){
		context.unregisterReceiver(netReceiver);
	}

}
