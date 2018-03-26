package box.adapter;

import java.util.List;

import com.box.background.TagConfig;
import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.box.launcher.Constant;
import com.ider.launcher.R;
import com.box.views.ATool;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

public class CustomAdapter extends BaseAdapter {
	String TAG = "YD12";
	List<TagConfig> list;
	Context context;


	public CustomAdapter(Context context, List<TagConfig> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ATool tool = new ATool(context);
		LayoutParams lp = new LayoutParams(140, 130);
		tool.setLayoutParams(lp);
		TagConfig config = list.get(position);
		if (config.status == null) {
			Application app;
			if (config.pkgName.equals(Constant.TOOL_APK_INSTALLER)) {
				app = ApplicationUtil.doAppInstaller(context);
			} else if (config.pkgName.equals(Constant.TOOL_APPLIST)) {
				app = ApplicationUtil.doAppListApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_BOOT_START)) {
				app = ApplicationUtil.doBootstartApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_CLEAN_MASTER)) {
				app = ApplicationUtil.doCleanApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_FILE_MANAGER)) {
				app = ApplicationUtil.doFileManagerApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_HOTKEY)) {
				app = ApplicationUtil.doHotKeyApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_NETSETTINGS)) {
				app = ApplicationUtil.doNetworkApplication(context);
			} else if (config.pkgName.equals(Constant.TOOL_SETTING)) {
				app = ApplicationUtil.doSettings(context);
			} else if (config.pkgName.equals(Constant.TOOL_CALCULATOR)) {
				app = ApplicationUtil.doCalculator(context);
			} else {
				app = ApplicationUtil.doApplication(context, config.pkgName);
			}
			if (app != null) {
				tool.setApplicaton(app);
//				tool.backlan();
//				tool.hidlan();
			}
		} else {
			tool.updateConfig(config);
		}
//		tool.backlan();
//		tool.hidlan();
		tool.setBackgroundResource(R.drawable.zxydown);
		if(position==0){
//			tool.showlan();
			tool.setBackgroundResource(R.drawable.shelectdown2);
		}
		return tool;
	}
	
}
