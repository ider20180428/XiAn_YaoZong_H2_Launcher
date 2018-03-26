package com.box.launcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.ider.launcher.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import box.database.DatabaseManager;
import box.utils.PreferenceManager;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class ApplicationUtil {
	private static boolean DEBUG = false;
	private static String[] systemPackages = { "com.yunos.tv.alitvedu",
			"com.yunos.tvtaobao", "com.yunos.localzone",
			"com.yunos.tvhelperinstallguider", "com.yunos.tv.safehome",
			"com.kanboxtv.cloudphoto", "com.yunos.bluetooth",
			"com.yunos.lifeservice", "com.yunos.tv.alitvab",
			"com.yunos.chaoshi", "com.softwinner.TvdFileManager",
			"com.android.settings", "com.xiami.tv", "com.yunos.tv.defensor",
			"com.yunos.tv.appstore", "com.yunos.tv.videochat" };

	public static List<String> loadAllApplication(Context context) {
		PackageManager pm = context.getPackageManager();
		List<String> apps = new ArrayList<String>();
		Intent main = new Intent(Intent.ACTION_MAIN, null);
		main.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolves = pm.queryIntentActivities(main, 0);

		Collections.sort(resolves, new ResolveInfo.DisplayNameComparator(pm));
		for (int i = 0; i < resolves.size(); i++) {
			ResolveInfo info = resolves.get(i);
			String pkgName = info.activityInfo.applicationInfo.packageName;
			if(!pkgName.equals("com.ider.launcher")&&!pkgName.equals("com.softwinner.update")){
				apps.add(pkgName);
			}
		}
//		apps.add(Constant.TOOL_CLEAN_MASTER);
//		apps.add(Constant.TOOL_HOTKEY);
		//apps.add(arg0)
		return apps;
	}
	public static List<String> loadAllApplicationtwo(Context context) {
		PackageManager pm = context.getPackageManager();
		List<String> apps = new ArrayList<String>();
		Intent main = new Intent(Intent.ACTION_MAIN, null);
		main.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolves = pm.queryIntentActivities(main, 0);

		Collections.sort(resolves, new ResolveInfo.DisplayNameComparator(pm));
		for (int i = 0; i < resolves.size(); i++) {
			ResolveInfo info = resolves.get(i);
			String pkgName = info.activityInfo.applicationInfo.packageName;
			if(!pkgName.equals("com.ider.launcher")&&!pkgName.equals("com.softwinner.update")){
				apps.add(pkgName);
			}

		}
		apps.add(Constant.TOOL_CLEAN_MASTER);
		apps.add(Constant.TOOL_HOTKEY);
		apps.add(Constant.TOOL_BGCH);
		apps.add(Constant.TOOL_QUICK);
		//apps.add(arg0)
		return apps;
	}

	public boolean isSystemApp(Application app) {
		for (int i = 0; i < systemPackages.length; i++) {
			if (systemPackages[i].equals(app.getPackageName())) {
				return true;
			}
		}
		return false;
	}


	public List<String> doApps(Context context, String type) {
		DatabaseManager dbManager = DatabaseManager.getInstance(context);
		List<String> list = dbManager.findApplicationByType(type);
		return list;
	}


	public static void startApp(Context context, Application app) {
		PackageManager pm = context.getPackageManager();
		String pkgName = app.getPackageName();
		Intent intent = pm.getLaunchIntentForPackage(pkgName);
		if (intent != null) {
			context.startActivity(intent);
		}
	}


	public static void startApp(Context context, String pkgName) {
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(pkgName);
		if (intent != null) {
			context.startActivity(intent);
		}
	}


	public static String getApkPackageName(Context context, File apkFile) {
		String packageName = null;
		try {
			PackageInfo info = context.getPackageManager()
					.getPackageArchiveInfo(apkFile.getAbsolutePath(),
							PackageManager.GET_ACTIVITIES);
			packageName = info.packageName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageName;
	}


	public static Application doApplication(Context context, String pkgName) {
		
		if (!pkgName.equals(Constant.ADD_PACKAGE) && pkgName != null) {
			PackageManager pm = context.getPackageManager();
			Application app = null;
			ApplicationInfo info;
			try {
				info = pm.getApplicationInfo(pkgName, 0);
				app = new Application(pkgName);
				app.setIntent(pm.getLaunchIntentForPackage(pkgName));
				app.setLabel(info.loadLabel(pm).toString());
				app.setIcon(info.loadIcon(pm));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				app = doAddApplication(context);
			}
			return app;
		} else {
			return doAddApplication(context);
		}

	}

	public static void installApkFile(Context context, File apkFile) {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}



	public static Application doAddApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.Add));
		app.setIcon(context.getResources().getDrawable(
				R.drawable.replce_app_default_logo));
		app.setPackageName(Constant.ADD_PACKAGE);
		return app;
	}

	public static Application doAddApplicationtwo(Context context,String lab) {
		Application app = new Application();
		app.setLabel(lab);
		app.setIcon(context.getResources().getDrawable(
				R.drawable.replce_app_default_logo));
		app.setPackageName(Constant.ADD_PACKAGE);
		return app;
	}
	public static Application doAddApplicationtwo(Context context,String pkg,String lab,String iconurl) {
		Application app = new Application();
		app.setLabel(lab);
		app.setTage(iconurl);
		app.setPackageName(pkg);
		return app;
	}

	public static Application doCleanApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.CleanMaster));
		app.setIcon(context.getResources().getDrawable(R.drawable.cleanup));
		app.setPackageName(Constant.TOOL_CLEAN_MASTER);
		return app;
	}


	public static Application doHotKeyApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.HotKey));
		app.setIcon(context.getResources().getDrawable(R.drawable.hotkey));
		app.setPackageName(Constant.TOOL_HOTKEY);
		return app;
	}


	public static Application doBootstartApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.BootStart));
		app.setIcon(context.getResources().getDrawable(R.drawable.boot_start));
		app.setPackageName(Constant.TOOL_BOOT_START);
		return app;
	}




	public static Application doFileManagerApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.FileManager));
		app.setIcon(context.getResources().getDrawable(R.drawable.file_manager));
		app.setPackageName(Constant.TOOL_FILE_MANAGER);
		return app;
	}


	public static Application doNetworkApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.NetWorkSetting));
		app.setIcon(context.getResources().getDrawable(
				R.drawable.network_settings));
		app.setPackageName(Constant.TOOL_NETSETTINGS);
		return app;
	}


	public static Application doAppListApplication(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.AppList));
		app.setIcon(context.getResources().getDrawable(R.drawable.app_list));
		app.setPackageName(Constant.TOOL_APPLIST);
		return app;
	}
	

	public static Application doAppInstaller(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.AppInstaller));
		app.setIcon(context.getResources().getDrawable(R.drawable.installer));
		app.setPackageName(Constant.TOOL_APK_INSTALLER);
		return app;
	}


	public static Application doSettings(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.Settings));
		app.setIcon(context.getResources().getDrawable(R.drawable.item_tag_6a8_icon));
		app.setPackageName(Constant.TOOL_SETTING);
		return app;
	}

		public static Application dochangebg(Context context) {
			Application app = new Application();
			app.setLabel(context.getString(R.string.Changebg));
			app.setIcon(context.getResources().getDrawable(R.drawable.bgch));
			app.setPackageName(Constant.TOOL_BGCH);
			return app;
		}

		public static Application doQuick(Context context) {
			Application app = new Application();
			app.setLabel(context.getString(R.string.quicktab));
			app.setIcon(context.getResources().getDrawable(R.drawable.quicktab));
			app.setPackageName(Constant.TOOL_QUICK);
			return app;
		}	
	public static Application doCalculator(Context context) {
		Application app = new Application();
		app.setLabel(context.getString(R.string.Calculator));
		app.setIcon(context.getResources().getDrawable(R.drawable.jisuanqi));
		app.setPackageName(Constant.TOOL_CALCULATOR);
		return app;
	}
	


	public static void setDefaultTagPackages(Context context) {
		File file = new File("/system/preinstall");
		File[] apks = file.listFiles();
		if (apks != null && apks.length != 0) {
			for (File apk : apks) {
				String name = apk.getName();
				String tag = name.substring(0, name.lastIndexOf("."));
				String pkgName = getApkPackageName(context, apk);
				if (pkgName != null) {
					PreferenceManager.getInstance(context).putString(tag,
							pkgName);
				}
			}
		}
	}

}
