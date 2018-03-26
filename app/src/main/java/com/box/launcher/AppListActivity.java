package com.box.launcher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.launcher.R;

import java.util.List;

import box.adapter.AppAdapter;
import box.cleansystem.StartActivity;
import box.cleansystem.zzStartActivity;
import box.database.DatabaseManager;
import box.utils.PreferenceManager;

public class AppListActivity extends Activity {
	Context context = AppListActivity.this;
	GridView grid;
	ApplicationUtil appUtil;
	AppAdapter adapter;
	List<String> apps;
	Handler handler = new Handler();
	int uploadDelayMills = 2000;
	long oldTimeMills;
	TextView tip;
	int appposition=0;
	DatabaseManager dbManager;
	int startTag = 1001;
	AlertDialog  mAlertDialog ;
	PreferenceManager Preference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_list);
		dbManager = DatabaseManager.getInstance(context);
		appUtil = new ApplicationUtil();
		Preference =PreferenceManager.getInstance(this);
		adapter = new AppAdapter(context, R.layout.app_list_item, initApps());
		initViews();
		setListeners();

	}

	public void initViews() {
		grid = (GridView) findViewById(R.id.app_grid);
		grid.setAdapter(adapter);
		tip = (TextView) findViewById(R.id.uninstall_tip);

	}

	public List<String> getCustomApps() {
		List<String> list = dbManager
				.findApplicationByType(Constant.TYPE_COMMON);
		list.add(Constant.ADD_PACKAGE);
		return list;

	}

	@Override
	public void onResume() {
		super.onResume();
		registReceivers();
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			unregisterReceiver(packageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> initApps() {
		apps = ApplicationUtil.loadAllApplicationtwo(context);

		return apps;
	}

	BroadcastReceiver packageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			grid.setAdapter(new AppAdapter(context, R.layout.app_list_item,
					initApps()));
		}

	};

	public void registReceivers() {
		// screensaverReceiver = new ScreensaverReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("ACTION_SCREEN_SAVER");
		// registerReceiver(screensaverReceiver, filter);

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addAction("android.intent.action.PACKAGE_CHANGED");
		filter.addDataScheme("package");
		registerReceiver(packageReceiver, filter);
	}

	@Override
	protected void onPause() {

		// try {
		// unregisterReceiver(screensaverReceiver);
		// } catch (Exception e) {
		//
		// }

		super.onPause();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}


	  private void fileOrDirHandle(){

		  String[] itemList = {getResources().getString(R.string.app_clean),"  "+getResources().getString(R.string.app_uninstall)};        //file操作
	      ListAdapter mAdapter = new ArrayAdapter(context, R.layout.dialog_item, itemList);	
	      LayoutInflater inflater = LayoutInflater.from(AppListActivity.this);
	      View view = inflater.inflate(R.layout.meundialog, null);
	      ListView listview = (ListView)view.findViewById(R.id.dialog_list);
	      listview.setAdapter(mAdapter);
	      listview.setOnItemClickListener(meunlistener);
	      mAlertDialog  =  new AlertDialog.Builder(AppListActivity.this)
	            .show();
	      
	      mAlertDialog.setContentView(view); 
	      mAlertDialog.getWindow().setLayout(324, 250);
	}  
	
	  OnItemClickListener meunlistener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				mAlertDialog.cancel();
				if(position==10){
					if(!apps.get(appposition).equals(Preference.getPackage("bootlauncher"))){
					Preference.setBootPackage(apps.get(position));
					String toast = String.format(getResources().getString(R.string.boot_app),appUtil.doApplication(context, apps.get(appposition)).getLabel());
					Toast.makeText(AppListActivity.this, toast, Toast.LENGTH_SHORT).show();
					}else{
						Preference.putString("bootlauncher", null);
						String toast =  String.format((String) getResources().getString(R.string.boot_removed),appUtil.doApplication(context, apps.get(appposition)).getLabel());
						Toast.makeText(AppListActivity.this, toast, Toast.LENGTH_SHORT).show();
					}
				}else if(position==0){
					Intent intent = new Intent(AppListActivity.this, zzStartActivity.class);
					startActivity(intent);
				}else if(position==1){
					uninstallApp(apps.get(appposition));
				}
			}
		};
	  
	public void setListeners() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String pkg = apps.get(position);
				if(pkg.equals(Constant.TOOL_CLEAN_MASTER)){
					Intent intent = new Intent(AppListActivity.this, StartActivity.class);
					startActivity(intent);
				}else if(pkg.equals(Constant.TOOL_HOTKEY)){
					Intent intent = new Intent(AppListActivity.this, FastKey.class);
					startActivity(intent);
				}else if(pkg.equals(Constant.TOOL_BGCH)){
					Intent intent = new Intent(AppListActivity.this, BgchangeActivity.class);
					startActivity(intent);
				}else if(pkg.equals(Constant.TOOL_QUICK)){
					Intent intent = new Intent(AppListActivity.this, QuickActivity.class);
					startActivity(intent);
				}
				else{
				Intent intent = getPackageManager().getLaunchIntentForPackage(
						pkg);
				startActivity(intent);
				}
			}

		});

//		grid.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				uninstallApp(apps.get(position));
//
//				return true;
//			}
//		});

		grid.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				appposition = position;
				if(apps.get(position).equals(Constant.TOOL_HOTKEY)||apps.get(position).equals(Constant.TOOL_QUICK)
					||apps.get(position).equals(Constant.TOOL_CLEAN_MASTER)||apps.get(position).equals(Constant.TOOL_BGCH))
					{
						tip.setText(R.string.AppListLongTip2);
					}else{
						tip.setText(R.string.AppListLongTip);
					}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//	return super.onKeyDown(keyCode, event);
		if(keyCode == KeyEvent.KEYCODE_MENU){
			fileOrDirHandle();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void uninstallApp(String pkg) {
		Uri uri = Uri.parse("package:" + pkg);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		startActivity(intent);

	}

}
