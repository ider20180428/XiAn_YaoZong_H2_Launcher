package com.box.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.box.background.TagConfig;
import com.box.views.ATool;
import com.box.views.FlyImageView;
import com.box.views.MyDialog;
import com.ider.launcher.R;

import java.util.List;

import box.adapter.AppAdapter;
import box.utils.PreferenceManager;

public class QuickActivity extends Activity {
	ATool tool[] = new ATool[2];
	PreferenceManager preManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick);
		tool[0] = (ATool) findViewById(R.id.tvmini);
		tool[1] = (ATool) findViewById(R.id.moviemini);
		tool[0].setTag("100");
		tool[0].labset();
		tool[1].setTag("101");
		tool[1].labset();
		preManager = PreferenceManager.getInstance(this);
		String pkg = preManager.getPackage("100");
		if(pkg == null){
			tool[0].setApplicaton(ApplicationUtil.doAddApplicationtwo(this, "请选择电视应用"));
		}else{
			tool[0].setApplicaton(ApplicationUtil.doApplication(this, pkg));
		}
		String pkg2 = preManager.getPackage("101");
		if(pkg2 == null){
			tool[1].setApplicaton(ApplicationUtil.doAddApplicationtwo(this, "请选择电影应用"));
		}else{
			tool[1].setApplicaton(ApplicationUtil.doApplication(this, pkg2));
		}
		for (int i = 0; i < 2; i++) {
			tool[i].setFocusable(true);
			tool[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String tag = (String) v.getTag();
					showAddDialog(tag);
				}
			});

			tool[i].setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasfocus) {
					if(hasfocus){
						v.setBackgroundResource(R.drawable.app_gridview_bg08);
					}else{
						v.setBackgroundResource(0);
					}
				}
			});
		}
	}
	public void showAddDialog(final String tag) {
		final MyDialog dialog = new MyDialog(QuickActivity.this,
				R.layout.add_dialog, R.style.MyDialog);
		GridView gridview = (GridView) dialog.getWindow().getDecorView()
				.findViewById(R.id.app_grid);
		final FlyImageView bord = (FlyImageView) dialog.getWindow()
				.getDecorView().findViewById(R.id.bord);
		final List<String> list = ApplicationUtil
				.loadAllApplication(QuickActivity.this);
		gridview.setAdapter(new AppAdapter(QuickActivity.this, R.layout.add_item,
				list));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String pkg = list.get(position);
				TagConfig config = new TagConfig(pkg);
				preManager.putString(tag, pkg);

				if(tag.equals("100")){
					tool[0].setApplicaton(ApplicationUtil.doApplication(QuickActivity.this, pkg));
					preManager.setKeyPackage(KeyEvent.KEYCODE_CALENDAR, pkg);
				}if(tag.equals("101")){
					tool[1].setApplicaton(ApplicationUtil.doApplication(QuickActivity.this, pkg));
					preManager.setKeyPackage(KeyEvent.KEYCODE_MUSIC, pkg);
				}
				dialog.cancel();
			}


		});
		gridview.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				ImageView icon = (ImageView) view
						.findViewById(R.id.app_item_icon);
				int[] location = new int[2];
				icon.getLocationInWindow(location);
				bord.setVisibility(View.VISIBLE);
				bord.flyTo(view.getWidth(), view.getHeight(), location[0] - 31,
						location[1] - 35);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		dialog.show();
	}
}
