package com.tscl.utils;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author tscl
 * 2022-01-24 17:19:33
 * 任何语言自动翻译成中文,使用google translate api
 * 其他语言可以参考 https://github.com/matheuss/google-translate-api/blob/master/languages.js
 */
public class GoogleFanyiSupport{
	private final String baseurl = "https://translate.googleapis.com/translate_a/single?client=gtx";

	public String requestInfo(String target, String from, String to) throws IOException{

		if (StringUtils.isBlank(target)) {
			target = "null";
		} else {
			target = URLEncoder.encode(target, "utf8");
		}
		from = StringUtils.isBlank(from)?"auto":from;
		to = StringUtils.isBlank(to)?"zh-CN":to;

		OkHttpClient okHttpClient = new OkHttpClient();


		Request request = new Request.Builder().addHeader("User-Agent", "Mozilla/5.0")
							.url(baseurl + "&sl="+ from +"&tl="+to+"&dt=t&q="+target)
							.get()
							.build();
		Call call = okHttpClient.newCall(request);
		Response response = call.execute();
		String string = response.body().string();
		String result = string.substring(4, string.indexOf(",")-1);
		return result;
	}

	public static void main(String[] args) {
		GoogleFanyiSupport googleFanyiSupport = new GoogleFanyiSupport();
		try {
			googleFanyiSupport.requestInfo("hello", null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
