package com.tcsl.translate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * @author tcsl
 * 2022-01-24 14:34：26
 */
public class BaiduFanyiSupport{
	private final String baseurl = "https://fanyi-api.baidu.com/api/trans/vip/translate";
	private final String appId = "20220125001067121";  //百度翻译注册以后id 
	private final String securityKey = "5DGNY_qH3ObADeyW5KeP"; //百度翻译注册以后securityKey
	
	public Map<String, String> buildParams(String target, String from, String to) throws IOException {
		from = StringUtils.isBlank(from)?"auto":from;
		to = StringUtils.isBlank(to)?"zh":to;
		Map<String, String> params = new HashMap<String, String>();
        params.put("q", target);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appId);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = appId + target + salt + securityKey; // 加密前的原文
        params.put("sign", MD5.md5(src));
        return params;
	}
	
	public String  requestInfo(String target, String from, String to) throws IOException {
		Map<String, String> params = buildParams(target, from, to);
		
		Request request = new Request.Builder().addHeader("User-Agent", "Mozilla/5.0")
				.url(getUrlWithQueryString(baseurl, params))
				.get()
				.build();
		
		OkHttpClient okHttpClient = new OkHttpClient();
		Call call = okHttpClient.newCall(request);
		Response response = call.execute();
		String string = response.body().string();
		JSONObject result = JSONObject.parseObject(string);
		JSONArray jsonArray = JSONArray.parseArray(result.getString("trans_result"));
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		String string2 = jsonObject.getString("dst");
		return string2;
	}
	
	public static String getUrlWithQueryString(String url, Map<String, String> params) throws UnsupportedEncodingException {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int i = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) { // 过滤空的key
                continue;
            }

            if (i != 0) {
                builder.append('&');
            }

            builder.append(key);
            builder.append('=');
            builder.append(URLEncoder.encode(value, "utf-8"));

            i++;
        }

        return builder.toString();
    }
	
	public static void main(String[] args) {
		BaiduFanyiSupport cibaFanyiSupport = new BaiduFanyiSupport();
		try {
			cibaFanyiSupport.requestInfo("Ihr habt mir das Handycase schon einmal ausgetauscht. Dieses Case hat genau die gleichen Mängel, diesmal ist es ander linken oberen seite gerissen, das davor an der rechten oberen !	", null, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String translate(String target, String from, String to) {
		// TODO Auto-generated method stub
		try {
			return requestInfo(target, from, to);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
