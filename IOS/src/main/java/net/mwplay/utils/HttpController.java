package net.mwplay.utils;

import com.android.okhttp.OkHttpClient;
import com.android.okhttp.Request;
import com.android.okhttp.Response.Receiver;

public class HttpController {

	private static HttpController instance = null;

	public static HttpController getInstance() {
		if (instance == null) {
			instance = new HttpController();
		}

		return instance;
	}

	public HttpController() {

	}

	public void onSearch(String url, Receiver receiver) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder(url).build();
		client.enqueue(request, receiver);
	}
}
