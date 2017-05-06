package cn.vipapps;

public class STRING {
	/**
	 * exists() 判断字符串是否为空
	 * 
	 * @param str
	 *            :字符串
	 * */
	public static boolean empty(String str) {
		return (str == null || str.equals(""));
	}
	public static String fill(int value, int length) {
		String RESULT = value + "";
		while (RESULT.length() < length) {
			RESULT = "0" + RESULT;
		}
		return RESULT;
	}
	/**
	 * toString 数组转字符串
	 * 
	 * @param array
	 *            :数组
	 * @param seperator
	 *            :分离参数
	 * */
	public static String toString(Object[] array, String seperator) {
		if (array == null) {
			return null;
		}
		String RESULT = "";
		for (Object item : array) {
			if (!RESULT.equalsIgnoreCase("")) {
				RESULT += seperator;
			}
			RESULT += item.toString();
		}
		return RESULT;
	}

	/**
	 * parse 字符串转数组
	 * 
	 * @param str
	 *            :字符串
	 * @param seperator
	 *            :分离参数
	 * */
	public static Object[] parse(String str, String seperator) {
		if (str == null) {
			return null;
		}
		Object[] stringArr = str.split(seperator);
		return stringArr;
	}


	/**
	 * 从字符串中删除指定的子串
	 *
	 * s是需要删除某个子串的字符串
	 * sub1是需要删除的子串的开头
	 * sub2是需要删除的子串的结尾
	* */
	public static String removeString(String s, String sub1, String sub2)
	{
		int postion1 = s.indexOf(sub1);
		int postion2 = s.indexOf(sub2);
		int Length = s.length();
		String newString = s.substring(0,postion1) + s.substring( postion2+1, Length);
		return newString;
	}
}
