package com.jt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	/**
	 * 	思路：
	 * 1.创建工具API对象
	 * 2.定义远程url地址
	 * 3.定义请求类型
	 * 4.发起http请求
	 * 5.从响应对象中获取数据
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		//1.创建工具API对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 2.定义远程url地址
		String url="https://szlh.szmis.cn/login";
		//3.定义请求类型
		HttpGet get = new HttpGet(url);
		//4.发起http请求
		CloseableHttpResponse response = client.execute(get);
		if(response.getStatusLine().getStatusCode()==200) {
		//5.从响应对象中获取数据
		String result = EntityUtils.toString(response.getEntity());
		System.out.println(result);
		}else
			System.out.println("请求失败");
	}
}
