package com.box.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ider.launcher.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import box.adapter.SpinnerAdapter;
import box.utils.PreferenceManager;


public class CityPicker extends Activity {
	JSONArray array;
	List<String> provinces;
	List<String> cities;
	String city;
	Spinner spinner1;
	Spinner spinner2;
	TextView bigCity;
	GridView hotGrid;
	CheckBox autoCheck;
	PreferenceManager preManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_picker);
		preManager = PreferenceManager.getInstance(this);
		findViews();
		showViews();
		setListeners();


	}

	public void findViews() {
		bigCity = (TextView) findViewById(R.id.city_big);
		spinner1 = (Spinner) findViewById(R.id.province_spinner);
		spinner2 = (Spinner) findViewById(R.id.city_spinner);
		hotGrid = (GridView) findViewById(R.id.hot_grid);
		autoCheck = (CheckBox) findViewById(R.id.auto_check);
	}


	public void showViews() {

		// 获取传入的城市，显示在BigCity上
		Intent in = getIntent();
		city = in.getStringExtra("currentCity");

		if (city != null) {
			bigCity.setText(city);
		} else {
			bigCity.setText(R.string.auto);
		}

		provinces = getProvinces();
		spinner1.setAdapter(new SpinnerAdapter(getApplicationContext(),
				provinces));

		hotGrid.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				R.layout.hot_city_item, hotCities()));

		// 设置checkBox状态
		if(preManager.getManuCity() == null) {
			autoCheck.setChecked(true);
		} else {
			autoCheck.setChecked(false);
		}

	}

	public void setListeners() {
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				cities = getCities(position);
				spinner2.setAdapter(new SpinnerAdapter(getApplicationContext(),
						cities));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if (!cities.get(position).equals("城市")) {
					// 手动城市
					setManuCity(cities.get(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		hotGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				setManuCity(hotCities().get(position));
			}
		});

		autoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					setAutoCity();
				} else {
					setManuCity(null);
				}
			}
		});
	}

	/**
	 * 写入手动城市
	 * @param city
	 */
	public void setManuCity(String city) {
		this.city = city;
		if (city != null) {
			preManager.setManuCity(city);
			bigCity.setText(city);
		}
	}

	/**
	 * 设置自动定位（删除保存的城市）
	 */
	public void setAutoCity() {
		this.city = null;
		preManager.removeCity();
		bigCity.setText(R.string.auto);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public String readData() {
		String data = null;
		// ========== 通过输入流获取JSON格式的省市数据
		try {
			InputStream is = getAssets().open("city.json");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count;
			while ((count = is.read()) != -1) {
				baos.write(count);
			}
			is.close();
			baos.close();
			byte[] buffer = baos.toByteArray();
			data = new String(buffer, "gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public List<String> getProvinces() {
		List<String> provinces = new ArrayList<String>();
		try {
			array = new JSONArray(readData());
			for (int i = 0; i < array.length(); i++) {
				JSONObject jo = array.getJSONObject(i);
				provinces.add(jo.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return provinces;
	}

	public List<String> getCities(int index) {
		List<String> cities = new ArrayList<String>();
		try {
			JSONObject jo = array.getJSONObject(index);
			JSONArray ja = jo.getJSONArray("sub");
			for (int i = 1; i < ja.length() - 1; i++) {
				JSONObject object = ja.getJSONObject(i);
				cities.add(object.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < cities.size(); i++) {
			System.out.println(cities.get(i));
		}
		return cities;
	}

	public List<String> hotCities() {
		List<String> list = new ArrayList<String>();
		list.add("北京");
		list.add("上海");
		list.add("广州");
		list.add("深圳");
		list.add("武汉");
		list.add("南京");
		list.add("西安");
		list.add("成都");
		list.add("郑州");
		list.add("杭州");
		list.add("东莞");
		list.add("重庆");
		list.add("长沙");
		list.add("天津");
		list.add("苏州");
		list.add("沈阳");
		list.add("福州");
		list.add("无锡");
		list.add("哈尔滨");
		list.add("厦门");
		list.add("石家庄");
		return list;
	}
}
