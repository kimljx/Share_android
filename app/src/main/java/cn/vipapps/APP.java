package cn.vipapps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.vipapps.android.ACTIVITY;

import java.lang.reflect.Field;

public class APP {
	/**
	 * appID 查看appID
	 */
	public static String appID() {
		return ACTIVITY.context.getPackageName();
	}

	public static void openApp(String appID) {
		try {
			Intent intent = ACTIVITY.context.getPackageManager().getLaunchIntentForPackage(appID);
			ACTIVITY.context.startActivity(intent);
		} catch (Exception e) {
			if (appID.equalsIgnoreCase("com.android.phone")) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				ACTIVITY.context.startActivity(intent);
				return;
			}
			if (appID.equalsIgnoreCase("com.android.sms")) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setType("vnd.android-dir/mms-sms");
				ACTIVITY.context.startActivity(intent);
				return;
			}
			if (appID.equalsIgnoreCase("com.android.camera")) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				ACTIVITY.context.startActivity(intent);
				return;
			}
			e.printStackTrace();
			DIALOG.alert("app isn't exist!");
		}
	}

	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	static public String getVersion() {
		try {
			PackageManager manager = ACTIVITY.context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(ACTIVITY.context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "找不到版本号！";
		}
	}

	/**
	 * openUrl() 打开浏览器
	 *
	 * @param url
	 *            网页地址
	 */
	public static void openUrl(final String url) {
		DIALOG.confirm("确认打开" + url + "？", new CALLBACK<Object>() {
			public void run(boolean isError, Object result) {
				if (isError) {
					return;
				}
				if (url.startsWith("http://")) {
					ACTIVITY.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} else {
					ACTIVITY.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url)));
				}
			}
		}, new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
			}
		});
	}

	/**
	 * openEmail 发送Email
	 *
	 //	 * @param url
	 *            收件人
	 */
	public static void openEmail(final String title) {
		DIALOG.confirm("确认向" + title + "发送邮件？", new CALLBACK<Object>() {
			public void run(boolean isError, Object result) {
				if (isError) {
					return;
				}
				Intent data = new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse("mailto:" + title));
				ACTIVITY.context.startActivity(data);
			}
		}, new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
			}
		});
	}

	/**
	 * callPhone 打电话 需要权限：android.permission.CALL_PHONE
	 *
	 * @param number
	 *            电话号码
	 */
	public static void callPhone(final String number) {
		DIALOG.confirm("确定拨打" + number + "?", new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));

				if (ActivityCompat.checkSelfPermission(ACTIVITY.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				ACTIVITY.context.startActivity(intent);
			}
		}, new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
			}
		});
	}

	/**
	 * sendSMS 发短信
	 * 
	 * @param number
	 *            电话号码
	 */
	public static void sendSMS(final String number) {
		DIALOG.confirm("确定向" + number + "发送短信?", new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
				ACTIVITY.context.startActivity(intent);
			}
		}, new CALLBACK<Object>() {

			@Override
			public void run(boolean isError, Object obj) {
				if (isError) {
					return;
				}
			}
		});
	}

	public static int statusHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return ACTIVITY.context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static int navigationHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("navigation_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return ACTIVITY.context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static void hideKeyboard(View editText) {
		InputMethodManager inputmanger = (InputMethodManager) ACTIVITY.context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
