package com.box.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class TVideo extends VideoView {

	private int mVideoWidth =488;
    private int mVideoHeight =320;

    public TVideo(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
    }

    public TVideo(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
    }

    public TVideo(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
            int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
            setMeasuredDimension(width, height);
    }
    
    
}
