package com.box.background;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.box.launcher.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadUtil {
	String TAG = "ConfigService";
	private boolean keep;
	private boolean downloading = false;

	private static DownloadUtil downloadUtil;
	private boolean forceDownloading = false;
	private long forceTotalSize = 1;
	private long forceCurrentSize = 0;

	private long totalSize = 1;
	private long downloadedSize = 0;
	private String pkgName;
	RandomAccessFile raFile;

	private DownloadUtil() {
		printPercent();
	}

	public static DownloadUtil getInstance() {

		if (downloadUtil == null) {
			downloadUtil = new DownloadUtil();
		}
		return downloadUtil;
	}

	public String download2Disk(String url, String pkg) {
		this.pkgName = pkg;
		keep = true;
		File cacheFolder = new File(Constant.CACHE_PATH);
		if (!cacheFolder.exists()) {
//			Log.i(TAG, "���������ļ� �� " + cacheFolder);
			boolean s1 = cacheFolder.mkdirs();
//			Log.i(TAG, "���������ļ� �� " + s1);
		}
		File apkFile = new File(Constant.CACHE_PATH + File.separator
				+ getNameFromUrl(url));
		if (apkFile.exists()) {
			downloading = false;
			return apkFile.getPath();
		} else {
			downloading = true;
			File file = new File(Constant.CACHE_PATH + File.separator + getCacheNameFromUrl(url));
			downloadedSize = file.length();
			HttpURLConnection conn = null;
			InputStream is = null;
			try {
				URL picUrl = new URL(url);
				conn = (HttpURLConnection) picUrl.openConnection();
				conn.setConnectTimeout(5 * 1000);
				conn.setReadTimeout(10 * 1000);

				long startOffet = file.length();
				conn.setRequestProperty("Range", "bytes=" + startOffet + "-");
				long currentSize = conn.getContentLength();
				totalSize = currentSize + startOffet;
//				Log.i(TAG, "����������ʼλ��Ϊ �� " + startOffet);
//				Log.i(TAG, "��ǰconnect ����Ϊ�� " + currentSize);
//				Log.i(TAG, "�����ļ��ܳ���Ϊ�� " + totalSize);
				raFile = new RandomAccessFile(file, "rw");
				if (file.exists()) {
					if (totalSize != -1 && file.length() == currentSize) {
						downloading = false;
						totalSize = file.length();
						return file.getPath();
					}
				}
				is = conn.getInputStream();
				byte[] buffer = new byte[1024];
				int length;

				raFile.seek(startOffet);

				while (keep && ((length = is.read(buffer)) != -1)) {
					raFile.write(buffer, 0, length);
					downloadedSize = file.length();
				}

			} catch (FileNotFoundException e) {
				Log.i(TAG, "file  busy!!! please reboot and retry!!!");
				try {
					if(raFile!=null){
					raFile.close();
					}
					file.delete();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Log.i(TAG, "close inputstream and fileoutputstream");
				if (raFile != null) {
					try {
						raFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			downloading = false;
//			Log.i(TAG, "file���ȣ�" + file.length() + "/�ܳ��� ��" + totalSize);
			if (file.exists() && file.length() == totalSize) {
				File apk = new File(Constant.CACHE_PATH + File.separator + getNameFromUrl(url));
				file.renameTo(apk);
//				Log.i(TAG, "���ص��ļ���" + file.getName());
				return file.getPath();
			} else {
				return null;
			}
		}
	}

	public void stopDownload() {
		keep = false;
	}

	public String getNameFromUrl(String url) {
		String name = null;
		if (url == null) {
			return null;
		}
		int regionStart = url.lastIndexOf("/") + 1;
		int regionEnd = url.length();
		if (url.length() > 4 && regionStart != 0 && regionStart < regionEnd) {
			name = url.substring(regionStart, regionEnd);
			return name;
		}
		return null;
	}

	public String getCacheNameFromUrl(String url) {
		return getNameFromUrl(url) + ".cfg";
	}

	public String getPercent() {
		String percent = null;
//		Log.i(TAG, "currentSize = " + downloadedSize + "/ totalSize = " + totalSize);
		if (totalSize != 0) {
			long data = downloadedSize * 100 / totalSize;
			if (data > 100) {
				return "0%";
			}
			return data + "%";
		}
		return percent;
	}

	public boolean downloading() {
		return downloading;
	}

	public String currentPackage() {
		return pkgName;
	}

	public void forceDownload(Context context, String url) {
		if (url == null) {
			return;
		}

		Log.i(TAG, "force download : " + url);
		forceDownloading = true;
		forceCurrentSize = 0;
		String name = getNameFromUrl(url);
		File cacheFolder = new File(Constant.CACHE_PATH);
		if (!cacheFolder.exists() || !cacheFolder.isDirectory()) {
			cacheFolder.delete();
			cacheFolder.mkdirs();
		}
		File file = new File(Constant.CACHE_PATH + File.separator + name);
		HttpURLConnection conn = null;
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			URL picUrl = new URL(url);
			conn = (HttpURLConnection) picUrl.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(10 * 1000);
			forceTotalSize = conn.getContentLength();
			if (file.exists()) {
				if (file.length() < forceTotalSize) {
					file.delete();
				} else {
					install(context, file);
					return;
				}
			}
			Log.i(TAG, "startForce Download : " + url);
			is = conn.getInputStream();
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				fos.write(buffer, 0, length);
				forceCurrentSize = file.length();
			}
		} catch (Exception e) {
			e.printStackTrace();
			file.delete();
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
		install(context, file);
	}

	public void install(Context context, File file) {
		Log.i(TAG, "force download success, installing now");
		forceDownloading = false;
		if (file.exists()) {
//			InstallUtil installUtil = new InstallUtil(context, false);
//			installUtil.silenceInstall(file);
		}
	}

	Handler handler = new Handler();

	class ForceDownload implements Runnable {

		Context context;
		String url;

		public ForceDownload(Context context, String url) {
			this.context = context;
			this.url = url;
		}

		@Override
		public void run() {
			// printPercent();
			if (!forceDownloading) {
				new Thread() {
					@Override
					public void run() {
						forceDownload(context, url);
					}
				}.start();
			} else {
				Log.i(TAG, "force downing is doing now, please wait");
				handler.postDelayed(this, 10 * 1000);
			}

		}
	};

	public void addToDownloadList(Context context, String url) {
		handler.post(new ForceDownload(context, url));
	}

	public String forcePercent() {
		String percent = null;
		if (forceTotalSize != 0) {
			long data = forceCurrentSize * 100 / forceTotalSize;
			return data + "%";
		}
		return percent;
	}

	Runnable print = new Runnable() {

		@Override
		public void run() {
			if (!forcePercent().equals("0%") && !forcePercent().equals("100%")) {
				Log.i(TAG, "FORCE downloading : " + forcePercent());
				handler.postDelayed(this, 1000);
			}
		}
	};

	public void printPercent() {
		handler.post(print);
	}

}
