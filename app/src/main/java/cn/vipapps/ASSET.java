package cn.vipapps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.vipapps.android.ACTIVITY;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ASSET {

	public static String loadString(String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					ACTIVITY.context.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject loadJSON(String fileName) {
		String string = null;

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1, fileName.length());
		}

		if (fileName.endsWith("/")) {
			fileName = fileName.substring(0, fileName.length() - 1);
		}

		string = loadString(fileName);
		if (STRING.empty(string)) {
			return null;
		}
		return JSON.parse(string);
	}

	public static JSONArray loadJSONs(String fileName) {
		String string = null;

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1, fileName.length());
		}

		if (fileName.endsWith("/")) {
			fileName = fileName.substring(0, fileName.length() - 1);
		}

		string = loadString(fileName);
		if (STRING.empty(string)) {
			return null;
		}
		return JSON.parses(string);
	}

	@SuppressWarnings("deprecation")
	public static Bitmap loadImage(String fileName, boolean isMini) {
		try {
			InputStream stream = ACTIVITY.context.getResources().getAssets().open(fileName);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			if (isMini) {
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				options.inSampleSize = 2;
			}
			Bitmap RESULT = BitmapFactory.decodeStream(stream, null, options);
			// AJAX.DATA.put(name, RESULT);
			stream.close();
			return RESULT;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void copy(String fromPath, String toPath) {
		InputStream myInput;
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(toPath);
			myInput = ACTIVITY.context.getAssets().open(fromPath);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}
			myOutput.flush();
			myInput.close();
			myOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}