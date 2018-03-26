package com.box.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.box.views.FlyImageView;
import com.box.views.MyDialog;
import com.ider.launcher.R;

import java.text.DecimalFormat;
import java.util.List;

import box.adapter.AppAdapter;
import box.database.DatabaseManager;

public class CommonApp extends Activity {
	boolean DEBUG = false;
	String TAG = "CommanApp";
	DatabaseManager dbManager;
	// ApplicationUtil appUtil;
	AppAdapter adapter;
	GridView grid;
	RelativeLayout mainRelative;
	FlyImageView selectBord;
	List<String> commonApps;
	int uploadDelayedMills = 2000;
	Handler handler = new Handler();
	DecimalFormat df = new DecimalFormat("0000");
	int startTag = 201;
	ImageView screen_move;
	ImageView screen_keep;
	ImageView img_dim;
	MyDialog dialog;
	// ScreensaverReceiver screensaverReceiver;
	long oldTimeMills;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_apps);
		if (DEBUG)
			Log.i(TAG, "-----------oncreate---------");
		dbManager = DatabaseManager.getInstance(this);
		// appUtil = ApplicationUtil.newInstance(this);

		mainRelative = (RelativeLayout) findViewById(R.id.main_relative);
		grid = (GridView) findViewById(R.id.common_grid);
		selectBord = (FlyImageView) findViewById(R.id.common_app_focus_bg);

		Intent intent = getIntent();
		int y = intent.getIntExtra("keep", 0);

		screen_move = (ImageView) findViewById(R.id.image_move);
		screen_keep = (ImageView) findViewById(R.id.image_keep);
		img_dim = (ImageView) findViewById(R.id.image_dim);

		if (!MainActivity.move.isRecycled())
			screen_move.setImageBitmap(MainActivity.move);
		if (!MainActivity.keep.isRecycled())
			screen_keep.setImageBitmap(MainActivity.keep);
		screen_keep.setY(y);
		mainRelative.setY(y - Constant.commonAppTranLength);

		TranslateAnimation tranAnim = new TranslateAnimation(0, 0, 0,
				-Constant.commonAppTranLength);
		tranAnim.setDuration(300);
		tranAnim.setFillAfter(true);
		screen_move.startAnimation(tranAnim);
		tranAnim.setAnimationListener(new MyAnimationListener());

		commonApps = getCustomApps();
		adapter = new AppAdapter(this, R.layout.common_item, commonApps);
		grid.setAdapter(adapter);
		setListeners();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {

		if (dialog != null) {
			dialog.dismiss();
		}
		super.onDestroy();

	}

	class MyAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation arg0) {
			System.out.println("anim end");

			AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
			alphaAnim.setDuration(300);
			alphaAnim.setFillAfter(true);
			alphaAnim.setAnimationListener(new DimAnimationListener());
			img_dim.startAnimation(alphaAnim);
			mainRelative.bringToFront();

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

	}

	class DimAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation arg0) {
			img_dim.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

	}

	class ExitAlphaAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation arg0) {

			screen_move.bringToFront();
			img_dim.bringToFront();
			TranslateAnimation exitAnim = new TranslateAnimation(0, 0,
					-Constant.commonAppTranLength, 0);
			exitAnim.setDuration(500);
			exitAnim.setFillAfter(true);
			exitAnim.setAnimationListener(new ExitTranAnimationListener());
			screen_move.startAnimation(exitAnim);

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

	}

	class ExitTranAnimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation arg0) {

			CommonApp.this.finish();
			CommonApp.this.overridePendingTransition(0, 0);

		}

		@Override
		public void onAnimationRepeat(Animation arg0) {

		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}

	}

	public void setListeners() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String tag = getTagByPosition(position);
				// 添加
				if (commonApps.get(position).equals("app.add")) {
					showAddDialog(commonApps);
				}

				else {
					// 应用程序
					String pkg = commonApps.get(position);
					Intent intent = getPackageManager()
							.getLaunchIntentForPackage(pkg);
					startActivity(intent);
				}
			}
		});

		grid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int toX = location[0];
				int toY = location[1] - (int) mainRelative.getY();
				selectBord.flyTo(view.getWidth(), view.getHeight(), toX, toY,
						R.drawable.common_app_focus_bg, 100);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		grid.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				String pkg = commonApps.get(position);
				if (!pkg.equals(Constant.ADD_PACKAGE)) {
					commonApps.remove(pkg);
					dbManager.delete(pkg, Constant.TYPE_COMMON);
					adapter.notifyDataSetChanged();
				}
				return true;
			}
		});
	}

	// 网络测速
	public void testNetSpeed(String tag) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.yunos.tv.probe",
				"com.yunos.tv.probe.NetworkSpeedActivity"));
		startActivity(intent);

	}

	// WIFI热点
	public void wifiAp(String tag) {
		Intent intent = new Intent();
		intent.setComponent(new ComponentName("com.android.settings",
				"com.android.settings.network.wifi.WifiApSettingActivity"));
		startActivity(intent);
	}

	public List<String> getCustomApps() {

		List<String> list = dbManager
				.findApplicationByType(Constant.TYPE_COMMON);
		return list;

	}

	public void showAddDialog(List<String> list) {
		final List<String> allapps = ApplicationUtil
				.loadAllApplication(this);

		allapps.removeAll(list);
		dialog = new MyDialog(this, R.layout.add_dialog, R.style.MyDialog);
		View view = dialog.getWindow().getDecorView();
		GridView grid = (GridView) view.findViewById(R.id.app_grid);
		final FlyImageView bord = (FlyImageView) view.findViewById(R.id.bord);
		grid.setAdapter(new AppAdapter(this, R.layout.add_item, allapps));
		dialog.show();

		grid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				ImageView icon = (ImageView) view
						.findViewById(R.id.app_item_icon);
				int[] location = new int[2];
				icon.getLocationInWindow(location);
				bord.flyTo(view.getWidth(), view.getHeight(), location[0] - 31,
						location[1] - 35, R.drawable.all_app_selecter, 100);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				addToCommon(allapps.get(position));
				dialog.cancel();

			}
		});
	}

	public void addToCommon(String pkg) {
		dbManager.insert(pkg, Constant.TYPE_COMMON);
		commonApps.add(commonApps.size() - 1, pkg);
		adapter.notifyDataSetChanged();
	}

	public String getTagByPosition(int position) {
		return df.format(startTag + position);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			AlphaAnimation exitAlpha = new AlphaAnimation(1.0f, 0.0f);
			exitAlpha.setDuration(200);
			exitAlpha.setFillAfter(true);
			exitAlpha.setAnimationListener(new ExitAlphaAnimationListener());
			img_dim.startAnimation(exitAlpha);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}
