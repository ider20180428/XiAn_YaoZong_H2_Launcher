package com.box.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.box.background.ConfigService;
import com.box.background.TagConfig;
import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.box.launcher.Constant;
import com.box.launcher.MainActivity;
import com.box.views.ATool;
import com.box.views.FlyImageView;
import com.box.views.MyDialog;
import com.ider.launcher.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import box.adapter.AppAdapter;
import box.adapter.CustomAdapter;
import box.database.DatabaseManager;
import box.utils.PreferenceManager;

//import com.box.views.TabsBar;

public class BaseFragment extends Fragment {
	String TAG = "base";
	// 布局
	protected int layout;
	// 布局对应的View
	protected View view;
	CustomAdapter adapter;
	List<TagConfig> data;
	GridView gridView;
	FlyImageView bord; // 焦点框
	//	TabsBar tabBar;
	TextView tvtip;
	PreferenceManager preManager;
	boolean DEBUG = false,times =false;
	List<HashMap<String, String>> updateResults; // 可更新的坑位
	Handler handler = new Handler();
	String type;
	ConfigService service;
	int lastSelection = 0;
	ATool[] topTools;
	int itemWidth = 150;
	int size=0;
	private ImageView animationIV,animationIV2;
	private AnimationDrawable animationDrawable;
	FrameLayout bottom_an,top_an;
	ImageView mImag;
	//	ATool[] downTools;
//	 private SlidingDrawer sd;
//	 private ImageView iv;
	HorizontalScrollView hsv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(layout, container, false);
		initViews();
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.layout = getArguments().getInt("layout");
		preManager = PreferenceManager.getInstance(getActivity());

	}

	public void initViews() {
//		hsv = (HorizontalScrollView) view.findViewById(R.id.my_hsv);
//		bord = (FlyImageView) (getActivity().findViewById(R.id.focus_bord));
//		tabBar = (TabsBar) (getActivity().findViewById(R.id.tabs_bar));
//		tvtip = (TextView) view.findViewById(R.id.shortcut_tip);
//		gridView = (GridView) (view.findViewById(R.id.dishtype));
//		animationIV = (ImageView) view.findViewById(R.id.anim_iv);
//		animationIV.setOnKeyListener(listener);
//		animationIV2 = (ImageView) view.findViewById(R.id.anim_iv2);
//		bottom_an = (FrameLayout) view.findViewById(R.id.bottom_an);
//		top_an = (FrameLayout) view.findViewById(R.id.top_an);
//		startanim();
//		startanim2();
//		sd = (SlidingDrawer)( view.findViewById(R.id.sliding));
//		  iv = (ImageView)( view.findViewById(R.id.imageViewIcon));
//		gridView.setFocusable(false);

		data = getUserData();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//			//	gridView.setVisibility(View.INVISIBLE);
//			}
//		}, 500);
//		changeGrid();
		topTools = new ATool[3];
		topTools[0] = (ATool) view.findViewById(R.id.fragment1_item_s);
		topTools[0].setTag("100");
		topTools[0].requestFocus();
		topTools[1] = (ATool) view.findViewById(R.id.fragment1_item_a1);
		topTools[1].setTag("101");
		topTools[2] = (ATool) view.findViewById(R.id.fragment1_item_a2);
		topTools[2].setTag("102");
		topTools[2].setlayout();
//		topTools[3] = (ATool) view.findViewById(R.id.fragment1_custom_item_b1);
//		topTools[3].setTag("103");
//		topTools[3].setlayout();
//		topTools[4] = (ATool) view.findViewById(R.id.fragment1_custom_item_b2);
//		topTools[4].setTag("104");
//		topTools[5] = (ATool) view.findViewById(R.id.fragment1_custom_item_b3);
//		topTools[5].setTag("105");
//		topTools[6] = (ATool) view.findViewById(R.id.fragment1_custom_item_b4);
//		topTools[6].setTag("106");
//		topTools[7] = (ATool) view.findViewById(R.id.fragment1_custom_item_b5);
//		topTools[7].setTag("107");
		setListeners();
//		showapps();
//		animationIV.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasfocus) {
//				if(hasfocus){
//					System.out.println("---------------");
//					startAnimation();
//				}
//			}
//		});
	}


	private void showapps() {
		for (int i = 2; i < 4; i++) {
			String pkg = preManager.getPackage(topTools[i].getTag().toString());
			if(pkg==null){
				topTools[i].setApplicaton(ApplicationUtil.doAddApplication(getActivity()));
			}else{
				topTools[i].setApplicaton(ApplicationUtil.doApplication(getActivity(), pkg));
			}
		}
		topTools[4].setApplicaton(ApplicationUtil.doAppListApplication(getActivity()));
		topTools[5].setApplicaton(ApplicationUtil.doFileManagerApplication(getActivity()));
		topTools[6].setApplicaton(ApplicationUtil.doApplication(getActivity(),"com.zxy.yidian"));
		topTools[7].setApplicaton(ApplicationUtil.doSettings(getActivity()));
	}
