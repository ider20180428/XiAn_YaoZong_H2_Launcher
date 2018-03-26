package com.box.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import com.ider.launcher.R;

import java.util.Calendar;

@SuppressWarnings("deprecation")
public class MyDigitalClock extends android.widget.DigitalClock {
	Calendar mCalendar;
	// private final static String mFormat =
	// "EEEE,MMMM-dd-yyyy hh:mm aa";//h:mm:ss aa
	private  String mFormat;
	private FormatChangeObserver mFormatChangeObserver;
	private Runnable mTicker;
	private Handler mHandler;
	private boolean mTickerStopped = false;
	private String m24 = "k:mm";
	private String m12 = "h:mm aa";

	public MyDigitalClock(Context context) {
		super(context);
		initClock(context);
	}

	public MyDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyDigitalClock);
		this.mFormat=(String)typedArray.getText(R.styleable.MyDigitalClock_format);
		typedArray.recycle();
		initClock(context);
	}

	private void initClock(Context context) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
				Settings.System.CONTENT_URI, true, mFormatChangeObserver);

	}

	/**
	 * 当此view附加到窗体上时调用该方法
	 * 次方法在onDraw方法之前调用，也就是view还没有画出来的时候，可以在此方法中去执行一些初始化的操作
	 */
	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();

		mHandler = new Handler();

		mTicker = new Runnable() {
			@Override
			public void run() {
				if (mTickerStopped) {
					return;
				}
				mCalendar.setTimeInMillis(System.currentTimeMillis());
				setText(DateFormat.format(mFormat, mCalendar));
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	private class FormatChangeObserver extends ContentObserver {

		public FormatChangeObserver() {
			super(new Handler());
		}

		@Override
		public void onChange(boolean selfChange) {

		}
	}

	// 根据系统时间格式调整格式
	public void setFormat() {
		if(get24HourMode()) {
			mFormat = m24;
		} else {
			mFormat = m12;
		}
	}

	/**
	 * Pulls 12/24 mode from system settings
	 */
	private boolean get24HourMode() {
		return android.text.format.DateFormat.is24HourFormat(getContext());
	}


}

