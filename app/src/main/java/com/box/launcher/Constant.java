package com.box.launcher;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;

@SuppressLint("SdCardPath")
public class Constant {

	public static final String LAUNCHER_CODE = "YD12";

	public static final String TAG_WEATHER = "056";
	public static final String TAG_CLEAN = "051";
	public static final String TAG_NOFITY = "052";
	public static final String TAG_SETTING = "053";
	public static final String TAG_NET = "054";
	public static final String TAG_USB = "055";


	public static final String TYPE_TVLIVE = "tvlive";
	public static final String TYPE_VIDEO = "video";
	public static final String TYPE_EDUCATION = "education";
	public static final String TYPE_GAME = "game";
	public static final String TYPE_HEALTH = "health";
	public static final String TYPE_TOOLS = "tools";
	public static final String TYPE_COMMON = "common";

	public static final String NET_PACKAGE = "android.provider.Settings.ACTION_WIRELESS_SETTINGS";
	public static final String ADD_PACKAGE = "app.add";


	// 工具
	public static final String TOOL_HOTKEY = "app.hotkey";
	public static final String TOOL_BOOT_START = "app.bootstart";
	public static final String TOOL_SETTING = "com.settings";
	public static final String TOOL_BGCH = "com.bgchange";
	public static final String TOOL_QUICK = "com.quicktab";
	public static final String TOOL_FILE_MANAGER = "com.ider.filemanager";
	public static final String TOOL_NETSETTINGS = "com.ider.netsettings";
	public static final String TOOL_APPLIST = "com.ider.applist";
	public static final String TOOL_CLEAN_MASTER = "app.clean";
	public static final String TOOL_APK_INSTALLER = "apk.installer";
	public static final String TOOL_CALCULATOR = "com.calculator";
	public static final String PKG_TV = "com.android.deskclock";
	public static final String PKG_MOVIE = "com.android.calendar";


	public static final int[] verticalSize = {360, 480};
	public static final int[] horizontalSize = {732, 480};
	public static final int PIC_WIDTH1 = 360;
	public static final int PIC_HEIGHT1 = 480;

	public static final String ACTION_INSTALLED = "ider.installed.apk";
	public static final String ACTION_INSTALL_OVER = "ider.install.over";
	public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getPath() + "/launcher/download";

	public static final int commonAppTranLength = 300;

	public static final String reset_file_path = Environment.getExternalStorageDirectory().getPath() + File.separator + "ref.conf";
	
	
}
