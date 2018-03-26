package com.box.launcher;

import java.util.List;

import box.adapter.AppAdapter;
import box.utils.PreferenceManager;

import com.ider.launcher.R;
import com.box.views.FlyImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class BootChooser extends Activity {

	GridView grid;
	FlyImageView bord;
	List<String> allapps;
	PreferenceManager preManager;
	String bootPackage;
	AppAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boot_chooser);
		
		preManager = PreferenceManager.getInstance(this);
		
		findViewById(R.id.tv_chooser1).setVisibility(View.VISIBLE);
		grid = (GridView) findViewById(R.id.app_grid1);
		bord = (FlyImageView) findViewById(R.id.bord1);
		
		bootPackage = preManager.getBootPackage();
		allapps = ApplicationUtil.loadAllApplication(this);
		adapter = new AppAdapter(this, R.layout.add_item, allapps);
		adapter.setSelectPackage(bootPackage);
		grid.setAdapter(adapter);
		
		grid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ImageView icon = (ImageView) view
						.findViewById(R.id.app_item_icon);
				int[] location = new int[2];
				icon.getLocationInWindow(location);
				bord.flyTo(view.getWidth(), view.getHeight(), location[0]  -31, location[1] -135, R.drawable.all_app_selecter, 0);
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
				
				if(pkg.equals(bootPackage)) {
					view.findViewById(R.id.app_item_selector2).setVisibility(View.GONE);
					preManager.removeBootPackage();
					Toast.makeText(BootChooser.this, R.string.boot_removed, Toast.LENGTH_SHORT).show();
				} else {
					preManager.setBootPackage(pkg);
					Application app = ApplicationUtil.doApplication(BootChooser.this, pkg);
					String toast = String.format(getResources().getString(R.string.boot_app), app.getLabel());
					Toast.makeText(BootChooser.this, toast, Toast.LENGTH_SHORT).show();
				}

				BootChooser.this.finish();
				
			}
		});
	}
	
}
