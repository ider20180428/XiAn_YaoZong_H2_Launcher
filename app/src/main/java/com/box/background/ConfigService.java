package com.box.background;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import box.utils.NetUtil;
import box.utils.PreferenceManager;
import box.utils.WeatherUtil;

public class ConfigService extends Service {

	public String TAG = "launcher_mini";
	private Handler handler = new Handler();
	public ConfigBinder mBinder;
	private int vendorID;
	private String model;
	private ArrayList<String> tags;
	private ArrayList<TagConfig> configs;
	private int tagRequestCount = 0;
	public static final int TYPE_TAG_CONFIG = 0;
	public static final String TAG_ACTION_HASUPDATE = "TAG_ACTION_HASUPDATE";
	public static final String UPATE_TAGS = "UPDATE_TAGS";

	public static final String TAG_CONFIG_STATUS_IMAGE = "0";
	public static final String TAG_CONFIG_STATUS_APPLICATION = "1";
	public static final String TAG_CONFIG_STATUS_NULL = "2";
	public static final String TAG_CONFIG_STATUS_CUSTOM = "3";

	public static final String URL_SERVER_VERSION = "http://szider.net/searchinfo.aspx?type=0";
	public String URL_TAG_CONFIG = "http://szider.net/searchinfo.aspx?type=1&vendorID=%d&model=%s&city=%s";
	public static final String SERVER_DOMAIN = "http://szider.net";


	public class ConfigBinder extends Binder {
		public ConfigService getService() {
			return ConfigService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		tags = new ArrayList<String>();
		configs = new ArrayList<TagConfig>();
		vendorID = Product.getVendorId(ConfigService.this);
//		vendorID=0;
		model = Build.MODEL;
//		model = "IDER_BBA51";
		mBinder = new ConfigBinder();
		handler.postDelayed(checkServerVersion, 30 * 60 * 1000);
	}
	Runnable checkServerVersion = new Runnable() {
		@Override
		public void run() {
			if(NetUtil.isNetworkAvailable(ConfigService.this)) {
				checkUpdate();
			}
			handler.postDelayed(this, 30 * 60 * 1000);
		}
	};
	@Override
	public IBinder onBind(Intent intent) {
		return this.mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	/**
	 * 检查update
	 *
	 * @return
	 */
	public void checkUpdate() {

		// 首先对比服务端与本地数据本地版本
		new AsyncTask<Object, Object, Integer>() {

			@Override
			protected Integer doInBackground(Object... arg0) {
				try {

					String result = OKhttpManager.exuteFromServer(URL_SERVER_VERSION);
					JSONArray array = new JSONArray(result);
					int count = array.getJSONObject(0).getInt("count");
					return count;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}

			protected void onPostExecute(Integer result) {

				Log.i("url", String.valueOf(result));
				// 比较
				if (result > getLastestLocalVersion() || result == 0) {
					PreferenceManager.getInstance(ConfigService.this).setLocalServiceVersion(result);
					requestUpdate();
				}
			};

		}.execute();

	}

	public int getLastestLocalVersion() {
		return PreferenceManager.getInstance(ConfigService.this)
				.getLocalServiceVersion();
	}

	/**
	 * 请求更新数据
	 */
	public void requestUpdate() {
		tagRequestCount++;
		new AsyncTask<Object, Object, String>() {

			@Override
			protected String doInBackground(Object... params) {
				String city = getConfigCity();
//				Log.i(TAG, city);
				try {
					String url = String.format(URL_TAG_CONFIG, new Object[] {params[0], params[1], URLEncoder.encode(city, "utf-8")});
					Log.i(TAG, url);
					String result = OKhttpManager.exuteFromServer(url);
					if(result != null) {
						//本地缓存
						PreferenceManager.getInstance(ConfigService.this)
								.putLastConfig(TYPE_TAG_CONFIG, result);
						Log.i(TAG,
								"requestUpdate  start put local config"+result);
						return result;
					}

				} catch (Exception e) {

					Log.i(TAG,
							"requestUpdate  erro, url format failed...start read local config");
					return PreferenceManager.getInstance(ConfigService.this)
							.readLastConfig(TYPE_TAG_CONFIG);
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				if(result == null || result.equals("null")){
					// 没有结果， 一分钟后重试
					Log.i(TAG,
							"get tagConfig data failed , try again 1 minute later");
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (tagRequestCount < 3) {
								requestUpdate();
							} else {
								//PreferenceManager.getInstance(ConfigService.this).getPackage("servicedata");
								Log.i(TAG, "get tagConfig has failed for 3 times, stop request");
							}
						}
					}, 10 * 1000);
				}else{
					try {
						Log.i(TAG, "9999999999");
						parseJsonResult(result);
						//PreferenceManager.getInstance(ConfigService.this).putString("servicedata", result);
						tagRequestCount = 0;
					} catch (JSONException e) {
						Log.i(TAG, "failed to parse config result : " + result);
					}
				}
			};

		}.execute(new Object[] { this.vendorID, this.model });

	}

