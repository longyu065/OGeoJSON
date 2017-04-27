package com.oyou.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HtmlUtil {

	
	/**
	 * 根据URL获得所有的html信息
	 * 
	 * @param url
	 * @param isPost 
	 * @return
	 */
	public static String getHtmlByUrl(String url,Boolean isPost) {
		return getHtmlByUrl(url, null,isPost);
	}
	/**
	 * 根据URL获得所有的html信息
	 * 
	 * @param url
	 * @return
	 */
	public static String getHtmlByUrl(String url) {
		return getHtmlByUrl(url, null,false);
	}
	/**
	 * 根据URL获得所有的html信息
	 * 
	 * @param url
	 * @param headMap
	 * @return
	 */
	public static String getHtmlByUrl(String url,Map<String,String> headMap) {
		return getHtmlByUrl(url, headMap,false);
	}
	/**
	 * @param url
	 * @param headMap
	 * @param isPost  the default is false
	 * @return html
	 */
	public static String getHtmlByUrl(String url,Map<String,String> headMap,Boolean isPost) {
		String html = "";
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

		HttpRequestBase httpReauest =isPost?new HttpPost(url): new HttpGet(url);// 以get方式请求该URL
		if(headMap!=null){
			Iterator<Entry<String,String>> it=headMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String>entry =it.next();
				String key=entry.getKey();
				String value=entry.getValue();
				httpReauest.setHeader(key, value);
			}
		}

		try {
			HttpResponse responce = closeableHttpClient.execute(httpReauest);// 得到responce对象
			int resStatu = responce.getStatusLine().getStatusCode();// 返回码
			if (resStatu == HttpStatus.SC_OK) {// 200正常 其他就不对
				// 获得相应实体
				HttpEntity entity = responce.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity, "UTF-8");// 获得html源代码
				}
			} else {
				System.out.println("访问【" + resStatu + "】出现异常!");
			}
		} catch (Exception e) {
			System.out.println("访问【" + url + "】出现异常!");
			e.printStackTrace();
		} finally {
			closeableHttpClient.getConnectionManager().shutdown();
		}
		return html;
	}


}
