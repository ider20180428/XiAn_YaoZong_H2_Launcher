package box.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.box.launcher.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetUtil {
	static boolean DEBUG = true;
	public static String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	public static String RSSI_CHANGE = "android.net.wifi.RSSI_CHANGED";
	public static String WIFI_STATE_CHANGE = WifiManager.WIFI_STATE_CHANGED_ACTION;
	
	
	
	

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiInfo.isConnected() && wifiInfo.isAvailable();
	}


	@SuppressLint("InlinedApi")
	public static boolean isEthernetConnect(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Service.CONNECTIVITY_SERVICE);
		NetworkInfo etherInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
		return etherInfo.isConnected() && etherInfo.isAvailable();
	}

	public static boolean isNetworkAvailable(Context context) {
		return isWifiConnect(context) || isEthernetConnect(context);
	}


	public static int wifiLevel(Context context) {
		WifiManager manager = (WifiManager) context
				.getSystemService(Service.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		return Math.abs(info.getRssi());
	}


	public static InputStream getInputStream(String strUrl) {
		InputStream in = null;
		try {
			URL url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			in = connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

	public static String getStringFromUrl(String strUrl) {
		InputStream in = null;
		String content = null;
		URL url;
		try {
			url = new URL(strUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(5000);
			connection.connect();
			in = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str = null;
			content = new String();
			while ((str = br.readLine()) != null) {
				content = content + str;
				str = null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}


	public static Bitmap downloadBitmap(String imageUrl) {
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL url = new URL(imageUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			if(DEBUG) Log.i("NetUtil", "45454456");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}


	public static Bitmap loadBitmap(String url) {
		String path = Constant.CACHE_PATH + File.separator
				+ getNameFromUrl(url);
		Bitmap bitmap = null;
		File file = new File(Constant.CACHE_PATH + File.separator
				+ getNameFromUrl(url));
		try {
			HttpURLConnection connection = (HttpURLConnection) (new URL(url)
					.openConnection());

			if (!file.exists()) {
				if(DEBUG) Log.i("NetUtil", "45654545" + url);
				download2Disk(url);
			} else {

				if (file.length() != connection.getContentLength()) {
					
					if(DEBUG) Log.i("NetUtil", "45454556445");
					file.delete();
					download2Disk(url);
				}
			}
			connection.getContentLength();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

	public static Bitmap createScaledBimtap(Bitmap bmp, int dstWidth,
			int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, false);
		if (bmp != dst) {
			bmp.recycle();
		}
		return dst;
	}


	public static String download2Disk(String url) {
		File file = new File(Constant.CACHE_PATH + File.separator
				+ getNameFromUrl(url));
		if (!file.exists()) {
			HttpURLConnection conn = null;
			FileOutputStream fos = null;
			InputStream is = null;
			try {
				URL picUrl = new URL(url);
				conn = (HttpURLConnection) picUrl.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setReadTimeout(10 * 1000);
				is = conn.getInputStream();
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = is.read(buffer)) != -1) {
					fos.write(buffer, 0, length);
				}
			} catch (Exception e) {
				e.printStackTrace();
				file.delete();
				return null;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return file.getPath();
	}

	public static String getNameFromUrl(String url) {
		String name = null;
		if(url == null) {
			return null;
		}
		int regionStart = url.lastIndexOf("/") + 1;
		int regionEnd = url.length();
		if(url.length() > 4 && regionStart != 0 && regionStart < regionEnd) {
			name = url.substring(regionStart, regionEnd);
			return name;
		}
		return null;
	}
}
