package com.box.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.ider.launcher.R;

import box.utils.PreferenceManager;

@SuppressLint("Recycle") public class KeyItem extends LinearLayout {

	ImageView key;
	ImageView key_icon;
	int keycode;  // 用于数据保存的key
	PreferenceManager preManager;

	public KeyItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		preManager = PreferenceManager.getInstance(context);
		LayoutInflater.from(context).inflate(R.layout.key_item, this);
		key = (ImageView) findViewById(R.id.key_num);
		key_icon = (ImageView) findViewById(R.id.key_icon);

		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.KeyItem);
		int num_src = array.getResourceId(R.styleable.KeyItem_key_num, 0);
		int icon_src = array.getResourceId(R.styleable.KeyItem_key_icon, 0);
		key.setImageResource(num_src);
		key_icon.setImageResource(icon_src);


	}

	public void setKeycode(int keycode) {
		this.keycode = keycode;
		String pkg = preManager.getKeyPackage(keycode);
		if(pkg != null) {
			Application app = ApplicationUtil.doApplication(getContext(), pkg);
			if(app != null) {
				setApplication(app);
			}
		}
	}

	public void setApplication(Application app) {
		key_icon.setImageDrawable(app.getIcon());

	}

	public int getKeycode() {
		return this.keycode;
	}


	public void removeApplication() {
		key_icon.setImageResource(R.drawable.key_add);
		preManager.removeKeyPackage(keycode);
	}



}
