package com.box.background;

public class TagConfig {
	
	public String status;
	public String pkgName;
	public String label;
	public String verName;
	public int verCode;
	public String summary;
	public String description;
	public String iconUrl;
	public String apkUrl;
	public boolean forced;
	public String miniImage;
	public String image;
	
	public void setStatus(String status) {
		this.status = status;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public void setVerCode(int verCode) {
		this.verCode = verCode;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public void setForced(boolean forced) {
		this.forced = forced;
	}
	public void setMiniImage(String miniImage) {
		this.miniImage = miniImage;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	
	public TagConfig() {}
	
	public TagConfig(String packageName) {
		this.pkgName = packageName;
		
	}
	
	
	@Override
	public boolean equals(Object o) {
		return ((TagConfig)o).pkgName.equals(pkgName);
	}
	
}
