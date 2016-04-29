package com.foya;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class Test01 {
	String index = "http://localhost:8080/SpringFileUploadHttpClient/";
	String url = "http://127.0.0.1:8080/SpringFileUploadHttpClient/uploadFile";
	String fileLocation = "E:/tmp/requestCSP複製.xml";

	@Test
	public void httpGet() {

		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet httpPost = new HttpGet(index);

			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println("[LOG]" + response.getStatusLine().getStatusCode());
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void httpPost() {

		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("username", "John"));
			//params.add(new BasicNameValuePair("password", "pass"));
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			CloseableHttpResponse response = client.execute(httpPost);
			System.out.println("[LOG]" + response.getStatusLine().getStatusCode());
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void multipart() {

		try {

			CloseableHttpClient client = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			//FileBody bin = new FileBody(new File(fileLocation));

			MultipartEntityBuilder create = MultipartEntityBuilder.create();
			create.setCharset(Charset.forName("UTF-8"));
			create.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			final String fileName = new File(fileLocation).getName();
			create.addBinaryBody("file", new File(fileLocation), ContentType.APPLICATION_OCTET_STREAM, fileName);

			//create.addPart("file", bin);
			HttpEntity reqEntity = create.build();

			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = client.execute(httppost);
			try {
				System.out.println("----------------------------------------");
				System.out.println("[LOG]" + response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