//	public void startanim(){
//		animationIV.setImageResource(R.anim.animation1);
//		animationDrawable = (AnimationDrawable) animationIV.getDrawable();
//		animationDrawable.start();
//	}
//	public void startanim2(){
//		animationIV2.setImageResource(R.anim.animation2);
//		animationDrawable = (AnimationDrawable) animationIV2.getDrawable();
//		animationDrawable.start();
//	}
	public void setListeners() {
		for (int i = 0; i < topTools.length; i++) {
			topTools[i].setFocusable(true);
			topTools[i].setOnFocusChangeListener(focusChangeListener);
			topTools[i].setOnClickListener(onclicker);
			topTools[i].setOnKeyListener(listener);
			// topTools[i].backlan();
		}
//		topTools[2].setOnLongClickListener(longclick);
//		topTools[3].setOnLongClickListener(longclick);
//		gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//									   int position, long id) {
//				bord.setVisibility(View.INVISIBLE);
//				changecolor();
//				if(view!=null){
//					ATool to = (ATool) view;
//					to.setBackgroundResource(0);
////						to.showlan();
//				}
////					view.setBackgroundResource(R.drawable.shelectdown);
////					ATool to = (ATool) view;
////					to.showlan();
////					lastSelection = position;
////					int[] location = new int[2];
////					if (view != null) {
////						view.getLocationInWindow(location);
////						bord.flyTo(220, 146, location[0] - 10, location[1] - 10);
////					}
//				if(position>5){
//					int x =0;
//					x = (position-5)*(itemWidth+20);
//					hsv.smoothScrollTo(x, 0);
//				}else if(position==5){
//					hsv.smoothScrollTo(0, 0);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});

//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//									int position, long id) {
//				TagConfig config = data.get(position);
//				if (config.status == null) {
//					if (config.pkgName.equals(Constant.ADD_PACKAGE)) {
//						showAddDialog();
//					} else {
//						ApplicationUtil.startApp(getActivity(), config.pkgName);
//					}
//				} else {
//
//					Intent intent = getActivity().getPackageManager()
//							.getLaunchIntentForPackage(config.pkgName);
//					if (intent == null) {
//						((ATool) view).startDownload(type);
//					} else {
//						startActivity(intent);
//					}
//
//				}
//			}
//		});

//		gridView.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View view, boolean hasFocus) {
//
//				if (hasFocus) {
//					bord.setVisibility(View.INVISIBLE);
////						int[] location = new int[2];
////						View child = gridView.getChildAt(lastSelection);
////						if (child != null) {
////							child.getLocationInWindow(location);
////							bord.flyTo(220, 146, location[0] - 10, location[1] - 10);
////
////						}
//					startAnimation();
//				}
//			}
//		});

//		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//										   int position, long id) {
//				if (!type.equals(Constant.TYPE_TOOLS)) {
//					TagConfig config = data.get(position);
//					if(!config.pkgName.equals(Constant.ADD_PACKAGE)){
//						System.out.println("-------111");
//						data.remove(position);
//						size = size-1;
//						DatabaseManager.getInstance(getActivity()).delete(
//								config.pkgName, type);
////							scollto();
//						if(size==14){
//							System.out.println("-------222");
//							data.add(new TagConfig(Constant.ADD_PACKAGE));
//							size = size+1;
//						}
//						adapter.notifyDataSetChanged();
////							hsv.smoothScrollTo(0, 0);
////							scollto();
//					}
//				}
//				return true;
//			}
//		});
	}

	public void showAddDialog() {
		final MyDialog dialog = new MyDialog(getActivity(),
				R.layout.add_dialog, R.style.MyDialog);
		GridView gridview = (GridView) dialog.getWindow().getDecorView()
				.findViewById(R.id.app_grid);
//		final FlyImageView bord = (FlyImageView) dialog.getWindow()
//				.getDecorView().findViewById(R.id.bord);
		final List<String> list = ApplicationUtil
				.loadAllApplication(getActivity());
		gridview.setAdapter(new AppAdapter(getActivity(), R.layout.add_item,
				list));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String pkg = list.get(position);
				TagConfig config = new TagConfig(pkg);
				if (!data.contains(config)) {
					if (data.size() == 15) {
						if(data.get(data.size()-1).pkgName.equals(Constant.ADD_PACKAGE)){
							data.remove(data.size()-1);
							data.add(config);
							DatabaseManager.getInstance(getActivity())
									.insert(pkg, type);
							adapter.notifyDataSetChanged();
							Toast.makeText(getActivity(), "已经添加满15个应用了", Toast.LENGTH_SHORT).show();
							dialog.cancel();
							dialog.dismiss();
						}else {
							Toast.makeText(getActivity(), "已经添加满15个应用了", Toast.LENGTH_SHORT).show();
						}
					}else if(data.size() < 15){
						data.add(data.size()-1,config);
						DatabaseManager.getInstance(getActivity())
								.insert(pkg, type);
						size = size+1;
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(getActivity(), R.string.Already_add,
							Toast.LENGTH_SHORT).show();
				}
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



	public void showAddDialog(final String tag) {
		final MyDialog dialog = new MyDialog(getActivity(),
				R.layout.add_dialog, R.style.MyDialog);
		GridView gridview = (GridView) dialog.getWindow().getDecorView()
				.findViewById(R.id.app_grid);
		final FlyImageView bord = (FlyImageView) dialog.getWindow()
				.getDecorView().findViewById(R.id.bord);
		final List<String> list = ApplicationUtil
				.loadAllApplication(getActivity());
		gridview.setAdapter(new AppAdapter(getActivity(), R.layout.add_item,
				list));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String pkg = list.get(position);
				TagConfig config = new TagConfig(pkg);
				if (!data.contains(config)) {
					data.add(data.size() - 1, config);
					if (data.size() > 15) {
						data.remove(data.size() - 1);
					}
					DatabaseManager.getInstance(getActivity())
							.insert(pkg, type);
//					preManager.putString(tag, pkg);
					adapter.notifyDataSetChanged();
					showapps();
					dialog.cancel();
				} else {
					Toast.makeText(getActivity(), R.string.Already_add,
							Toast.LENGTH_SHORT).show();
				}
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

	public void showAddDialogtwo(final String tag) {
		final MyDialog dialog = new MyDialog(getActivity(),
				R.layout.add_dialog, R.style.MyDialog);
		GridView gridview = (GridView) dialog.getWindow().getDecorView()
				.findViewById(R.id.app_grid);
		final FlyImageView bord = (FlyImageView) dialog.getWindow()
				.getDecorView().findViewById(R.id.bord);
		final List<String> list = ApplicationUtil
				.loadAllApplication(getActivity());
		gridview.setAdapter(new AppAdapter(getActivity(), R.layout.add_item,
				list));
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String pkg = list.get(position);
				preManager.putString(tag, pkg);
				adapter.notifyDataSetChanged();
				showapps();
				dialog.cancel();
			}

		});
//		gridview.setOnKeyListener(listener);
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



	/**
	 * 本地
	 *
	 * @return
	 */
	public List<TagConfig> getUserData() {
		List<String> pkgs = DatabaseManager.getInstance(getActivity())
				.findApplicationByType(type);
		List<TagConfig> list = new ArrayList<TagConfig>();
		for (int i = 0; i < pkgs.size(); i++) {
			Intent intent = getActivity().getPackageManager()
					.getLaunchIntentForPackage(pkgs.get(i));
			if (intent != null) {
				list.add(new TagConfig(pkgs.get(i)));
			}
		}
		if(list.size()<15){
			list.add(new TagConfig(Constant.ADD_PACKAGE));
		}
		return list;
	}

	// 更新
	public void updateData(List<String> tags) {
		if (service == null) {
			service = ((MainActivity) getActivity()).getConfigService();
		}
		for (String tag : tags){
			TagConfig config = service.getTagConfig(tag);
			Intent intent = getActivity().getPackageManager()
					.getLaunchIntentForPackage(config.pkgName);
			if (intent == null){
				if(tag.equals("102")&&Constant.ADD_PACKAGE.equals(topTools[2].getApplication().getPackageName())){
					topTools[2].updateConfig(config);
					return;
				}
				if(tag.equals("103")&&Constant.ADD_PACKAGE.equals(topTools[3].getApplication().getPackageName())){
					topTools[3].updateConfig(config);
					return;
				}
				else if(!data.contains(config)&&data.size()<15&&
						!config.pkgName.equals(topTools[2].getApplication().getPackageName())&&
						!config.pkgName.equals(topTools[3].getApplication().getPackageName())
						){
					data.add(data.size() - 1,config);
				}
			}
		}
//			data.add(new TagConfig(Constant.ADD_PACKAGE));
//			data = getUserData();
//			for (String tag : tags) {
//				if (data.size() < 10) {
//					TagConfig config = service.getTagConfig(tag);
//					Intent intent = getActivity().getPackageManager()
//							.getLaunchIntentForPackage(config.pkgName);
//					if (intent == null) {
//						data.add(data.size() - 1, config);
//					}
//				} else if (data.size() == 10) {
//					if (data.get(data.size() - 1).pkgName
//							.equals(Constant.ADD_PACKAGE)) {
//						TagConfig config = service.getTagConfig(tag);
//						Intent intent = getActivity().getPackageManager()
//								.getLaunchIntentForPackage(config.pkgName);
//						if (intent == null) {
//							data.remove(data.size() - 1);
//							data.add(config);
//						}
//					}
//				}
//			}
		size = data.size();
//			if(size<15){
//				data.add(new TagConfig(Constant.ADD_PACKAGE));
//			}
//			scollto();
		adapter.notifyDataSetChanged();
	}


	//	public List<TagConfig> removeData(List<TagConfig> data){
//		if (service == null) {
//			service = ((MainActivity) getActivity()).getConfigService();
//		}
//		for (int i = 0; i < data.size(); i++) {
//			TagConfig t = data.get(i);
//			String pkg = t.pkgName;
//			Intent intent = getActivity().getPackageManager()
//					.getLaunchIntentForPackage(pkg);
//			if(intent==null){
//				data.
//			}
//		}
//	}
//	public void hiddenbar(){
//		top_an.setVisibility(View.GONE);
//		bottom_an.setVisibility(View.VISIBLE);
//	}
	OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View view, boolean hasFocus) {

			ATool tool = (ATool) view;
			if (hasFocus) {
//				hsv.setBackgroundResource(0);
//				hiddenbar();
//				gridView.setVisibility(View.INVISIBLE);
//				gridView.setFocusable(false);
				bord.setVisibility(View.VISIBLE);
//				Toast.makeText(getActivity(), "hasfocus", 1000).show();
				// 控制白框动画
				int[] location = new int[2];
				tool.getLocationInWindow(location);
				bord.flyTo(tool.getWidth() + 20, tool.getHeight() + 20,
						location[0] - 10, location[1] - 10,
						R.drawable.main_selector, 10);
//				if(tool.getTag().equals("100")||tool.getTag().equals("101")){
//					tvtip.setVisibility(View.VISIBLE);
//				}else{
//					//	tvtip.setVisibility(View.INVISIBLE);
//				}
			}


		}
	};
	OnClickListener onclicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = (String) v.getTag();
