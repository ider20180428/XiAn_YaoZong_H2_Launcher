package box.utils;

public class Video {
private  String video_name;
private  String video_url;
private  String video_id;
private  int video_time;
public String getVideo_name() {
	return video_name;
}
public void setVideo_name(String video_name) {
	this.video_name = video_name;
}
public String getVideo_url() {
	return video_url;
}
public void setVideo_url(String video_url) {
	this.video_url = video_url;
}
public String getVideo_id() {
	return video_id;
}
public void setVideo_id(String video_id) {
	this.video_id = video_id;
}
public int getVideo_time() {
	return video_time;
}
public void setVideo_time(int video_time) {
	this.video_time = video_time;
}
public Video() {
	super();
}
public Video(String video_name, String video_url, String video_id,
		int video_time) {
	super();
	this.video_name = video_name;
	this.video_url = video_url;
	this.video_id = video_id;
	this.video_time = video_time;
}



}
