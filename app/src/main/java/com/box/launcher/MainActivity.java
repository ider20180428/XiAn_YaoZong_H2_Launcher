package com.box.launcher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.box.background.ConfigService;
import com.box.background.Product;
import com.box.fragments.BaseFragment;
import com.box.fragments.TVLive;
import com.box.views.StateBar;
import com.box.views.StateBar2;
import com.ider.launcher.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import box.cleansystem.StartActivity;
import box.database.DatabaseManager;
import box.utils.NetUtil;
import box.utils.PreferenceManager;

//import com.box.fragments.Education;
//import com.box.fragments.Game;
//import com.box.fragments.Health;
//import com.box.fragments.Tools;
//import com.box.fragments.Videos;

public class MainActivity extends Activity {

    private String TAG = "launcher_mini";
    private String TAG_SERVICE = "ConfigService";
    private boolean DEBUG = true;
    private PreferenceManager preferenceManager;
    private PackageManager pkgManager;
    private DatabaseManager dbManager;
    // ==================views===================
    private StateBar2 stateBar;
    //	TabsBar tabsBar;
    // fragments
    private BaseFragment[] fragments = new BaseFragment[1];
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int currentIndex;
    private FrameLayout main;
    private ConfigService configService; // ��̨���÷���
    boolean firstRun = false;
    private String delpack = null;
    private List<String> tvliveData;
    public static boolean screenSaveroutUpload = false; // �յ������˳��Ĺ㲥�ع������Ϊtrue����ֹ�˳�����onResume�����ع�
    public boolean stopBack = false; // ����onStopʱ���Ϊtrue,��onStop����onResumeʱ�����ع⣬��ֹonCreate����

    private ImageView imageViewTV,imageViewFilm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = PreferenceManager.getInstance(this);
        fragmentManager = getFragmentManager();
        pkgManager = getPackageManager();
        dbManager = DatabaseManager.getInstance(this);

        preferenceManager.setLocalServiceVersion(-1);

        if (preferenceManager.isFirstRun()) {
            firstRun = true; // ��һ�ο���
            ApplicationUtil.setDefaultTagPackages(this);
            preferenceManager.setFirstRun(false);
//			preferenceManager.putString("100", "com.elinkway.tvlive2");
//			preferenceManager.putString("101", "cn.dolit.wenzhoutv");
        }
        Log.i("vendorid", String.valueOf(Product.getVendorId(this)));

        setContentView(R.layout.activity_main);
        makeCachePath(); // ����ͼƬ����·��
//		if(preferenceManager.ifConfigServiceOn()) {
//			bindConfigService();
//		}
//		if (ifBindConfigService()) {
//			bindConfigService();
//		}
        registReceivers();// �ع�һ��
        initViews();
        // showFragment(0);
//		startbootlauncher();
        AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        AppWidgetProviderInfo appWidget = mAppWidgetManager
                .getAppWidgetInfo(appWidgetId);

        List<AppWidgetProviderInfo> providers = mAppWidgetManager
                .getInstalledProviders();
        final int providerCount = providers.size();
        for (int i = 0; i < providerCount; i++) {
            ComponentName provider = providers.get(i).provider;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void startbootlauncher() {
        String pkgName = preferenceManager.getBootPackage();
        System.out.println("----------------pkg++++zxczx" + pkgName);
        ApplicationUtil.startApp(this, pkgName);


    }

    public void initViews() {
        main = (FrameLayout) findViewById(R.id.main_bg);
        imageViewTV = (ImageView)findViewById(R.id.main_tv_imageview);
        imageViewFilm = (ImageView)findViewById(R.id.main_film_imageview);
        imageViewTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"打开电视",Toast.LENGTH_SHORT).show();
//                launchApp("com.qclive.tv");
                /**HDP电视直播*/
//hdpfans.com/hdp.player.LivePlayerNew
                launchApp(new ComponentName("hdpfans.com","hdp.player.LivePlayerNew"));

            }
        });
        imageViewFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,"打开电影",Toast.LENGTH_SHORT).show();
                launchApp("com.yidian.calendar");
            }
        });
//        setingBg();
        stateBar = (StateBar2) findViewById(R.id.main_state_bar);
//		tabsBar = (TabsBar) findViewById(R.id.tabs_bar);
//        initFragments();
//        openFragment(0);
    }

    public void setingBg() {
        String resID = preferenceManager.getPackage("resID");
        System.out.println("----------resID" + resID);
        if (resID != null) {
            main.setBackgroundResource(Integer.parseInt(resID));
        } else {
            main.setBackgroundResource(R.drawable.backzxy2);
        }
    }

    /**
     * ��ʼ������fragment
     */
    public void initFragments() {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragments[0] = TVLive.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[0]);
