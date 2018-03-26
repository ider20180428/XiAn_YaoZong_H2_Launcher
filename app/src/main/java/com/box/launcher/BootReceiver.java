package com.box.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import box.utils.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
	

		PreferenceManager.getInstance(context).updateBootCount();
		startBootApp(context);
	
	}
	
	public void startBootApp(Context context) {
		PreferenceManager preferenceManager = PreferenceManager.getInstance(context);
		String pkg = preferenceManager.getBootPackage();
		
		
		if (pkg != null) {
			Intent in = context.getPackageManager().getLaunchIntentForPackage(pkg);
			if (in != null) {
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(in);
			}
		}
	}

	

}
