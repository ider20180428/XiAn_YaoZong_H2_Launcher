package com.box.background;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;

public class Product {

	public static int getVendorId(Context context) {

		Class<?> clazz;
		try {
			clazz = Class.forName("com.yunos.settings.SettingApiManager");
			Method method = clazz.getMethod("getInfoCollectionUtils",
					Context.class);
			Object infoCollectionObj = method.invoke(null, context);
			Method getVendorMethod = infoCollectionObj.getClass().getMethod(
					"getVendorID");
			Object vendorID = getVendorMethod.invoke(infoCollectionObj);
			if (vendorID instanceof Integer)
				return (Integer)vendorID;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
