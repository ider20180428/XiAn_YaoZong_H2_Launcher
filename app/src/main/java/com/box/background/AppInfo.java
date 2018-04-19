package com.box.background;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ider.launcher.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.File;
import java.io.IOException;
import box.database.DatabaseManager;
import box.utils.PreferenceManager;
import box.utils.RoundCornerTransform;

public class AppInfo extends Activity {
	ImageView icon;
	TextView label, description;
	Button button;
	String url;
	String pkgName;
	String type;
	boolean downloading;
	DownloadUtil downloadUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_info);
		button = (Button) findViewById(R.id.download);
		icon = (ImageView) findViewById(R.id.image);
		label = (TextView) findViewById(R.id.label);
		description = (TextView) findViewById(R.id.description);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		loadIcon(data.getString("icon"));
		label.setText(data.getString("label"));
		description.setText("简介：" + data.getString("description"));
		this.url = data.getString("apk");
		this.type = data.getString("type");
		this.pkgName = data.getString("pkg");
		// 应用程序安装与卸载广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addDataScheme("package");
		registerReceiver(packageReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(packageReceiver);
		super.onDestroy();

	}
	public void loadIcon(String url) {
		/**
		 * 加载图片
		 *
		 * @param url
		 */
		RequestCreator creator, creator2;
		creator = Picasso.with(this).load(url)
				.placeholder(R.drawable.default_b6);
		creator2 = Picasso.with(this).load(url)
				.placeholder(R.drawable.default_b6);
		// creator.resize()之后，很小几率会导致加载失败，此处做判断，如果resize失败，则在RoundCornerTransform操作类中进行resize
		new AsyncTask<RequestCreator, Object, Bitmap>() {

			protected Bitmap doInBackground(RequestCreator... creator) {
				try {
					return creator[0].resize(240, 156)
							.transform(new RoundCornerTransform()).get();
				} catch (IOException e) {
					try {
						return creator[1].transform(
								new RoundCornerTransform(240, 156)).get();
					} catch (IOException e1) {
						e1.printStackTrace();
						return null;
					}
				}
			}

			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					icon.setImageBitmap(result);
				} else {
					// 默认
				}
			}
		}.execute(new RequestCreator[] { creator, creator2 });

	}

	public void download(View view) {
		downloading = true;
		downloadUtil = DownloadUtil.getInstance();
		button.setText("正在下载…");
		new DownloadThread(url).start();
		handler.post(percent);
	}

	Runnable percent = new Runnable() {
		@Override
		public void run() {
			String percent = downloadUtil.getPercent();
			if(percent == null) {
				percent = "";
			}
			button.setText("正在下载… " + downloadUtil.getPercent());
			handler.postDelayed(this, 1000);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					downloading = false;
					button.setText("下载完成，正在安装");
					break;
				case 2:
					button.setText("下载失败");
					break;

				default:
					break;
			}
		}
	};
	//安装广播-------------------
	BroadcastReceiver packageReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String data = intent.getDataString();
			final String packgename = data.substring(data.indexOf(":") + 1,
					data.length());
			System.out.println("----------------packgename"+packgename);
			if (packgename.equals(AppInfo.this.pkgName)) {
				System.out.println("---------------add to database");
				PreferenceManager preManager=PreferenceManager.getInstance(AppInfo.this);
				String tag = preManager.getPackage("tagg");
				if(tag==null){
					DatabaseManager dbManager = DatabaseManager.getInstance(AppInfo.this);
					dbManager.insert(pkgName, type);
				}else{
					String pkg = preManager.getPackage(tag);
					if(pkg==null){
						preManager.putString(tag, packgename);
						preManager.putString("tagg", null);
					}
				}
				AppInfo.this.finish();
			}
		}
	};

	class DownloadThread extends Thread {
		String url;

		public DownloadThread(String apkUrl) {
			this.url = apkUrl;
		}

		@Override
		public void run() {
			String path = downloadUtil.download2Disk(url,pkgName);
			if(path != null) {
				System.out.println("---------------111"+path);
				if(path.endsWith("cfg")) {

					path = path.substring(0, path.lastIndexOf("."));
					System.out.println("--------------------222"+path);
				}
				handler.sendEmptyMessage(1);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(path)),
						"application/vnd.android.package-archive");
				startActivity(intent);
			} else {
				handler.sendEmptyMessage(2);
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if(downloading) {
					exitDialog();

					return true;
				}
			default:
				break;
		}

		return super.onKeyDown(keyCode, event);
	}


	public void exitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.AppInfo_downloading);
		builder.setNegativeButton(R.string.Cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton(R.string.Exit, new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				downloadUtil.stopDownload();
				AppInfo.this.finish();
			}
		});
		builder.create().show();
	}
	

}