	private String getConfigCity() {
		// 首先联网，定位城市
		String city = new WeatherUtil(ConfigService.this).getCity();
		if(city == null) {
			// 若定位失败，采用本地城市
			city = PreferenceManager.getInstance(ConfigService.this).getConfigLocation();
			if(city == null) {
				city = "北京";
			}
		} else {
			PreferenceManager.getInstance(ConfigService.this).setConfigLocation(city);
		}

		return city;
	}

	/**
	 * 解析服务端返回的JSON数据
	 *
	 * @throws JSONException
	 */
	public void parseJsonResult(String result) throws JSONException {
		if(result == null) {
			return;
		}
		tags.clear();
		configs.clear();

//		JSONObject object = new JSONObject(result);

//		JSONArray arrays = object.getJSONArray("tag");
		JSONArray arrays = new JSONArray(result);
		if (arrays == null || arrays.length() == 0) {
			return;
		}
		for (int i = 0; i < arrays.length(); i++) {
			JSONObject tagConfig = arrays.getJSONObject(i);
			String tag = tagConfig.getString("tag");
			String status = tagConfig.getString("statues");
			String pkg = tagConfig.getString("package");
			String verName = tagConfig.getString("vername");
			//	int verCode = tagConfig.getInt("vercode");
			String label = tagConfig.getString("label");
			String description = tagConfig.getString("descriptions");
			String summary = tagConfig.getString("summary");
			String iconUrl = tagConfig.getString("icon");
			String apkUrl = tagConfig.getString("down");
			String miniImage = tagConfig.getString("miniimg");
			String image = tagConfig.getString("images");


			TagConfig config = new TagConfig();
			config.setStatus(status);
			config.setPkgName(pkg);
			//	config.setVerCode(verCode);
			config.setVerName(verName);
			config.setLabel(label);
			config.setDescription(description);
			config.setSummary(summary);
			if(iconUrl.startsWith("http")){
				config.setIconUrl(iconUrl);
			}else{
				config.setIconUrl(SERVER_DOMAIN + iconUrl);
			}
			if(apkUrl.startsWith("http")){
				config.setApkUrl(apkUrl);
			}else{
				config.setApkUrl(SERVER_DOMAIN + apkUrl);
			}
			if(miniImage.startsWith("http")){
				config.setMiniImage(miniImage);
			}else{
				config.setMiniImage(SERVER_DOMAIN + miniImage);
			}
			if(image.startsWith("http")){
				config.setImage(image);
			}else{
				config.setImage(SERVER_DOMAIN + image);
			}
			configs.add(config);  // 填充configure数据
			tags.add(tag); // 填充tag数据
		}
		Intent intent = new Intent();
		intent.setAction(TAG_ACTION_HASUPDATE);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(ConfigService.UPATE_TAGS, tags);
		intent.putExtras(bundle);
		sendBroadcast(intent);

	}

	// 读取本地
	public void readLocalConfig() {
		Log.i(TAG, "Network not available, read local data instead");
		String tagConfig = PreferenceManager.getInstance(this).readLastConfig(
				TYPE_TAG_CONFIG);
		Log.i(TAG, "Network not available, read local data instead"+tagConfig);
		try {
			parseJsonResult(tagConfig);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public TagConfig getTagConfig(String tag) {
		int position = tags.indexOf(tag);
		return configs.get(position);
	}

}
