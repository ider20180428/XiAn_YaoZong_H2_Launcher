package box.utils;

public class TagUtil {
	
	// ÿһҳtag����ʼ
	public static int RECOMMAND_TAG_START = 100;
	public static int VIDEO_TAG_START = 200;
	public static int GAME_TAG_START = 300;
	public static int EDU_TAG_START = 400;
	public static int SHOPPING_TAG_START = 500;
	

	

	public static boolean b2Recommend(String tagStr) {
		int tag = Integer.parseInt(tagStr);
		if(tag > 100 && tag < 200) {
			return true;
		}
		return false;
	}
	

	public static boolean b2Video(String tagStr) {
		int tag = Integer.parseInt(tagStr);
		if(tag > 200 && tag < 300) {
			return true;
		}
		return false;
	}
	

	public static boolean b2Shopping(String tagStr) {
		int tag = Integer.parseInt(tagStr);
		if(tag > 500 && tag < 600) {
			return true;
		}
		return false;
	}
	

	public static boolean b2Game(String tagStr) {
		int tag = Integer.parseInt(tagStr);
		if(tag > 400 && tag < 500) {
			return true;
		}
		return false;
	}
	

	public static boolean b2Educate(String tagStr) {
		int tag = Integer.parseInt(tagStr);
		if(tag > 300 && tag < 400) {
			return true;
		}
		return false;
	}
	

	public static int getPageIndex(String tag) {
		int tagInteger = Integer.parseInt(tag);
		if(tagInteger<1000){
		return tagInteger / 100 - 1;
		}else{
			return tagInteger / 1000 - 1;
		}
	}
}