//		fragments[1] = Videos.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[1]);
//		fragments[2] = Education.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[2]);
//		fragments[3] = Game.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[3]);
//		fragments[4] = Health.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[4]);
//		fragments[5] = Tools.newInstance(R.layout.base_fragment);
//		fragmentTransaction.add(R.id.fragment_container, fragments[5]);
        fragmentTransaction.commit();
    }

    /**
     * ��ʾfragment
     *
     * @param index Ҫ��ʾ������
     */
    public void showFragment(final int index) {
        hideAllFragments();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragments[index]);
        fragmentTransaction.commit();
        currentIndex = index;
//
//		new Handler().postDelayed(new Runnable() {
//
//			@Override
//			public void run() {
//				fragments[index].setGridFocusable();
//			}
//		}, 200);
    }

    public void openFragment(int index) {
        hideAllFragments();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragments[index]);
        fragmentTransaction.commit();
        currentIndex = index;
    }

    public void hideAllFragments() {
        fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            fragmentTransaction.hide(fragments[i]);
        }
        fragmentTransaction.commit();
    }

    public void bindConfigService() {
        Log.i(TAG_SERVICE, "START bind config service");
        Intent intent = new Intent(MainActivity.this, ConfigService.class);
        bindService(intent, configServiceConnection, Context.BIND_AUTO_CREATE);

    }

    ServiceConnection configServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindConfigService();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG_SERVICE, "bind config service success");
            ConfigService.ConfigBinder binder = (ConfigService.ConfigBinder) service;
            // ConfigService ʵ��
            configService = binder.getService();
            if (configService == null) {
                return;
            }
            configService.readLocalConfig();
            if (NetUtil.isNetworkAvailable(MainActivity.this)) {
                configService.checkUpdate();
            }

        }
    };

    // ����Ƿ�ָ���������
    public boolean ifBindConfigService() {

        int count = preferenceManager.getBootCount();
        // return count >= 10;
        return true; // for test;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stateBar.getWeather();
        System.out.println("------------onResume");
//        setingBg();
        if (configService != null) {
            configService.checkUpdate();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBack = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistReceivers();

    }

    public void registReceivers() {

        IntentFilter filter;
        // ����Ĺ㲥
        filter = new IntentFilter();
        filter.addAction(NetUtil.CONNECTIVITY_CHANGE);
        filter.addAction(NetUtil.RSSI_CHANGE);
        filter.addAction(NetUtil.WIFI_STATE_CHANGE);
        registerReceiver(netReceiver, filter);

        // ϵͳ֪ͨ�Ĺ㲥
        filter = new IntentFilter();
        filter.addAction(StateBar.NOTIFY_COUNT);
        registerReceiver(notifyReceiver, filter);

        // ���u�̹㲥
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mediaReciever, filter);

        // Ӧ�ó���װ��ж�ع㲥
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        filter.addDataScheme("package");
        registerReceiver(packageReceiver, filter);

        // ��̨����Ӧ�ù㲥
        filter = new IntentFilter();
        filter.addAction(ConfigService.TAG_ACTION_HASUPDATE);
        registerReceiver(configReceiver, filter);
    }

    /**
     * ����㲥
     */
    public void unRegistReceivers() {
        try {
            unregisterReceiver(netReceiver);
            unregisterReceiver(notifyReceiver);
            unregisterReceiver(mediaReciever);
            unregisterReceiver(packageReceiver);
            unregisterReceiver(configReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ��̨����㲥
    BroadcastReceiver configReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            ArrayList<String> tags = bundle
                    .getStringArrayList(ConfigService.UPATE_TAGS);
            System.out.println("----------------tags" + tags.size() + "---999");
            tvliveData = new ArrayList<String>();
//			videoData = new ArrayList<String>();
//			gameData = new ArrayList<String>();
//			collectData = new ArrayList<String>();
//			recentData = new ArrayList<String>();
//			toolsData = new ArrayList<String>();

            for (String tag : tags) {
                try {
                    int tagInt = Integer.parseInt(tag);
                    switch (tagInt / 100 - 1) {
                        case 0:
                            tvliveData.add(tag);
                            break;
//					case 1:
//						videoData.add(tag);
//						break;
//					case 2:
//						gameData.add(tag);
//						break;
//					case 3:
//						collectData.add(tag);
//						break;
//					case 4:
//						recentData.add(tag);
//						break;
//					case 5:
//						toolsData.add(tag);
//						break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    Log.i(TAG_SERVICE,
                            "tag can not be parsed to integer, please check your server data !!!!!!");
                }
            }
            System.out.println("--------------tvliveData" + tvliveData.size());
            fragments[0].updateData(tvliveData);
//			fragments[1].updateData(videoData);
//			fragments[2].updateData(gameData);
//			fragments[3].updateData(collectData);
//			fragments[4].updateData(recentData);
            // fragments[5].updateData(toolsData);
        }
    };

    public ConfigService getConfigService() {
        return this.configService;
    }

    /**
     * ����㲥
     */
    BroadcastReceiver netReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DEBUG)
                Log.i(TAG, "�������㲥====>" + intent.getAction());
            String action = intent.getAction();
            if (NetUtil.CONNECTIVITY_CHANGE.equals(action)) {

//				stateBar.updateNetState();
                if (NetUtil.isNetworkAvailable(context)) {
                    stateBar.getWeather();
                    if (configService != null) {
                        configService.checkUpdate();
                    }
                }
            }
        }
    };
    /**
     * ֪ͨ�㲥
     */
    BroadcastReceiver notifyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras.containsKey("nitify.count")) {
                // ֪ͨ��
                int mReceiverCount = intent.getIntExtra("nitify.count", 0);
                Log.d(TAG, "====MainActivity=====notify Count===="
                        + mReceiverCount);

            }
        }
    };

    /**
     * ý����ع㲥
     */
    BroadcastReceiver mediaReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            } else {

            }
        }
    };

    Handler handler = new Handler();
    /**
     * Ӧ�ð�װ��ж�ع㲥
     */
    BroadcastReceiver packageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            // ���¹㲥
            if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                if (DEBUG)
                    Log.i(TAG, "package   replaced");
                String data = intent.getDataString();
                String packgename = data.substring(data.indexOf(":") + 1,
                        data.length());

            } // ж�ع㲥
            else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                if (DEBUG)
                    Log.i(TAG, "package  removed");
                String data = intent.getDataString();
                String pkgName = data.substring(data.indexOf(":") + 1,
                        data.length());
                for (int i = 0; i < fragments.length - 1; i++) {
                    fragments[i].checkPackageRemove(pkgName);
                }

            } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                Log.i(TAG, "onReceive: 2354655651656165");
                String packageName = intent.getData().getSchemeSpecificPart();
                checkInstall(packageName);
            }
        }
    };

    public void checkInstall(String p) {
        if (p.equals("com.linkin.tv") || p.equals("hdpfans.com") ||
                p.equals("com.booslink.Wihome_videoplayer3") || p.equals("com.vst.live") || p.equals("com.cloudmedia.videoplayer")
                || p.equals("com.xiaojie.tv")
                ) {
            if (preferenceManager.getPackage("100") == null) {
                Log.i(TAG, "checkInstall: 00000000");
                preferenceManager.putString("100", p);
            }
        }
        if (p.equals("com.gitvvideo.yidiankeji ") || p.equals("cn.beevideo") ||
                p.equals("com.yidian.calendar") || p.equals("com.cibn.tv") || p.equals("com.moretv.android")
                || p.equals("net.myvst.v2") || p.equals("com.starcor.mango")
                ) {
            if (preferenceManager.getPackage("101") == null) {
                Log.i(TAG, "checkInstall: 11111111111");
                preferenceManager.putString("101", p);
            }
        }
    }

    /**
     * �жϵ�ǰӦ���Ƿ���ǰ̨��ʾ
     *
     * @param activity
     * @return
     */
    public boolean isTopActivity(Activity activity) {
        String pkgName = this.getPackageName();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (taskInfos.size() > 0) {
            if (pkgName.equals(taskInfos.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * ���������ļ���
     */
    public void makeCachePath() {
        File file = new File(Constant.CACHE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String getVerName() {
        try {
            PackageInfo info = pkgManager.getPackageInfo(this.getPackageName(),
                    0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";

    }

    public String getVerCode() {

        try {
            PackageInfo info = pkgManager.getPackageInfo(this.getPackageName(),
                    0);
            int code = info.versionCode;
            return String.valueOf(code);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    int config_pwd = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("MINIKEYCODE", "keycode=" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1
                || keyCode == KeyEvent.KEYCODE_2
                || keyCode == KeyEvent.KEYCODE_3
                || keyCode == KeyEvent.KEYCODE_4
                || keyCode == KeyEvent.KEYCODE_5
                || keyCode == KeyEvent.KEYCODE_6
                || keyCode == KeyEvent.KEYCODE_7
                || keyCode == KeyEvent.KEYCODE_8
                || keyCode == KeyEvent.KEYCODE_9
                || keyCode == KeyEvent.KEYCODE_KANA
                || keyCode == KeyEvent.KEYCODE_RO) {

            if (keyCode == KeyEvent.KEYCODE_6) {
                config_pwd++;
                handler.removeCallbacks(key6Start);
                handler.postDelayed(key6Start, 1000);
                if (config_pwd == 6) {
                    handler.removeCallbacks(key6Start);
                }
                return true;
            } else {
                config_pwd = 0;
                String pkg = preferenceManager.getKeyPackage(keyCode);
                if (pkg != null) {
                    ApplicationUtil.startApp(this, pkg);
                    return true;
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_EISU) {
            startapplist();
        } else if (keyCode == KeyEvent.KEYCODE_YEN) {
            setting();
        } else if (keyCode == KeyEvent.KEYCODE_MINUS) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.ider.factorytest", "com.ider.factorytest.MainActivity"));
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    Runnable key6Start = new Runnable() {
        @Override
        public void run() {
            config_pwd = 0;
            String pkg = preferenceManager.getKeyPackage(KeyEvent.KEYCODE_6);
            if (pkg != null) {
                ApplicationUtil.startApp(MainActivity.this, pkg);
            }
        }
    };

    // һ������
    public void cleanUp(View view) {
        stopBack = true;
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
    }

    // WIFI�ȵ�
    public void wifiAp(View view) {
        String tag = (String) view.getTag();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.network.wifi.WifiApSettingActivity"));
        startActivity(intent);
    }

    // ��������
    public void bootChooser() {
        Intent intent = new Intent(this, BootChooser.class);
        startActivity(intent);
    }

    // �ļ�����
    public void fileManager() {
        Intent intent = pkgManager
                .getLaunchIntentForPackage("com.android.rockchip"); // rk
        if (intent == null)
            intent = pkgManager
                    .getLaunchIntentForPackage("com.softwinner.TvdFileManager"); // allwinner
        if (intent == null) {
            intent = pkgManager.getLaunchIntentForPackage("com.fb.FileBrower"); // amlogic
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    // ����
    public void setting() {
//		Intent settingIntent = getPackageManager().getLaunchIntentForPackage(
//				"com.softwinner.tvdsetting");
//		if (settingIntent == null) {
//			settingIntent = getPackageManager().getLaunchIntentForPackage(
//					"com.android.settings");
//		}
//		if (settingIntent != null) {
//			startActivity(settingIntent);
//		}
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS); //ϵͳ����
        if (intent != null) {
            startActivity(intent);
        }
    }

    // ��������
    public void networkSetting() {
        Intent intent = null;
        if (Build.MODEL.startsWith("IDER_BBA4")) {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.rk_itvui.settings",
                    "com.rk_itvui.settings.network_settingnew"));
        } else {
            intent = new Intent("android.settings.WIFI_SETTINGS");
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    // ������
    public void startCalculator() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
        if (intent != null) {
            startActivity(intent);
        }
    }

    // �����б�
    public void startapplist() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AppListActivity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * ��ȡ�ͱ��浱ǰ��Ļ�Ľ�ͼ
     */
    public static Bitmap move;
    public static Bitmap keep;

    public void GetandSaveCurrentImage() {

        // ��ȡ��Ļ
        View decorview = getWindow().getDecorView();
        int keepPosition = 720 / 2 + Constant.commonAppTranLength / 2;
        decorview.setDrawingCacheEnabled(true);
        decorview.buildDrawingCache();
        Bitmap Bmp = Bitmap.createBitmap(decorview.getDrawingCache());
        decorview.destroyDrawingCache();

        if (move != null && !move.isRecycled()) {
            move.recycle();
            move = null;
        }
        if (keep != null && !keep.isRecycled()) {
            keep.recycle();
            keep = null;
        }

        if (Bmp != null && !Bmp.isRecycled()) {
            move = Bitmap.createBitmap(Bmp, 0, 0, Bmp.getWidth(), keepPosition);
            keep = Bitmap.createBitmap(Bmp, 0, keepPosition, Bmp.getWidth(),
                    Bmp.getHeight() - keepPosition);
        }

        Intent intent = new Intent(MainActivity.this, CommonApp.class);
        intent.putExtra("keep", keepPosition);

        startActivity(intent);

    }

    public Fragment getCurrentFragment() {
        return fragments[currentIndex];
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            int index = intent.getIntExtra("fragment", -1);
            if (index != -1) {
//				((LinearLayout) (tabsBar.findViewById(R.id.tab_line)))
//						.getChildAt(index).requestFocus();
            }
        }
    }

//	public int getCurrentTabId() {
//		LinearLayout line = (LinearLayout) tabsBar.findViewById(R.id.tab_line);
//		return line.getChildAt(currentIndex).getId();
//	}

    public void launchApp(String pkgName) {
        try {
            Intent intent = getPackageManager()
                    .getLaunchIntentForPackage(pkgName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchApp(ComponentName componentName) {
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
