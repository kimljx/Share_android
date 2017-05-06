package cn.vipapps;

import android.graphics.Color;

public class COLOR {

	/**
	 * 颜色转字符串
	 * 
	 * @param intColor 颜色
	 * */
	public static String toString(Integer intColor) {
		if (intColor == 0) {
			return "#00000000";
		}
		String red = Integer.toHexString((intColor & 0xff0000) >> 16);
		String green = Integer.toHexString((intColor & 0x00ff00) >> 8);
		String blue = Integer.toHexString((intColor & 0x0000ff));
		if (red.length() == 1) {
			red = String.format("%1s%2s", 0, red).replace(" ", "");
		}
		if (green.length() == 1) {
			green = String.format("%1s%2s", 0, green).replace(" ", "");
		}
		if (blue.length() == 1) {
			blue = String.format("%1s%2s", 0, blue).replace(" ", "");
		}
		return '#' + red + green + blue;
	}

	/**
	 * 字符串转颜色
	 * 
	 * @param str 字符串
	 * */
	@SuppressWarnings("static-access")
	public static Integer parse(String str) {
		Color color = new Color();
		return color.parseColor(str);
	}
}
