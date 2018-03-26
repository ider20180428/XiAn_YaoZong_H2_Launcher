package com.box.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.box.views.FlyImageView;
import com.box.views.KeyItem;
import com.box.views.MyDialog;
import com.ider.launcher.R;

import java.util.ArrayList;
import java.util.List;

import box.adapter.AppAdapter;
import box.utils.PreferenceManager;

public class FastKey extends Activity implements OnFocusChangeListener, OnClickListener, OnLongClickListener {
	
	KeyItem[] keys = new KeyItem[12];
	FlyImageView selecter;
	PreferenceManager preManager;
	MyDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fast_key);
		preManager = PreferenceManager.getInstance(this);
		
		selecter = (FlyImageView) findViewById(R.id.key_selector);
		keys[0] = (KeyItem) findViewById(R.id.key_1);
		keys[1] = (KeyItem) findViewById(R.id.key_2);
		keys[2] = (KeyItem) findViewById(R.id.key_3);
		keys[3] = (KeyItem) findViewById(R.id.key_4);
		keys[4] = (KeyItem) findViewById(R.id.key_5);
		keys[5] = (KeyItem) findViewById(R.id.key_6);
		keys[6] = (KeyItem) findViewById(R.id.key_7);
		keys[7] = (KeyItem) findViewById(R.id.key_8);
		keys[8] = (KeyItem) findViewById(R.id.key_9);
		keys[9] = (KeyItem) findViewById(R.id.key_0);
		keys[10] = (KeyItem) findViewById(R.id.key_tv);
		keys[11] = (KeyItem) findViewById(R.id.key_vod);
		keys[0].setKeycode(KeyEvent.KEYCODE_1);
		keys[1].setKeycode(KeyEvent.KEYCODE_2);
		keys[2].setKeycode(KeyEvent.KEYCODE_3);
		keys[3].setKeycode(KeyEvent.KEYCODE_4);
		keys[4].setKeycode(KeyEvent.KEYCODE_5);
		keys[5].setKeycode(KeyEvent.KEYCODE_6);
		keys[6].setKeycode(KeyEvent.KEYCODE_7);
		keys[7].setKeycode(KeyEvent.KEYCODE_8);
		keys[8].setKeycode(KeyEvent.KEYCODE_9);
		keys[9].setKeycode(KeyEvent.KEYCODE_0);
		keys[10].setKeycode(KeyEvent.KEYCODE_KANA);
		keys[11].setKeycode(KeyEvent.KEYCODE_RO);
		
		for(int i = 0; i < keys.length; i++) {
			keys[i].setOnFocusChangeListener(this);
			keys[i].setOnClickListener(this);
			keys[i].setOnLongClickListener(this);
		}
		
		
	
	}
	
	
	
	@Override
	protected void onDestroy() {
		if(dialog!=null){
			dialog.dismiss();
		}
		super.onDestroy();
	}



	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		if(hasFocus) {
			int[] location = new int[2];
			view.getLocationInWindow(location);
			
			selecter.flyTo(view.getWidth(), view.getHeight(), location[0], location[1], R.drawable.key_selecter, 100);
			
		}
	}
	
	@Override
	public void onClick(View view) {
		showAddDialog((KeyItem) view);
	}
	
	@Override
	public boolean onLongClick(View view) {
		
		((KeyItem) view).removeApplication();
			
		return true;
	}
	
	public void showAddDialog(final KeyItem item) {
		final List<String> allapps = ApplicationUtil.loadAllApplication(this);
		List<String> app2=findall();
		List<String> allap=insectApp(allapps, app2);
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
				bord.flyTo(view.getWidth(), view.getHeight(), location[0] - 31, location[1] - 35, R.drawable.all_app_selecter, 0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pkg = allapps.get(position);
				Application app = ApplicationUtil.doApplication(FastKey.this, pkg);
				item.setApplication(app);
				
				preManager.setKeyPackage(item.getKeycode(), pkg);
				dialog.cancel();

			}
		});
	}
	
	public  List<String>  findall(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < keys.length; i++) {
			//Application app =keys[i].getContext().getApplicationInfo()
			String pkgname = preManager.getKeyPackage(keys[i].getKeycode());
			if(pkgname!=null&&!"".equals(pkgname)){
				list.add(pkgname);
			}
		}
		return list;
	}
	
	//ȥ������ӵ�apk	
		public List<String> insectApp(List<String> app1,List<String>app2){
			for (int i = 0; i < app2.size(); i++) {
				for (int j = 0; j < app1.size(); j++) {
					if(app2.get(i).equals(app1.get(j))){
						app1.remove(app1.get(j));
					}
				}
			}
			return app1;
		}
	
}
