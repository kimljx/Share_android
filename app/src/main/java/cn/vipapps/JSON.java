package cn.vipapps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSON {

	/**
	 * 序列化
	 * 
	 */
	public static JSONObject parse(Object string) {
		if (string == null) {
			return null;
		}
		try {
			return new JSONObject(string.toString());
		} catch (Exception e) {
			e.printStackTrace();
//			Log.e("========", string + "");
//			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 序列化
	 */
	public static JSONArray parses(Object string) {
		try {
			return new JSONArray(string.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 反序列化
	 */
	public static String stringify(JSONObject json) {
		try {
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 反序列化
	 */
	public static String stringify(JSONArray json) {
		try {
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(JSONArray json) {
		List<T> RESULT = new ArrayList<T>();
		for (int i = 0; i < json.length(); i++) {
			Object item = json.opt(i);
			RESULT.add((T) item);
		}
		return RESULT;
	}

	public static Map<String, Object> toMap(JSONObject json) {
		Map<String, Object> RESULT = new HashMap<String, Object>();
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object item = json.opt(key);
			RESULT.put(key, item);
		}
		return RESULT;
	}

	public static JSONArray fromList(@SuppressWarnings("rawtypes") List list) {
		JSONArray RESULT = new JSONArray();
		for (Object item : list) {
			RESULT.put(item);
		}
		return RESULT;
	}

	public static JSONObject fromMap(Map<String, Object> map) {
		JSONObject RESULT = new JSONObject();
		for (String key : map.keySet()) {
			Object item = map.get(key);
			try {
				RESULT.put(key, item);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return RESULT;
	}

	public static Object find(JSONArray array, Object value) {

		for (int i = 0; i < array.length(); i++) {
			Object item = array.opt(i);
			if (item.toString().equals(value.toString())) {
				return item;
			}
		}
		return null;
	}

	public static boolean contains(JSONArray array, Object value) {
		return find(array, value) != null;
	}

	public static JSONObject find(JSONArray array, String key, JSONObject value) {
		for (int i = 0; i < array.length(); i++) {
			JSONObject item = array.optJSONObject(i);
			if (item.optString(key).equalsIgnoreCase(value.optString(key))) {
				return item;
			}
		}
		return null;
	}

	public static boolean contains(JSONArray array, String key, JSONObject value) {
		return find(array, key, value) != null;
	}

	/*
	 * public static Date toDate(String str) { String str1 = "/Date("; String
	 * str2 = ")/"; str = str.substring(str1.length(), str.length() -
	 * str2.length()); int p = str.indexOf('+'); if (p >= 0) { str =
	 * str.substring(0, p); } Date date = new Date(Long.valueOf(str)); return
	 * date; }
	 */
	/*
	 * public static JSONArray remove(JSONArray array, JSONObject item,String
	 * key) { if (array == null) { return null; } try { JSONArray RESULT = new
	 * JSONArray(); for (int i = 0; i < array.length(); i++) { Object itm =
	 * array.get(i); if (!item.equals(itm)) { RESULT.put(itm); } } return
	 * RESULT; } catch (Exception e) { return array; } }
	 */
	public static JSONArray remove(JSONArray array, Object item) {
		if (array == null) {
			return null;
		}

		JSONArray RESULT = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			Object itm = array.opt(i);
			if (!item.toString().equalsIgnoreCase(itm.toString())) {
				RESULT.put(itm);
			}
		}
		return RESULT;

	}

	public static JSONArray remove(JSONArray array, JSONObject item, String key) {
		if (array == null) {
			return null;
		}
		JSONArray RESULT = new JSONArray();
		for (int i = 0; i < array.length(); i++) {
			JSONObject itm = array.optJSONObject(i);
			if (!item.optString(key).toString().equalsIgnoreCase(itm.optString(key).toString())) {
				RESULT.put(itm);
			}
		}
		return RESULT;

	}
}
