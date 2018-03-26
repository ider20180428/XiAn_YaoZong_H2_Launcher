package com.box.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AliResult {
	
	List<HashMap<String, String>> result;
	public AliResult() {
		result = new ArrayList<HashMap<String,String>>();
	}
	
	public void add(HashMap<String, String> map) {
		result.add(map);
	}
	
	public int size() {
		return result.size();
	}
	

	public List<HashMap<String, String>> get() {
		return this.result;
	}
	
}
