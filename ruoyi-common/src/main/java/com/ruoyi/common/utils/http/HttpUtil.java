package com.ruoyi.common.utils.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * http post提交
	 *
	 * @param url
	 * @param headers
	 * @param data
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> headers, String data) {
		String ret = HttpRequest.post(url).headerMap(headers, true).body(data).execute().body();
		return ret;
	}
	/**
	 * http post提交
	 *
	 * @param url
	 * @param data
	 * @return
	 */
	public static String httpPost(String url, String data) {
		log.info("data:" + data);
		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "application/json");
		String ret = HttpRequest.post(url).headerMap(headers, true).body(data).execute().body();
		return ret;
	}
	/**
	 *
	 * @param url
	 * @param headers
	 * @param data
	 * @param milliseconds
	 * @return
	 */
	public static String httpPost(String url, Map<String, String> headers, String data, int milliseconds) {
		log.info("data:" + data);
		String ret = HttpRequest.post(url).headerMap(headers, true).setConnectionTimeout(milliseconds).body(data)
				.execute().body();
		return ret;
	}

	public static String httpDel(String url, Map<String, String> headers, String data) {
		String ret = HttpRequest.delete(url).headerMap(headers, true).body(data).execute().body();
		return ret;
	}

	/**
	 * http get提交
	 *
	 * @param url
	 * @param headers
	 * @param formMap
	 * @return
	 */
	public static String httpGet(String url, Map<String, String> headers, Map<String, Object> formMap) {
		String ret = HttpRequest.get(url).headerMap(headers, true).form(formMap).execute().body();
		return ret;
	}

	/**
	 *
	 * @param url
	 * @param headers
	 * @param formMap
	 * @param milliSecondsTimeOut
	 *            超时时长：毫秒
	 * @return
	 */
	public static String httpGet(String url, Map<String, String> headers, Map<String, Object> formMap,
			int milliSecondsTimeOut) {
		HttpResponse response = HttpRequest.get(url).headerMap(headers, true).form(formMap).timeout(milliSecondsTimeOut)
				.execute();
		if (response.getStatus() != 200) {
			log.error("接口调用异常：" + url + "," + JSONObject.toJSONString(response));
			log.error("接口调用异常：" + response.body());
			return "";
		}
		String ret = response.body();
		return ret;
	}

	public static void main(String[] args) {
		String url = "http://www.rtc2sip.cn:81/public/v1/ticker?limit=35";
		if (args != null && args.length > 0) {
			url = args[0];
		}
		log.info("url:" + url);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Accept", "application/json");
		Map<String, Object> paraMap = new HashMap<>();
		String ret = httpGet(url, headerMap, paraMap, 5000);
		System.out.println("ret:" + ret);
	}
}
