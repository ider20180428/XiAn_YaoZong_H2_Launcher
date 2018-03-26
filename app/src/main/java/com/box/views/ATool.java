package com.box.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import box.adapter.AppAdapter;
import box.cleansystem.StartActivity;
import box.utils.NetUtil;
import box.utils.PreferenceManager;
import box.utils.RoundCornerTransform;
import box.utils.TagUtil;
import box.utils.Video;

import com.box.background.AppInfo;
import com.box.background.TagConfig;
import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.box.launcher.Constant;
import com.box.launcher.MainActivity;
import com.ider.launcher.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yunos.tv.launchercust.api.LctsEvent;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint("Recycle")
public class ATool extends FrameLayout {
	String TAG = "YD12";
	int iconRes;
	int bgRes;
	String summary;
	String title;
	ImageView icon;
	TextView label;
	Bitmap bitmap;
	Application app;
	PreferenceManager preManager;
	//	TabsBar tabsBar;
	TagConfig config;
	ImageView corner;
	String pkgName;
	Context context;
	public ATool(Context context) {
		super(context);
		setFocusable(false);
		preManager = PreferenceManager.getInstance(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		this.context = context;
		initViews();
	}

	//	public void backlan(){
//		label.setBackgroundResource(0);
//	}
	public void hidlan(){
		label.setVisibility(View.INVISIBLE);
	}

	public void setlayout(){
		LayoutParams param = new LayoutParams(200, 50);
		param.setMargins(0, 105, 0, 5);
		label.setLayoutParams(param);
		label.setGravity(Gravity.CENTER);
	}
	public void showlan(){
		Animation bigIn = AnimationUtils.loadAnimation(context,
				R.anim.scale_big_in);
		label.setAnimation(bigIn);
		label.setVisibility(View.VISIBLE);
		//label.setBackgroundResource(R.drawable.textbackground);
	}

	public ATool(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(false);
		preManager = PreferenceManager.getInstance(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tool_item, this);
		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.ToolItem);
		iconRes = typeArray.getResourceId(R.styleable.ToolItem_tool_icon, 0);
		bgRes = typeArray.getResourceId(R.styleable.ToolItem_background, 0);
		title = typeArray.getString(R.styleable.ToolItem_tool_label);
		initViews();
	}

	public void labset(){
		LayoutParams parms = new LayoutParams(200, 50);
		parms.setMargins(0, 106, 0, 10);
		label.setLayoutParams(parms);
	}
	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

	public int getBgRes() {
		return bgRes;
	}

	public void setBgRes(int bgRes) {
		this.bgRes = bgRes;
	}

	public ImageView getIcon() {
		return icon;
	}

	public void initViews() {
		setBackgroundResource(bgRes);
		corner = (ImageView) findViewById(R.id.corner);
		icon = (ImageView) findViewById(R.id.tool_icon);
		setIconRes(iconRes);
		icon.setImageResource(iconRes);
		icon.setVisibility(View.VISIBLE);
		label = (TextView) findViewById(R.id.tool_label);
		label.setText(title);

		icon.setScaleType(ScaleType.FIT_XY);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addDataScheme("package");
		getContext().registerReceiver(packageReceiver, filter);

	}

	BroadcastReceiver packageReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			String data = intent.getDataString();
			String dataPackage = data.substring(8, data.length());

			if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {

			} else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {

			}
		}
	};

	/**
	 * 设置热点
	 *
	 * @param text
	 */
	public void setSummary(String summary) {
		this.summary = summary;
		if (summary != null)
			label.setText(summary);
		else
			label.setText("");
		if (this.isFocused()) {
			label.setVisibility(View.VISIBLE);
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public String getSummary() {
		return summary;
	}

	public void hideTitle() {
		label.setVisibility(View.INVISIBLE);
	}

	public void showTitle() {
		label.setVisibility(View.VISIBLE);
	}

	public void hideTitleBackground() {
		label.setBackgroundResource(0);
	}

	/**
	 * 设置应用程序
	 *
	 * @param app
	 */
	public void setApplicaton(Application app) {
		this.app = app;
		this.pkgName = app.getPackageName();
		summary = null;
		this.title = app.getLabel();
		label.setText(title);
		if(app.getIcon()==null){
			Picasso.with(getContext()).load(app.getTage()).into(icon);
			corner.setVisibility(View.VISIBLE);
		}else{
			icon.setImageDrawable(app.getIcon());
		}
	}

	public Application getApplication() {
		return this.app;
	}

	/**
	 * 根据图片链接设置icon
	 *
	 * @param url
	 */
	public void setBitmap(String url) {
		new AsyncTask<String, Object, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return NetUtil.loadBitmap(params[0]);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					Bitmap src = createScaledBitmap(result, 240, 156);
					bitmap = getRoundedCornerBitmap(src);

					icon.setImageBitmap(bitmap);
				}
			}
		}.execute(url);
	}

	public Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) {
			src.recycle();
		}
		return dst;
	}

	public void setImageResource(int resId) {
		icon.setImageResource(resId);
	}

	/**
	 * 处理图片圆角
	 *
	 * @param bitmap
	 * @return
	 */
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 15;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 返回当前tab id
	 *
	 * @return
	 */

//	public int getCurrentTabId() {
//
//		if (tabsBar == null) {
//			View mainView = ((MainActivity) getContext()).getWindow()
//					.getDecorView();
//			tabsBar = (TabsBar) mainView.findViewById(R.id.tabs_bar);
//		}
//		return tabsBar.getCurrentTabId();
//	}

	public boolean onTop() {
		android.view.ViewGroup.LayoutParams lp = getLayoutParams();

		if (lp instanceof RelativeLayout.LayoutParams
				&& ((RelativeLayout.LayoutParams) lp).topMargin == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 加载图片
	 *
	 * @param url
	 */
	public void updateIcon(final String url, boolean iconFull) {
		RequestCreator creator, creator2;
		if (iconRes != 0) {
			creator = Picasso.with(getContext()).load(url).placeholder(iconRes);
			creator2 = Picasso.with(getContext()).load(url)
					.placeholder(iconRes);
		} else {
			creator = Picasso.with(getContext()).load(url)
					.placeholder(R.drawable.default_b6);
			creator2 = Picasso.with(getContext()).load(url)
					.placeholder(R.drawable.default_b6);
		}
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



	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					label.setText("下载完成，正在安装");
					break;

				default:
					break;
			}
		}
	};



	public void updateConfig(TagConfig config) {
		Log.i("updateConfig", config.pkgName+"/"+config.iconUrl+"/"+config.label);
		this.config = config;
		this.pkgName = config.pkgName;
		updateIcon(config.iconUrl, false);
		label.setText(config.summary);
		this.app= ApplicationUtil.doAddApplicationtwo(getContext(), config.pkgName, config.summary, config.iconUrl);
		//	corner.setVisibility(View.VISIBLE);
	}


	public void startDownload(String type) {
		Intent intent = new Intent(getContext(), AppInfo.class);
		Bundle bundle = new Bundle();
		bundle.putString("icon", config.iconUrl);
		updateConfigIcon(config.iconUrl);
		bundle.putString("apk", config.apkUrl);
		bundle.putString("description", config.description);
		bundle.putString("label", config.label);
		bundle.putString("type", type);
		bundle.putString("pkg", config.pkgName);
		intent.putExtras(bundle);
		getContext().startActivity(intent);
	}

	public String getPkg() {
		return this.pkgName;
	}
	public void updateConfigIcon(String url) {
		Picasso.with(getContext()).load(url)
				.placeholder(R.drawable.ider_replace).into(icon);

	}

}
