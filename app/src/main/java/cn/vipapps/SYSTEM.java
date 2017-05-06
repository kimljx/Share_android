package cn.vipapps;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import cn.vipapps.android.ACTIVITY;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SYSTEM {
	static ProgressDialog _loading;

	public static void getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2 = "";
		try {
			FileReader fr = new FileReader(str1);
			@SuppressWarnings("resource")
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				Log.i("DEVICE", "---" + str2);
			}
		} catch (IOException e) {

		}
	}

	public static long getAvailMemory() {
		ActivityManager am = (ActivityManager) ACTIVITY.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}

	@SuppressWarnings("deprecation")
	public static long[] getRomMemroy() {
		long[] romInfo = new long[2];
		romInfo[0] = getTotalInternalMemorySize();
		java.io.File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		romInfo[1] = blockSize * availableBlocks;
		getVersion();
		return romInfo;
	}

	@SuppressWarnings("deprecation")
	public static long getTotalInternalMemorySize() {
		java.io.File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	@SuppressWarnings("deprecation")
	public static long[] getSDCardMemory() {
		long[] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			java.io.File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize();
			long bCount = sf.getBlockCount();
			long availBlocks = sf.getAvailableBlocks();

			sdCardInfo[0] = bSize * bCount;// �ܴ�С
			sdCardInfo[1] = bSize * availBlocks;// ���ô�С
		}
		return sdCardInfo;
	}

	@SuppressWarnings("unused")
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
		}
	};

	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	public static String[] getVersion() {
		String[] version = { "null", "null", "null", "null" };
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			version[0] = arrayOfString[2];// KernelVersion
			localBufferedReader.close();
		} catch (IOException e) {
		}
		version[1] = Build.VERSION.RELEASE;// firmware version
		version[2] = Build.MODEL;// model
		version[3] = Build.DISPLAY;// system version
		return version;
	}

	public static String getVersionString() {
		String[] array = getVersion();
		String RESULT = new String();
		for (String str : array) {
			RESULT += str + ";";
		}
		return RESULT;
	}

	/**
	 * 判断应用是否已经启动
	 * @param context 一个context
	 * @param packageName 要判断应用的包名
	 * @return boolean
	 */
	public static boolean isAppAlive(Context context, String packageName){
		ActivityManager activityManager =
				(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processInfos
				= activityManager.getRunningAppProcesses();
		for(int i = 0; i < processInfos.size(); i++){
			if(processInfos.get(i).processName.equals(packageName)){
				Log.i("NotificationLaunch",
						String.format("the %s is running, isAppAlive return true", packageName));
				return true;
			}
		}
		Log.i("NotificationLaunch",
				String.format("the %s is not running, isAppAlive return false", packageName));
		return false;
	}

}
