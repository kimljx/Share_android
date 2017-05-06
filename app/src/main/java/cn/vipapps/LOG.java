package cn.vipapps;

import android.util.Log;

import cn.vipapps.android.ACTIVITY;

public class LOG {
	public static void v(String msg) {
		if (OneKit.DEBUG) {
			Log.v(ACTIVITY.context.toString(), msg);
		}
	}

	public static void d(String msg) {
		if (OneKit.DEBUG) {
			Log.d(ACTIVITY.context.toString(), msg);
		}
	}

	public static void i(String msg) {
		if (OneKit.DEBUG) {
			Log.i(ACTIVITY.context.toString(), msg);
		}
	}

	public static void w(String msg) {
		if (OneKit.DEBUG) {
			Log.w(ACTIVITY.context.toString(), msg);
		}
	}

	public static void e(String msg) {
		if (OneKit.DEBUG) {
			Log.e(ACTIVITY.context.toString(), msg);
		}
	}
}
