package cn.vipapps;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import cn.vipapps.android.ACTIVITY;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 需要写入文件的权限：android.permission.WRITE_EXTERNAL_STORAGE
 */
public class FSO {
	public static String COOKIE;
	int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

	private static String getCachDir() {
		return String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s", Environment.getExternalStorageDirectory(),
				File.separator, "Android", File.separator, "data", File.separator,
				ACTIVITY.context.getPackageName(), File.separator, "cache");
	}

	@SuppressWarnings("unused")
	private static void createCachDir() {
		File file = new File(getCachDir());
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private static String SDPATH() {
		return Environment.getExternalStorageDirectory() + "/";
	}

	public static File parse(Uri pictureUri) {
		Cursor cursor = ACTIVITY.context.getContentResolver().query(pictureUri, null, null, null, null);
		String st8 = "找不到图片";
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(ACTIVITY.context, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return null;
			} else {
				return new File(picturePath);
			}
		} else {
			File file = new File(pictureUri.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(ACTIVITY.context, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return null;
			} else {
				return file;
			}
		}
	}

	/**
	 * exists() 是否为空
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static Boolean exists(File dirPath, String fileName) {
		File file = new File(dirPath, fileName);
		return file.exists();
	}

	public static Boolean exists(String dirPath) {
		File file = new File(dirPath);
		return file.exists();
	}

	/**
	 * exists() 是否存在
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static Boolean exists(String dirPath, String fileName) {
		File file = new File(dirPath, fileName);
		return file.exists();
	}

	public static Boolean delete(String path) {
		return delete(new File(path));
	}

	/**
	 * deleteData 删除文件
//	 *
//	 * @param dirPath
//	 *            :文件路径
//	 * @param fileName
//	 *            :文件名
	 */
	public static Boolean delete(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				}
			}
			file.delete();
			// DIALOG.alert("删除成功");
			return true;
		} else {
			DIALOG.alert("文件不存在");
			return false;
		}
	}

	/**
	 * loadData 加载二进制
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static byte[] loadData(File dirPath, String fileName) {
		File file = new File(dirPath, fileName);
		int sum = 0;
		byte[] b = new byte[4];
		try {
			DataInputStream instr = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			sum += instr.readInt();
			sum += instr.readInt();
			sum += instr.readInt();

			for (int i = 0; i < 4; i++) {
				b[i] = (byte) (sum >> (24 - i * 8));
			}
			System.out.println("this sum is :" + sum);
			instr.close();
		} catch (IOException iox) {
		}
		return b;
	}

	/**
	 * savaData 保存二进制
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static void savaData(File dirPath, String fileName) {
		File file = new File(dirPath, fileName);
		int value0 = 255, value1 = 0, value2 = -1;
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.writeInt(value0);
			out.writeInt(value1);
			out.writeInt(value2);
			out.close();
		} catch (IOException io) {
			System.out.println("problem writing " + file);
		}
	}

	/**
	 * loadString 加载字符串
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static String loadString(File dirPath, String fileName) {
		StringBuffer RESULT = new StringBuffer();
		if (dirPath.toString().startsWith("http://")) {
			dirPath = new File(dirPath.toString().substring(7, dirPath.toString().length()));
		}
		if (fileName.startsWith("http://")) {
			fileName = fileName.substring(7, fileName.length());
		}
		try {
			File file = new File(dirPath, fileName);
			if (file.exists()) { // 判断文件是否存在
				// Resources Resources = APP.context.getResources();
				// InputStream stream =
				// Resources.openRawResource(Resources.getIdentifier(String.format("%1$s.%2$s",
				// name, ext), "raw",APP.context.getPackageName()));
				InputStream stream = new FileInputStream(file);
				InputStreamReader streamReader = new InputStreamReader(stream);
				BufferedReader bufferedReader = new BufferedReader(streamReader);
				String temp;
				while ((temp = bufferedReader.readLine()) != null) {
					RESULT.append(temp);
				}
				bufferedReader.close();
				streamReader.close();
				stream.close();
				return RESULT.toString();
			} else {
				DIALOG.alert("文件不存在");
			}
		} catch (IOException e) {
			DIALOG.warning(true, e);
			return null;
		}
		return null;
	}

	/**
	 * saveString 保存字符串
	 *
//	 * @param name
	 *            :文件名
	 * @param string
	 *            :字段
	 */
	public static void saveString(String fileName, String string) {

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1, fileName.length());
		}

		if (fileName.endsWith("/")) {
			fileName = fileName.substring(0, fileName.length() - 1);
		}

		try {
			int one = fileName.lastIndexOf("/");
			String strFile = fileName.substring(0, (one + 1));
			String strFileName = fileName.substring((one + 1), fileName.length());
			File file = new File(SDPATH(), String.format("%1s%2s", strFile, "/"));
			FileOutputStream stream = new FileOutputStream(makeFilePath(file.toString(), strFileName), true);
			OutputStreamWriter writer = new OutputStreamWriter(stream, "utf-8");
			writer.write(string);
			writer.flush();
			writer.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File makeFilePath(String filePath, String fileName) {
		File file = null;
		makeRootDirectory(filePath);
		try {
			file = new File(String.format("%1s%2s", filePath, fileName).replace(" ", ""));
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath.replace(" ", ""));
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
		}
	}

	/*
	 * public static boolean checkFileExists(String path) { java.io.File file =
	 * new java.io.File(SDPATH() + path); return file.exists(); }
	 *
	 * public static java.io.File createDIR(String dirpath) { java.io.File dir =
	 * new java.io.File(SDPATH() + dirpath); dir.mkdir(); return dir; }
	 *
	 * public static java.io.File createFile(String filepath) throws IOException
	 * { java.io.File file = new java.io.File(SDPATH() + filepath);
	 * file.createNewFile(); return file; }
	 *
	 * public static boolean deleteFile(String filepath) throws IOException {
	 * java.io.File file = new java.io.File(SDPATH() + filepath); return
	 * file.delete(); }
	 *
	 * public static java.io.File writeStreamToSDCard(String dirpath, String
	 * filename, InputStream input) { java.io.File file = null; OutputStream
	 * output = null; try { createDIR(dirpath); file = createFile(dirpath +
	 * filename); output = new FileOutputStream(file); byte[] bt = new byte[4 *
	 * 1024]; while (input.read(bt) != -1) { output.write(bt); } output.flush();
	 * } catch (IOException e) { e.printStackTrace(); } finally {
	 *
	 * try { output.close(); } catch (Exception e) { e.printStackTrace(); } }
	 *
	 * return file;
	 *
	 * }
	 */
	/**
	 * loadJSON 加载JSON
	 *
	 * @param dirPath
	 *            :文件路径
	 * @param fileName
	 *            :文件名
	 */
	public static JSONObject loadJSON(File dirPath, String fileName) {
		String string = null;

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1, fileName.length());
		}

		if (fileName.endsWith("/")) {
			fileName = fileName.substring(0, fileName.length() - 1);
		}

		string = loadString(dirPath, fileName);
		if (STRING.empty(string)) {
			return null;
		}
		return JSON.parse(string);
	}

	public static JSONArray loadJSONs(File dirPath, String fileName) {
		String string = null;

		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1, fileName.length());
		}

		if (fileName.endsWith("/")) {
			fileName = fileName.substring(0, fileName.length() - 1);
		}

		string = loadString(dirPath, fileName);
		if (STRING.empty(string)) {
			return null;
		}
		return JSON.parses(string);
	}

	/***
	 * saveJSON 保存JSON
	 *
	 * @param fileName
	 *            :保存的文件名
	 * @param name
	 *            :JSON数组（字典）
	 *
	 */
	public static void saveJSON(String fileName, JSONObject name) {
		saveString(fileName, JSON.stringify(name));
	}

	/**
	 * loadImage 加载图片
	 *
//	 * @param dirPath
//	 *            :文件路径
//	 * @param name
	 *            :文件名
	 * @param isMini
	 *            :是否压缩
	 */
	@SuppressWarnings("deprecation")
	public static Bitmap loadImage(String path, boolean isMini) {
		try {
			Bitmap RESULT;
			FileInputStream stream = new FileInputStream(new File(path));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inInputShareable = true;
			if (isMini) {
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inSampleSize = 2;
			}
			RESULT = BitmapFactory.decodeStream(stream, null, options);
			// AJAX.DATA.put(name, RESULT);
			stream.close();
			stream = null;
			return RESULT;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * saveImage 保存图片
	 *
	 * @param bm
	 *            :图片
//	 * @param dirPath
//	 *            :文件路径
//	 * @param fileName
	 *            :文件名
	 */
	public static void saveImage(final Bitmap bm, final String path) {
		File imageFile = new File(path);
		if (!imageFile.exists()) {
			try {
				imageFile.createNewFile();
			} catch (IOException e) {
			}
		}
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(imageFile));
			if (path.endsWith("jpg")) {
				bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
			} else if (path.endsWith("png")) {
				bm.compress(Bitmap.CompressFormat.PNG, 90, bos);
			}
			bos.flush();
			bos.close();
		} catch (IOException e) {
			DIALOG.warning(e);
		}

	}

	public static File photos(String album) {
		File path = ACTIVITY.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		if (path == null) {
			throw new Error("[PATH]" + path);
		}
		return new File(path, album);
	}

	public static void copy(String fromPath, String toPath) {
		InputStream myInput;
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(toPath);
			myInput = new FileInputStream(fromPath);
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