//			Toast.makeText(getActivity(), "tag=="+tag, 1000);
//			System.out.println("-----------tag"+tag);
			String pkg = preManager.getPackage(tag);
			ATool tool = (ATool) v;
			Application app = tool.getApplication();
//			if(tag.equals("100")||tag.equals("101")){
//				Intent intent =null;
//				if(pkg!=null){
//					intent = getActivity().getPackageManager().getLaunchIntentForPackage(
//							pkg);
//				}
//				if(intent==null){
//					intent = new Intent(getActivity(),QuickActivity.class);
//				}
//				startActivity(intent);
//
//			}
			if(tag.equals("100")){
				Intent intent =getActivity().getPackageManager().getLaunchIntentForPackage("com.qclive.tv");
				if(intent!=null){
					startActivity(intent);
				}
			}
			if(tag.equals("101")){
				Intent intent =getActivity().getPackageManager().getLaunchIntentForPackage("com.yidian.calendar");
				if(intent!=null){
					startActivity(intent);
				}
			}
//			if(tag.equals("104")){
//				((MainActivity) getActivity()).startapplist();
//			}
//			if(tag.equals("105")){
//				((MainActivity) getActivity()).fileManager();
//			}
//			if(tag.equals("106")){
//				//((MainActivity) getActivity()).bootChooser();
//				ApplicationUtil.startApp(getActivity(),"com.zxy.yidian");
//			}
//			if(tag.equals("107")){
//				((MainActivity) getActivity()).setting();
//			}
//			if(tag.equals("102")||tag.equals("103")){
//				if(!app.getPackageName().equals(Constant.ADD_PACKAGE)){
//					Intent intent = getActivity().getPackageManager()
//							.getLaunchIntentForPackage(app.getPackageName());
//					if (intent == null) {
//						preManager.putString("tagg", tag);
//						tool.startDownload(type);
//					}else{
//						ApplicationUtil.startApp(getActivity(), pkg);
//					}
//				}
//				else {
//					showAddDialogtwo(tag);
//				}
//			}
		}
	};




	OnLongClickListener longclick = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			ATool to = (ATool) v;
			String tag = (String) v.getTag();
			preManager.delete(tag);
			to.setApplicaton(ApplicationUtil.doAddApplication(getActivity()));
			return true;
		}
	};
	public void checkPackageRemove(String pkg) {
		for(TagConfig config : data) {
			if(pkg.equals(config.pkgName)) {
				data.remove(config);
				adapter.notifyDataSetChanged();
				DatabaseManager.getInstance(getActivity()).delete(pkg, type);
				return;
			}
		}
	}
	OnKeyListener listener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keycode, KeyEvent event) {
//			String tag = (String) v.getTag();
//			if("100".equals(tag)||"101".equals(tag)){
//				if(keycode==KeyEvent.KEYCODE_DPAD_DOWN){
//					bord.setVisibility(View.INVISIBLE);
//					startAnimation();
//				}
//			}

			String tag = (String) v.getTag();
			if("100".equals(tag)){
				if(keycode==KeyEvent.KEYCODE_DPAD_RIGHT){

				}
			}

			return false;
		}
	};
	public void startAnimation(){
		Animation bigIn = AnimationUtils.loadAnimation(getActivity(),
				R.anim.scale_big_in);
		gridView.startAnimation(bigIn);
		top_an.setVisibility(View.VISIBLE);
		bottom_an.setVisibility(View.GONE);
		bigIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				gridView.setVisibility(View.VISIBLE);
//				hsv.setBackgroundResource(R.drawable.backppp);
			}
		});
//		Animation downOut = AnimationUtils.loadAnimation(getActivity(),
//				R.anim.translate_down_out);
//			gridView.startAnimation(downOut);

	}
	public void changeGrid(){
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		// dishtype，welist为ArrayList
		size = data.size();
		params.width = 150 * 15+100;
		params.height = 300;
		gridView.setLayoutParams(params);
		//设置列数为得到的list长度
		gridView.setNumColumns(15);
		adapter = new CustomAdapter(getActivity(), data);
		gridView.setAdapter(adapter);
	}
	public void changecolor(){

		for (int i = 0; i <gridView.getChildCount(); i++) {
			ATool to = (ATool) gridView.getChildAt(i);
			//	to.hidlan();
			to.setBackgroundResource(R.drawable.zxydown);
		}
	}
}
