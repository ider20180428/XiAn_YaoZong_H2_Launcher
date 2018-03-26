package com.box.background;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class OKhttpManager {

	private static final OkHttpClient client = new OkHttpClient();
	
	static {
		client.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
	}

	public static String exuteFromServer(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		Response response = call.execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	public static Response exute(Request request) throws IOException {
		return client.newCall(request).execute();
	}


	public void download(final String url, final String destFileDir) {
		final Request request = new Request.Builder().url(url).build();
		final Call call = client.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Request request, final IOException e) {
				
			}

			@Override
			public void onResponse(Response response) {
				InputStream is = null;
				byte[] buf = new byte[2048];
				int len = 0;
				FileOutputStream fos = null;
				try {
					is = response.body().byteStream();
					File file = new File(destFileDir, getFileName(url));
					fos = new FileOutputStream(file);
					while ((len = is.read(buf)) != -1) {
						fos.write(buf, 0, len);
					}
					fos.flush();
				} catch (IOException e) {
					
				} finally {
					try {
						if (is != null)
							is.close();
					} catch (IOException e) {
					}
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
					}
				}

			}
		});
	}

	private String getFileName(String path) {
		int separatorIndex = path.lastIndexOf("/");
		return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1,
				path.length());
	}
	

}
