
package cn.vipapps;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import cn.share.Common;
import cn.vipapps.android.ACTIVITY;
import cn.vipapps.android.AppException;
import cn.vipapps.android.MultipartEntity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AJAX {

	/**
	 * 设置是否为demo方式
	 */
	public static Boolean isDemo = false;

	public enum Mode {
		GET, POST, PUT, DELETE, BITMAP
	}

	@SuppressWarnings({ "rawtypes" })
	private static HttpRequestBase getResponseWithUrl(String url, Mode mode, Map<String, Object> parameters)
			throws Exception {

		// OneKit.init();
		List<NameValuePair> pairs;
		String query = "";
		if (parameters != null) {
			pairs = new ArrayList<NameValuePair>();
			for (String key : parameters.keySet()) {
				Object value = parameters.get(key);
				if (value == null) {
					continue;
				}
				String json;
				if (value.getClass().isArray()) {
					json = new JSONArray((Collection) value).toString();
				} else if (value.getClass() == Map.class) {
					json = new JSONObject((Map) value).toString();
				} else {
					json = value.toString();
				}
				String string = json;
				if (mode == Mode.GET || mode == Mode.DELETE) {
					query += "&" + key + "=" + URLEncoder.encode(string);
				} else {
					pairs.add(new BasicNameValuePair(key, string));
				}
			}
		} else {
			pairs = null;
		}
		switch (mode) {
			case GET:
				if (!STRING.empty(query)) {
					URL _url = new URL(url);
					if (STRING.empty(_url.getQuery())) {
						url = url + "?" + query.substring(1);
					} else {
						url = url + "&" + query.substring(1);
					}
				}
				HttpGet HttpGet = new HttpGet(url);
				return HttpGet;
			case POST:
				HttpPost HttpPost = new HttpPost(url);
				if (pairs != null) {
					HttpPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
				}
				return HttpPost;
			case PUT:
				HttpPut HttpPut = new HttpPut(url);
				if (pairs != null) {
					HttpPut.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
				}
				return HttpPut;
			case DELETE:
				if (!STRING.empty(query)) {
					url = url + "?" + query.substring(1);
				}
				HttpDelete HttpDelete = new HttpDelete(url);
				return HttpDelete;
			default:
				return null;
		}
//		// OneKit.init();
//		List<NameValuePair> pairs;
//		String query = "";
//		JSONObject QUERY = new JSONObject();
//		if (parameters != null) {
//			pairs = new ArrayList<NameValuePair>();
//			for (String key : parameters.keySet()) {
//				Object value = parameters.get(key);
//				if (value == null) {
//					continue;
//				}
//				String json;
//				if (value.getClass().isArray()) {
//					json = new JSONArray((Collection) value).toString();
//				} else if (value.getClass() == Map.class) {
//					json = new JSONObject((Map) value).toString();
//				} else {
//					json = value.toString();
//				}
//				String string = json;
//				//if (mode == Mode.GET || mode == Mode.DELETE) {
//					query += "&" + key + "=" + URLEncoder.encode(string);
//				//} else {
//					pairs.add(new BasicNameValuePair(key, string));
//				//}
//				QUERY.put(key, string);
//			}
//		} else {
//			pairs = null;
//		}
//		HttpRequestBase request;
//		switch (mode) {
//		case GET:
//			if (!STRING.empty(query)) {
//				URL _url = new URL(url);
//				if (STRING.empty(_url.getQuery())) {
//					url = url + "?" + query.substring(1);
//				} else {
//					url = url + "&" + query.substring(1);
//				}
//			}
//			HttpGet HttpGet = new HttpGet(url);
//			request = HttpGet;
//			break;
//		case POST:
//			HttpPost HttpPost = new HttpPost(url);
//			if (pairs != null) {
//				HttpPost.setEntity(new StringEntity(query, HTTP.UTF_8));
//			}
//			request = HttpPost;
//			break;
//		case PUT:
//			HttpPut HttpPut = new HttpPut(url);
//			if (pairs != null) {
//				HttpPut.setEntity(new StringEntity(query, HTTP.UTF_8));
//			}
//			request = HttpPut;
//			break;
//		case DELETE:
//			if (!STRING.empty(query)) {
//				URL _url = new URL(url);
//				if (STRING.empty(_url.getQuery())) {
//					url = url + "?" + query.substring(1);
//				} else {
//					url = url + "&" + query.substring(1);
//				}
//			}
//			HttpDelete HttpDelete = new HttpDelete(url);
//			request = HttpDelete;
//			break;
//		default:
//			return null;
//		}
////		request.setHeader("Content-Type", "text/json");
//		return request;
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unused" }) private static
	 * HttpRequestBase RequestFile(String url, Map<String, Object> parameters,
	 * Map<String, Bitmap> imgs) throws UnsupportedEncodingException { HttpPost
	 * request = new HttpPost(url);
	 * request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
	 * 30000); request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
	 * 30000); MultipartEntity multipartEntity = new MultipartEntity(); if (imgs
	 * != null) { for (String name : imgs.keySet()) { Bitmap img =
	 * imgs.get(name); ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 * img.compress(Bitmap.CompressFormat.JPEG, 90, baos); InputStream is = new
	 * ByteArrayInputStream(baos.toByteArray()); multipartEntity.addPart(name,
	 * name, is, false); } } if (parameters != null) { int i = 0; for (String
	 * key : parameters.keySet()) { Object value = parameters.get(key); if
	 * (value == null) { continue; } String json; if
	 * (value.getClass().isArray()) { json = new JSONArray((Collection)
	 * value).toString(); } else if (value.getClass() == Map.class) { json = new
	 * JSONObject((Map) value).toString(); } else { json = value.toString(); }
	 * String string = json; multipartEntity.addPart(key, string, i ==
	 * (parameters.size() - 1)); i++; } } if (getHeaders() != null) {
	 * setHeaders(request, getHeaders()); } else { setHeaders(request, null); }
	 * request.setEntity(multipartEntity); request.setHeader("Content-Type",
	 * multipartEntity.getContentType().getValue()); return request; }
	 */
	private static void setHeaders(HttpRequestBase request, Map<String, Object> headers) {
		if (headers == null) {
			return;
		}
		for (String key : headers.keySet()) {
			if (headers.get(key) != null) {
				request.setHeader(key, headers.get(key).toString());
			}
		}
	}

	/**
	 * 返回的Header
	 */
	public static Header[] HEADERS;
	private static Map<String, Object> aHeaders;

	/**
	 * setHeaers 设置html头，如果要设置头请在网络请求之前设置。
	 * 
	 * @param headers
	 *            头参数
	 */
	public static void setHeaders(Map<String, Object> headers) {
		aHeaders = headers;
		// String COOKIE = (String) CONFIG.get("COOKIE");
		// if (!STRING_.exists(COOKIE)) {
		// headers.put("Cookie", COOKIE);
		// }
	}

	public static Map<String, Object> getHeaders() {
		return aHeaders;
	}

	@SuppressWarnings({ "rawtypes" })
	private static String str(String url, Map<String, Object> parameters, Mode mode) {
		String Url = null;
		String query = "";
		List<NameValuePair> pairs;
		if (parameters != null) {
			pairs = new ArrayList<NameValuePair>();
			for (String key : parameters.keySet()) {
				Object value = parameters.get(key);
				if (value == null) {
					continue;
				}
				String json;
				if (value.getClass().isArray()) {
					json = new JSONArray((Collection) value).toString();
				} else if (value.getClass() == Map.class) {
					json = new JSONObject((Map) value).toString();
				} else {
					json = value.toString();
				}
				String string = json;
				if (mode == Mode.GET || mode == Mode.DELETE) {
					query += "&" + key + "=" + URLEncoder.encode(string);
				} else {
					pairs.add(new BasicNameValuePair(key, string));
				}
			}
		} else {
			pairs = null;
		}
		if (!STRING.empty(query)) {
			URL _url = null;
			try {
				_url = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			if (STRING.empty(_url.getQuery())) {
				url = url + "?" + query.substring(1);
			} else {
				url = url + "&" + query.substring(1);
			}
		} else {
			if (url.startsWith("http://")) {
				url = url.substring(7, url.length());
			}
			Url = url;
			String[] a = { "*", "\\", "\"", ":", "?", "|", "<", ">" };
			for (int i = 0; i < a.length; i++) {
				Url = Url.replace(a[i], "/");
			}
		}

		return Url;
	}

	/**
	 * getData 返回二进制
	 * 
	 * @param url
	 *            服务器地址
	 * @param parameters
	 *            传输数据
	 * @param mode
	 *            获取的类型
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void getData(final String url, Map<String, Object> parameters, Mode mode, final CALLBACK<byte[]> callback) {

		if (isDemo) {
			if (FSO.exists("", url)) {
				callback.run(true, null);
			} else {
				callback.run(false, ImageDemo(str(url, null, null)));
			}
			return;
		}

		try {
			HttpRequestBase request = getResponseWithUrl(url, mode, parameters);
			if (getHeaders() != null) {
				setHeaders(request, getHeaders());
			} else {
				setHeaders(request, null);
			}
			new AsyncTask<HttpRequestBase, Integer, byte[]>() {
				@Override
				protected byte[] doInBackground(HttpRequestBase... params) {
					ByteArrayOutputStream bos;
					try {
						HttpResponse response = new DefaultHttpClient().execute(params[0]);
						HEADERS = response.getAllHeaders();
						Integer statusCode = response.getStatusLine().getStatusCode();

						if (statusCode != 200) {
							switch (statusCode) {
							case -1:
							case 0:
								DIALOG.warning(true, new AppException("网络无法连接，请检查网络设置。"));
								break;
							default:
								DIALOG.warning(true, new AppException(url + String.format("\n网络错误:%d,请稍后重试。", statusCode)));
								break;
							}
							return null;
						}
						HttpEntity entity = response.getEntity();
						bos = new ByteArrayOutputStream();
						InputStream in = entity.getContent();
						byte[] datas = new byte[1024 * 2];
						int len = 0;
						while ((len = in.read(datas)) != -1) {
							bos.write(datas, 0, len);
						}
						in.close();
						bos.close();
					} catch (Exception e) {
						DIALOG.warning(e);
						return null;
					}
					return bos.toByteArray();
				}

				protected void onPreExecute() {
				}

				@Override
				protected void onPostExecute(byte[] bytes) {
					callback.run(bytes == null, bytes);
				}

			}.execute(request);
		} catch (Exception e) {
			DIALOG.warning(true, e);
			callback.run(true, null);
		}
	}

	static String message;

	/**
	 * getString 返回字符串 需要权限：android.permission.INTERNET
	 * 
	 * @param url
	 *            服务器地址
	 * @param parameters
	 *            传输数据
	 * @param mode
	 *            获取的类型
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void getString(final String url, Map<String, Object> parameters, Mode mode,
								 final CALLBACK<String> callback) {
		if (isDemo) {
			callback.run(false, Stringdemo(str(url, parameters, mode)));
			return;
		}
		try {
			final HttpRequestBase request = getResponseWithUrl(url, mode, parameters);
			if (getHeaders() != null) {
				setHeaders(request, getHeaders());
			} else {
				setHeaders(request, null);
			}
			message = null;
			new AsyncTask<HttpRequestBase, Integer, String>() {

				@SuppressLint("DefaultLocale")
				@Override
				protected String doInBackground(HttpRequestBase... params) {
					try {
						HttpResponse response = new DefaultHttpClient().execute(request);
						HEADERS = response.getAllHeaders();
						Integer statusCode = response.getStatusLine().getStatusCode();
						if (statusCode != 200) {
							if (statusCode == 401) {
								MESSAGE.send(Common.MSG_LOGIN, null);
							}
							switch (statusCode) {
							case -1:
							case 0:
								message = "网络无法连接，请检查网络设置。";
								break;
							default:
								message = url + String.format("\n网络错误:%d,请稍后重试。", statusCode);
								break;
							}
							HttpEntity entity = response.getEntity();
							String error = EntityUtils.toString(entity, "utf-8");
							if (!STRING.empty(error)) {
								JSONObject json = JSON.parse(error);
								if (json != null) {
									message = json.optString("error_msg");
									JSONObject error_detail= json.optJSONObject("error_detail");
									if(error_detail!=null){
										 Iterator<String> iterator = error_detail.keys();
								            while(iterator.hasNext()){
								                String key  =  iterator.next();
								                message += "\n[" + key+"]"+error_detail.optString(key);
								                
								            }
									}
								}
							}
							return null;
						}
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity, "utf-8");
						return str;
					} catch (Exception e) {
						e.printStackTrace();
						message = e.getMessage();
						return null;
					}
				}

				protected void onPreExecute() {
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				protected void onPostExecute(String string) {
					if (message != null) {
						DIALOG.alert(message, new CALLBACK() {

							@Override
							public void run(boolean isError, Object result) {
								callback.run(true, null);
							}

						});
					} else {
						callback.run(string == null, string);
					}
				}

			}.execute(request);
		} catch (Exception e) {
			DIALOG.warning(true, e);
			callback.run(true, null);
		}
	}

	/**
	 * getVoid 返回null
	 * 
	 * @param url
	 *            服务器地址
	 * @param parameters
	 *            传输数据
	 * @param mode
	 *            获取的类型
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void getVoid(final String url, Map<String, Object> parameters, Mode mode,
			final CALLBACK<Object> callback) {

		if (isDemo) {
			callback.run(false, Stringdemo(str(url, parameters, mode)));
			return;
		}
		try {
			final HttpRequestBase request = getResponseWithUrl(url, mode, parameters);
			if (getHeaders() != null) {
				setHeaders(request, getHeaders());
			} else {
				setHeaders(request, null);
			}
			message = null;
			new AsyncTask<HttpRequestBase, Integer, String>() {

				@SuppressLint("DefaultLocale")
				@Override
				protected String doInBackground(HttpRequestBase... params) {
					try {
						HttpResponse response = new DefaultHttpClient().execute(request);
						HEADERS = response.getAllHeaders();
						Integer statusCode = response.getStatusLine().getStatusCode();

						if (statusCode != 200) {
							if (statusCode == 401) {
								MESSAGE.send(Common.MSG_LOGIN, null);
							}
							switch (statusCode) {
							case -1:
							case 0:
								message = "网络无法连接，请检查网络设置。";
								break;
							default:
								message = url + String.format("\n网络错误:%d,请稍后重试。", statusCode);
								break;
							}
							HttpEntity entity = response.getEntity();
							String error = EntityUtils.toString(entity, "utf-8");
							if (!STRING.empty(error)) {
								JSONObject json = JSON.parse(error);
								if (json != null) {
									message = json.optString("error_msg");
									JSONObject error_detail= json.optJSONObject("error_detail");
									if(error_detail!=null){
										 Iterator<String> iterator = error_detail.keys();
								            while(iterator.hasNext()){
								                String key  =  iterator.next();
								                message += "\n[" + key+"]"+error_detail.optString(key);
								                
								            }
									}
								}
							}
							return null;
						}
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity, "utf-8");
						return str;
					} catch (Exception e) {
						e.printStackTrace();
//						DIALOG.alert(e.getMessage());
						return null;
					}
				}

				protected void onPreExecute() {
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				protected void onPostExecute(String string) {
					if (message != null) {
//						DIALOG.alert(message, new CALLBACK() {
//
//							@Override
//							public void run(boolean isError, Object result) {
								callback.run(true, null);
//							}
//
//						});
					} else {
						callback.run(false, null);
					}
				}

			}.execute(request);
		} catch (Exception e) {
			DIALOG.warning(true, e);
			callback.run(true, null);
		}
	}

	/**
	 * getJSON 返回JSON
	 * 
	 * @param url
	 *            服务器地址
	 * @param parameters
	 *            传输数据
	 * @param mode
	 *            获取的类型
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void getJSON(String url, Map<String, Object> parameters, Mode mode,
			final CALLBACK<JSONObject> callback) {

		getString(url, parameters, mode, new CALLBACK<String>() {

			@Override
			public void run(boolean isError, String str) {

				try {
					if (isError) {
						callback.run(true, null);
						return;
					}
					callback.run(false, JSON.parse(str));
				} catch (Exception e) {
					e.printStackTrace();
					DIALOG.warning(true, e);
					callback.run(true, null);
				}

			}

		});
	}

	public static void getJSONs(String url, Map<String, Object> parameters, Mode mode,
			final CALLBACK<JSONArray> callback) {

		getString(url, parameters, mode, new CALLBACK<String>() {

			@Override
			public void run(boolean isError, String str) {

				try {
					if (isError) {
						callback.run(true, null);
						return;
					}
					callback.run(false, JSON.parses(str));
				} catch (Exception e) {
					e.printStackTrace();
					DIALOG.warning(true, e);
					callback.run(true, null);
				}

			}

		});
	}

	private static void getBytes(final String url, Map<String, Object> parameters, Mode mode,
			final CALLBACK<byte[]> callback) {

		if (isDemo) {
			if (FSO.exists("", url)) {
				callback.run(true, null);
			} else {
				callback.run(false, ImageDemo(str(url, null, null)));
			}
			return;
		}

		try {
			HttpRequestBase request = getResponseWithUrl(url, mode, null);

			new AsyncTask<HttpRequestBase, Integer, byte[]>() {

				@SuppressLint("DefaultLocale")
				@Override
				protected byte[] doInBackground(HttpRequestBase... params) {
					ByteArrayOutputStream bos;
					try {
						HttpResponse response = new DefaultHttpClient().execute(params[0]);
						HEADERS = response.getAllHeaders();
						Integer statusCode = response.getStatusLine().getStatusCode();

						if (statusCode != 200) {
							if (statusCode == 401) {
								MESSAGE.send(Common.MSG_LOGIN, null);
							}
							switch (statusCode) {
							case -1:
							case 0:
								message = "网络无法连接，请检查网络设置。";
								break;
							default:

								// url + String.format("\n网络错误:%d,请稍后重试。", statusCode)
								break;
							}
							HttpEntity entity = response.getEntity();
							String error = EntityUtils.toString(entity, "utf-8");
							if (!STRING.empty(error)) {
								JSONObject json = JSON.parse(error);
								if (json != null) {
									message = json.optString("error_msg");
									JSONObject error_detail= json.optJSONObject("error_detail");
									if(error_detail!=null){
										 Iterator<String> iterator = error_detail.keys();
								            while(iterator.hasNext()){
								                String key  =  iterator.next();
								                message += key + error_detail.optString(key);
								                
								            }
									}
								}
							}
							return null;
						}
						HttpEntity entity = response.getEntity();
						bos = new ByteArrayOutputStream();
						InputStream in = entity.getContent();
						byte[] datas = new byte[1024 * 2];
						int len = 0;
						while ((len = in.read(datas)) != -1) {
							bos.write(datas, 0, len);
						}
						in.close();
						bos.close();
					} catch (Exception e) {
						return null;
					}
					return bos.toByteArray();
				}

				protected void onPreExecute() {
				}

				@Override
				protected void onPostExecute(byte[] bytes) {
					callback.run(bytes == null, bytes);
				}

			}.execute(request);
		} catch (Exception e) {
			callback.run(true, null);
		}
	}

	static Map<String, Bitmap> BUFFER = new HashMap<String, Bitmap>();

	/**
	 * getImage 返回图片
	 * 
	 * @param url
	 *            服务器地址
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void getImage(final String url, final CALLBACK<Bitmap> callback) {


		if (CONFIG.getImage(url)!=null) {
			callback.run(false, CONFIG.getImage(url));
			return;
		}
		getBytes(url, null, Mode.GET, new CALLBACK<byte[]>() {

			@Override
			public void run(boolean isError, byte[] bytes) {
				if (isError) {
					callback.run(true, null);
					return;
				}
				try {
					Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					CONFIG.setImage(url,bitmap);
					callback.run(false, bitmap);
				} catch (Exception e) {
					callback.run(true, null);
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private static HttpRequestBase RequestFiles(String url, Map<String, Object> parameters,
			Map<String, InputStream> files, Mode mode) throws UnsupportedEncodingException {
		try {
			// OneKit.init();
			HttpRequestBase request;
			List<NameValuePair> pairs;
			String query = "";
			JSONObject QUERY = new JSONObject();
			MultipartEntity multipartEntity = new MultipartEntity();
			if (files != null) {
				if (files.keySet() != null) {
					for (String key : files.keySet()) {
						InputStream file = files.get(key);
						multipartEntity.addPart(key, key + ".png", file, false);
					}
				}
			}

			if (parameters != null) {

				int i = 0;
				for (String key : parameters.keySet()) {
					Object value = parameters.get(key);
					if (value == null) {
						continue;
					}
					String json;
					if (value.getClass().isArray()) {
						json = new JSONArray((Collection) value).toString();
					} else if (value.getClass() == Map.class) {
						json = new JSONObject((Map) value).toString();
					} else {
						json = value.toString();
					}
					String string = json;
					multipartEntity.addPart(key, string, i == (parameters.size() - 1));
					i++;

				}

			}

			switch (mode) {
			case GET:
				if (!STRING.empty(query)) {
					URL _url = new URL(url);
					if (STRING.empty(_url.getQuery())) {
						url = url + "?" + query.substring(1);
					} else {
						url = url + "&" + query.substring(1);
					}
				}
				HttpGet HttpGet = new HttpGet(url);
				request = HttpGet;
				break;
			case POST:
				HttpPost HttpPost = new HttpPost(url);
				HttpPost.setEntity(multipartEntity);

				request = HttpPost;
				break;
			case PUT:
				HttpPut HttpPut = new HttpPut(url);
				HttpPut.setEntity(multipartEntity);

				request = HttpPut;
				break;
			case DELETE:
				if (!STRING.empty(query)) {
					URL _url = new URL(url);
					if (STRING.empty(_url.getQuery())) {
						url = url + "?" + query.substring(1);
					} else {
						url = url + "&" + query.substring(1);
					}
				}
				HttpDelete HttpDelete = new HttpDelete(url);
				request = HttpDelete;
				break;
			default:
				request = null;
				break;
			}
			if (request == null) {
				return null;
			}
			if (getHeaders() != null) {
				setHeaders(request, getHeaders());
			} else {
				setHeaders(request, null);
			}
			String contentType = multipartEntity.getContentType().getValue();
			request.setHeader("Content-Type", contentType);
			// request.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
			// Charset.forName("UTF-8"));
			request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);

			return request;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * upload 上传
	 * 
	 * @param url
	 *            服务器地址
	 * @param parameters
	 *            传输数据
	 * @param files
	 *            图片地址
	 * @param callback
	 *            回调函数
	 * 
	 */
	public static void upload(final String url, Map<String, Object> parameters, Map<String, InputStream> files,
			Mode mode, final CALLBACK<JSONObject> callback) {
		try {
			final HttpRequestBase request = RequestFiles(url, parameters, files, mode);
			if (request == null) {
				return;
			}
			message = null;
			new AsyncTask<HttpRequestBase, Integer, String>() {

				@SuppressLint("DefaultLocale")
				@Override
				protected String doInBackground(HttpRequestBase... params) {
					try {
						HttpResponse response = new DefaultHttpClient().execute(request);
						HEADERS = response.getAllHeaders();
						Integer statusCode = response.getStatusLine().getStatusCode();

						if (statusCode != 200) {
							if (statusCode == 401) {
								MESSAGE.send(Common.MSG_LOGIN, null);
								return null;
							}
							switch (statusCode) {
							case -1:
							case 0:
								message = "网络无法连接，请检查网络设置。";
							default:
								message = url + String.format("\n网络错误:%d,请稍后重试。", statusCode);
								break;
							}
							HttpEntity entity = response.getEntity();
							String error = EntityUtils.toString(entity, "utf-8");
							if (!STRING.empty(error)) {
								JSONObject json = JSON.parse(error);
								if (json != null) {
									message = json.optString("error_msg");
									JSONObject error_detail= json.optJSONObject("error_detail");
									if(error_detail!=null){
										 Iterator<String> iterator = error_detail.keys();
								            while(iterator.hasNext()){
								                String key  =  iterator.next();  
								                message += "\n[" + key+"]"+error_detail.optString(key);
								                
								            }
									}
								}
							}
							return null;
						}
						HttpEntity entity = response.getEntity();
						String str = EntityUtils.toString(entity, "utf-8");
						return str;
					} catch (Exception e) {
						e.printStackTrace();
//						DIALOG.alert(e.getMessage());
						return null;
					}
				}

				protected void onPreExecute() {
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				protected void onPostExecute(String string) {
					if (message != null) {
//						DIALOG.alert(message, new CALLBACK() {
//
//							@Override
//							public void run(boolean isError, Object result) {
								callback.run(true, null);
//							}
//
//						});
					} else if (string == null) {
						callback.run(true, null);
					} else {
						JSONObject json = JSON.parse(string);
						callback.run(false, json);
					}
				}

			}.execute(request);
		} catch (Exception e) {
			DIALOG.warning(true, e);
			callback.run(true, null);
		}
	}

	private static String Stringdemo(String fileName) {
		String result = "";
		try {
			InputStream in = ACTIVITY.context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static byte[] ImageDemo(String fileName) {
		byte[] buffer = null;
		try {
			InputStream in = ACTIVITY.context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
