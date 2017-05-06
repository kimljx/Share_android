package cn.vipapps;

import android.app.Activity;
import android.util.DisplayMetrics;

import cn.vipapps.android.ACTIVITY;

public class SCREEN {
	static private SIZE size;

	public static SIZE size() {
		if (size != null) {
			return size;
		}
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) ACTIVITY.context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		final int width = metric.widthPixels;
		final int height = metric.heightPixels;
		size = new SIZE(width, height);
		return size;
	}
}
