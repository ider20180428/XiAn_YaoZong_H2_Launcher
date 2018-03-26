package box.adapter;

import java.util.List;

import com.box.background.TagConfig;
import com.box.launcher.Application;
import com.ider.launcher.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfGridAdapter extends BaseAdapter {
	Context context;
	List<TagConfig> list;
	public SelfGridAdapter(Context context, List<TagConfig> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.downloaded_item, parent, false);
			holder.app_icon = (ImageView) convertView.findViewById(R.id.app_item_icon);
			holder.app_label = (TextView) convertView.findViewById(R.id.app_item_label);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.app_label.setText(list.get(position).label);
		Picasso.with(context).load(Uri.parse(list.get(position).iconUrl)).placeholder(R.drawable.replce_app_default_logo).into(holder.app_icon);
		
		return convertView;
	}

}
