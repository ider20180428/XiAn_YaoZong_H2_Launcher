package box.cleansystem;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcher.R;

import java.text.DecimalFormat;
import java.util.List;

public class StartActivity extends Activity {
	private TextView cleaned_memory = null;
	private ImageView rotation = null;
	private String newAvailableMemory;
	private String cleaned;
	private float oldMemory;
	private float newMemory;
	private Animation rotationAnim = null;
	private DecimalFormat df;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clean_layout);
		df = new DecimalFormat("##0.0");

		cleaned_memory = (TextView) findViewById(R.id.cleanInfo);
		rotation = (ImageView) findViewById(R.id.rotation_image);

		startAnimation();

	}

	Runnable clean = new Runnable() {

		@Override
		public void run() {
			cleanMemory();
			newMemory = (float) getAvailableMemory() / 1024 / 1024;
			newAvailableMemory = df.format(newMemory);
			cleaned_memory.setVisibility(View.VISIBLE);
			String mMemoryFormat = getString(R.string.CleanedMemory);
			float memorys = newMemory - oldMemory;
			if (memorys < 0) {
				cleaned = "0";
			} else {
				cleaned = df.format(memorys);
			}
			cleaned_memory.setText(String.format(mMemoryFormat, cleaned,
					newAvailableMemory));
		}
	};

	public void cleanMemory() {
		ActivityManager manager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = manager.getRunningAppProcesses();
		for (int i = 0; i < list.size(); i++) {
			RunningAppProcessInfo appInfo = list.get(i);
			manager.killBackgroundProcesses(appInfo.processName);
		}
	}
	
	public long getAvailableMemory() {
		ActivityManager manager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		manager.getMemoryInfo(mi);
		return mi.availMem;
	}

	public void startAnimation() {
		rotationAnim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate);
		rotation.startAnimation(rotationAnim);

		rotationAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				oldMemory = (float)getAvailableMemory() / 1024 / 1024;
//				oldAvailableMemory = df.format((float)getAvailableMemory() / 1024 / 1024);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				new Handler().post(clean);
			}
		});
	}

}
