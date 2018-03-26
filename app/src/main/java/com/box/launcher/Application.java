package com.box.launcher;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class Application {
    public String label;
    private Drawable icon;
    private Intent in;
    private String packageName;
    private String className;
    private String type;
    String tag;
    private String tage;
    private boolean isChecked = false;
    
    
    public String getTage() {
		return tage;
	}

	public void setTage(String tage) {
		this.tage = tage;
	}

	public Application() {
    }

	
    public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}


	public Application(String packageName) {
	this.packageName = packageName;
    }

    public Application(String label, String packageName) {
	this.label = label;
	this.packageName = packageName;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public Drawable getIcon() {
	return icon;
    }

    public void setIcon(Drawable icon) {
	this.icon = icon;
    }
    
    public Intent getIntent() {
	return in;
    }

    public void setIntent(Intent in) {
	this.in = in;
    }

    public String getPackageName() {
	return packageName;
    }

    public void setPackageName(String packageName) {
	this.packageName = packageName;
    }

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    @Override
    public boolean equals(Object o) {
    	
    	return this.getPackageName().equals(((Application)o).getPackageName());
    }
    
    public void setChecked(boolean isChecked){
	this.isChecked = isChecked;
    }
    
    public boolean isChecked(){
	return isChecked;
    }
    
    public boolean isAdd() {
    	return this.packageName.equals(Constant.ADD_PACKAGE);
    }
}
