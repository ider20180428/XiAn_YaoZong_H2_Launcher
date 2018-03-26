package com.box.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ider.launcher.R;

@SuppressLint("Recycle")
public class TabButton extends RelativeLayout {
	private int iconResource;
	private int iconFocusResource;
	private int textResource;
	private ImageView icon;
	private TextView text;
	

	public TabButton(Context context) {
		super(context);
	}

	public TabButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.tab_button, this);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
		this.iconResource = typedArray.getResourceId(R.styleable.TabButton_icon, 0);
		this.iconFocusResource = typedArray.getResourceId(R.styleable.TabButton_icon_focus, 0);
		this.textResource = typedArray.getResourceId(R.styleable.TabButton_text, 0);
		initViews();
	
	}
	
	public void initViews() {
		icon = (ImageView) findViewById(R.id.tabbutton_icon);
		text = (TextView) findViewById(R.id.tabbutton_text);
		icon.setImageResource(iconResource);
		text.setText(textResource);
	}
	

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		if(gainFocus) {
			icon.setImageResource(iconFocusResource);
			text.setTextColor(Color.BLACK);
		} else {
			icon.setImageResource(iconResource);
			text.setTextColor(Color.WHITE);
		}
	}

}
