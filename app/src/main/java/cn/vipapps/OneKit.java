package cn.vipapps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import cn.share.BuildConfig;
import cn.vipapps.android.ACTIVITY;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class OneKit {

	private static String common_yes = "确定", common_no = "取消";
	public static Boolean DEBUG = BuildConfig.DEBUG;

	/**
	 * fix(Object obj)
	 * 
	 * @Object obj Object转String Object为null，则返回""
	 * */
	public static String fix(Object obj) {
		return (obj == null ? "" : obj.toString());
	}

	/**
	 * fixWith(Object obj, Object value)
	 * 
	 * @Object obj
	 * @Object value 如果obj为null则返回obj，否则返回value
	 * */
	public static Object fixWith(Object obj, Object value) {
		return (obj == null ? value : obj);
	}

	private static Boolean checking = false;

	static void init() {//初始化
		if (checking) {
			return;
		}
		checking = true;
		String appID = ACTIVITY.context.getPackageName();
		String url = String.format("http://www.onekit.cn/app/%s.txt", appID);
		HttpGet request = new HttpGet(url);
		new AsyncTask<HttpRequestBase, Integer, String>() {

			@Override
			protected String doInBackground(HttpRequestBase... params) {
				try {
					HttpResponse response = new DefaultHttpClient()
							.execute(params[0]);
					Integer statusCode = response.getStatusLine()
							.getStatusCode();

					if (statusCode != 200) {
						return null;
					}
					HttpEntity entity = response.getEntity();

					return EntityUtils.toString(entity, "utf-8");
				} catch (Exception e) {
					return null;
				}

			}

			@Override
			protected void onPostExecute(String string) {
				checking = false;
				if (!isWifiEnable(ACTIVITY.context)
						&& !isNetworkConnected(ACTIVITY.context)) {
					alert("请连接网络");
					return;
				}
				if (STRING.empty(string)) {
					new AlertDialog.Builder(ACTIVITY.context)
							.setTitle(null)
							.setMessage("OneKit无效，请到www.onekit.cn购买正版。")
							.setPositiveButton(common_yes,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ACTIVITY.context.startActivity(new Intent(
													Intent.ACTION_VIEW,
													Uri.parse("http://www.onekit.cn")));
											((Activity) ACTIVITY.context)
													.finish();
											System.exit(0);
										}
									})
							.setNegativeButton(common_no,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											System.exit(0);
										}
									}).show().setCancelable(false);
					return;
				}

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				ParsePosition pos = new ParsePosition(0);
				Date expire = formatter.parse(string, pos);
				if (expire == null) {
					new AlertDialog.Builder(ACTIVITY.context)
							.setTitle(null)
							.setMessage("有效期无效，请到www.onekit.cn购买正版。")
							.setPositiveButton(common_yes,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ACTIVITY.context.startActivity(new Intent(
													Intent.ACTION_VIEW,
													Uri.parse("http://www.onekit.cn")));
											((Activity) ACTIVITY.context)
													.finish();
											System.exit(0);
										}
									})
							.setNegativeButton(common_no,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											System.exit(0);
										}
									}).show().setCancelable(false);

					return;
				}
				long tick = new Date().getTime();
				tick += 8 * 60 * 60;
				Date now = new Date(tick);
				if (expire.before(now)) {
					new AlertDialog.Builder(ACTIVITY.context)
							.setTitle(null)
							.setMessage("OneKit过期，请到www.onekit.cn续费。")
							.setPositiveButton(common_yes,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ACTIVITY.context.startActivity(new Intent(
													Intent.ACTION_VIEW,
													Uri.parse("http://www.onekit.cn")));
											((Activity) ACTIVITY.context)
													.finish();
											System.exit(0);
										}
									})
							.setNegativeButton(common_no,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											System.exit(0);
										}
									}).show().setCancelable(false);
					return;
				}
			}
		}.execute(request);
		return;
	}

	// Wifi是否可用
	private static boolean isWifiEnable(Context context) {
		@SuppressWarnings("static-access")
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	// 是否有可用网络
	private static boolean isNetworkConnected(Context context) {
		@SuppressWarnings("static-access")
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getActiveNetworkInfo();
		if (network != null) {
			return network.isAvailable();
		}
		return false;
	}

	private static void alert(String message) {
		new AlertDialog.Builder(ACTIVITY.context)
				.setTitle(null)
				.setMessage(message)
				.setPositiveButton(common_yes,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).show();
	}

}
