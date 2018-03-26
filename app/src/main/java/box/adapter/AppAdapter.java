package box.adapter;

import java.util.List;

import box.utils.PreferenceManager;

import com.box.launcher.Application;
import com.box.launcher.ApplicationUtil;
import com.box.launcher.Constant;
import com.ider.launcher.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<String> list;
	int layout;
	private ApplicationInfo info;
	private PackageManager pm;
	int[] colors = { R.drawable.app_gridview_bg00,
			R.drawable.app_gridview_bg01, R.drawable.app_gridview_bg02,
			R.drawable.app_gridview_bg03, R.drawable.app_gridview_bg04,
			R.drawable.app_gridview_bg05, R.drawable.app_gridview_bg06,
			R.drawable.app_gridview_bg07, R.drawable.app_gridview_bg08 };

	String selectPackage;

	public void setSelectPackage(String selectPackage) {
		this.selectPackage = selectPackage;
	}

	public AppAdapter(Context context, int layout, List<String> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.layout = layout;
		this.context = context;
		pm = context.getPackageManager();
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
		ViewHolder holder;
		String pkg = list.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(layout, parent, false);
			holder.app_icon = (ImageView) convertView
					.findViewById(R.id.app_item_icon);
			holder.app_label = (TextView) convertView
					.findViewById(R.id.app_item_label);
			holder.app_color = (ImageView) convertView
					.findViewById(R.id.app_item_color);
			holder.app_selecter = (ImageView) convertView
					.findViewById(R.id.app_item_selector2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Application app = null;
		if(pkg.equals(Constant.TOOL_CLEAN_MASTER)){
			 app = ApplicationUtil.doCleanApplication(context);
		}else if(pkg.equals(Constant.TOOL_HOTKEY)){
			 app = ApplicationUtil.doHotKeyApplication(context);
		}else if(pkg.equals(Constant.TOOL_BGCH)){
			 app = ApplicationUtil.dochangebg(context);
		}else if(pkg.equals(Constant.TOOL_QUICK)){
			 app = ApplicationUtil.doQuick(context);
		}
		else{
		 app = ApplicationUtil.doApplication(context,
				pkg);
		}
		Drawable icon = app.getIcon();
		String label = app.getLabel();
		String pkgName = app.getPackageName();

		if (holder.app_selecter != null) {
			if (pkgName.equals(selectPackage)) {
				holder.app_selecter.setVisibility(View.VISIBLE);
			} else {
				holder.app_selecter.setVisibility(View.INVISIBLE);
			}
		}

		if (icon == null) {
			try {
				info = pm.getApplicationInfo(app.getPackageName(), 0);
				icon = info.loadIcon(pm);
				label = info.loadLabel(pm).toString();
			} catch (NameNotFoundException e) {
			}
		}
		if (holder.app_icon != null)
			holder.app_icon.setImageDrawable(icon);

		if (holder.app_label != null)
			holder.app_label.setText(label);

		if (holder.app_color != null) {
			holder.app_color.setImageResource(colors[position % colors.length]);
		}

		return convertView;
	}

}
