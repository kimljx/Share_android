package cn.vipapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import cn.vipapps.android.ACTIVITY;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CONFIG {

	/**
	 * set 用户配置
	 *
	 * @param key
	 *            键值
	 * @param Value
	 *            数据
	 */




	public static void set(String key, Object Value) {
		Context context = ACTIVITY.context;
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = spf.edit();
		edit.putString(key, Value != null ? Value.toString() : null);
		edit.commit();
	}

	/**
	 * set 获取配置
	 *
	 * @param key
	 *            键值
	 */
	public static Object get(String key) {
		Context context = ACTIVITY.context;
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
		return spf.getString(key, null);
	}

	public static void setString(String key, String string){
		Context context = ACTIVITY.context;
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
		Editor edit = spf.edit();
		edit.putString(key,string);
		edit.commit();
	}
	public static String getString(String key){
		Context context = ACTIVITY.context;
		SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
		return spf.getString(key,null);
	}

	/**
	 * 保存图片到SharedPreferences
	
	 * @param key
	 * @param image
	 */
	public static void setImage(String key, Bitmap image){
		ByteArrayOutputStream bStream=new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG,100,bStream);
		byte[] bytes = bStream.toByteArray();
		String string = Base64.encodeToString(bytes, Base64.DEFAULT);
		try {
			bStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setString(key,string);
	}

	public static Bitmap getImage(String key){
		String string = getString( key);
		byte[] byteArray= Base64.decode(string, Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
		Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
		try {
			byteArrayInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 存JSONObject到SharedPreferences
	
	 * @param key
	 * @param jsonObject
	 */
	public static void setJSON(String key, JSONObject jsonObject){
		String stringify = JSON.stringify(jsonObject);
		setString(key,stringify);
	}
	/**
	 * 存JSONArray到SharedPreferences
	
	 * @param key
	 * @param jsonArray
	 */
	public static void setJSONs(String key, JSONArray jsonArray){
		String stringify = JSON.stringify(jsonArray);
		setString(key,stringify);
	}
	/**
	 * 获取JSON
	  上下文
	 * @param key
	 * @return
	 */
	public static JSONObject getJSON(String key){
		String string = getString( key);
		JSONObject jsonObject = JSON.parse(string);
		return jsonObject;
	}
	/**
	 * 获取JSONArray
	  上下文
	 * @param key
	 * @return
	 */
	public static JSONArray getJSONs(String key){
		String string = getString( key);
		JSONArray jsonArray = JSON.parses(string);
		return jsonArray;
	}
}
