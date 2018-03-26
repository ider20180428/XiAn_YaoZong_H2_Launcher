package com.box.launcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcher.R;

import java.util.ArrayList;

import box.utils.PreferenceManager;

public class BgchangeActivity extends Activity {
	ImageView selector;
    private Gallery mGallery;
 //  private ImageView mImgView;
    FrameLayout fram;
    private ImageShowAdapter mAdapter = new ImageShowAdapter(this);
    boolean select = false;
    PreferenceManager preManager;
    int resID = 0;
    TextView enter_bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgchange);
        selector = (ImageView) findViewById(R.id.app_item_selector);
        mGallery = (Gallery) findViewById(R.id.gallery);
//        mImgView = (ImageView) findViewById(R.id.app_selector);
        preManager = PreferenceManager.getInstance(this);
        fram = (FrameLayout) findViewById(R.id.frambg);
        enter_bg = (TextView) findViewById(R.id.enter_bg);
        try {
            mGallery.setAdapter(mAdapter);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
            	Integer[] iView = mAdapter.getImagesId();
            	if(select==false){
            		select = true;
            		resID = iView[position%iView.length];
            		preManager.putString("resID", resID+"");
            		enter_bg.setText(R.string.bg_settings);
      //      		mImgView.setVisibility(View.VISIBLE);
            	}else{
            		select = false;
            		preManager.putString("resID", null);
            		enter_bg.setText(null);
//            		mImgView.setVisibility(View.INVISIBLE);
            	}
            }
        });
        mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				Integer[] iView = mAdapter.getImagesId();
				//v.setLayoutParams(new Gallery.LayoutParams(350,270));
//				mImgView.setImageDrawable(getResources().getDrawable(iView[position%iView.length]));
				fram.setBackgroundResource(iView[position%iView.length]);
				if(iView[position%iView.length]==resID){
					enter_bg.setText(R.string.bg_settings);
//					mImgView.setVisibility(View.VISIBLE);
				}else{
					enter_bg.setText(null);
//					mImgView.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        	
		});
        
    }

    public class ImageShowAdapter extends BaseAdapter{

        private Context mContext;
        private ArrayList<Integer> imgList = new ArrayList<Integer>();
        private ArrayList<Object> imgSize = new ArrayList<Object>();
        
        public ImageShowAdapter(Context c){
            mContext = c;
            
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ImageView i = new ImageView(mContext);
            i.setImageResource(mImagesId[position%mImagesId.length]);
            i.setLayoutParams(new Gallery.LayoutParams(210,190));
            return i;
        }
        
        public Integer[] getImagesId(){
            return mImagesId;
        }
        
        public void setImagesId(Integer[] mImagesId){
            this.mImagesId = mImagesId;
        }
        
        private Integer mImagesId[] = {
                R.drawable.backzxy1,
                R.drawable.backzxy2,
                R.drawable.backzxy3,
                R.drawable.backzxy4,
                R.drawable.backzxy5,
                R.drawable.backzxy6,
                R.drawable.backzxy7
        };
        
    }

}