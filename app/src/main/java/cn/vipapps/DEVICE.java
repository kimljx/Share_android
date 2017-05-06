package cn.vipapps;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;

import cn.vipapps.android.ACTIVITY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class DEVICE {
	
	/**
	 * 获取手机型号
	 * */
	@SuppressWarnings("unused")
	private static String phoneMode() {
		return android.os.Build.MODEL;
	}

	/***
	 * 获取设备ID
	 * 需要权限：android.permission.READ_PHONE_STATE
	 * */
	public static String ID() {
		TelephonyManager tm = (TelephonyManager) ACTIVITY.context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
		return DEVICE_ID;
	}

	/**
	 * 获取当前应用CPU使用情况
	 * */
	public static String CPU() {
		float totalCpuTime1 = getTotalCpuTime();
		float processCpuTime1 = getAppCpuTime();
		try {
			Thread.sleep(360);

		} catch (Exception e) {
		}

		float totalCpuTime2 = getTotalCpuTime();
		float processCpuTime2 = getAppCpuTime();

		float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
				/ (totalCpuTime2 - totalCpuTime1);

		return String.valueOf(cpuRate);
	}

	/**
	 * 获取可使用内存情况
	 * */
	@SuppressWarnings("deprecation")
	public static String Memroy() {
		// /proc/meminfo读出的内核信息进行解释
		// String path = "/proc/meminfo";
		// String content = null;
		// BufferedReader br = null;
		// try {
		// br = new BufferedReader(new FileReader(path), 8);
		// String line;
		// if ((line = br.readLine()) != null) {
		// content = line;
		// }
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// if (br != null) {
		// try {
		// br.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// // beginIndex
		// int begin = content.indexOf(':');
		// // endIndex
		// int end = content.indexOf('k');
		// // 截取字符串信息
		// Float a = Float.parseFloat(content.substring(begin + 1, end).trim());
		// Double d1 = Double.parseDouble(new DecimalFormat("#.##")
		// .format(a / 1024 / 1024));

		File path_ = Environment.getDataDirectory();
		StatFs stat = new StatFs(path_.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();

		Float b = (float) (blockSize * availableBlocks);
		Double d2 = Double.parseDouble(new DecimalFormat("#.##")
				.format(b / 1024 / 1024 / 10));

		return d2 + "MB";
	}

	/**
	 * 获取SD卡总大小
	 * */
	@SuppressWarnings("deprecation")
	public static String SD() {
		String sdCardInfo = new String();
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize();
			long bCount = sf.getBlockCount();

			sdCardInfo = Double.parseDouble(new DecimalFormat("#.##")
					.format(((float) (bSize * bCount) / 1024 / 1024 / 1024)))
					+ "GB";// 总大小
		}
		return sdCardInfo;
	}

	/**
	 * 获取网络状态
	 * 需要权限：android.permission.ACCESS_NETWORK_STATE
	 * */
	public static String NET() {
		String strNetworkType = "";

		NetworkInfo networkInfo = ((ConnectivityManager) ACTIVITY.context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = "WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();

				Log.e("cocos2d-x", "Network getSubtypeName : "
						+ _strSubTypeName);

				// TD-SCDMA networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
															// 11
					strNetworkType = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
															// 14
				case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
															// 12
				case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
															// 15
					strNetworkType = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
														// 13
					strNetworkType = "4G";
					break;
				default:
					// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
					if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
							|| _strSubTypeName.equalsIgnoreCase("WCDMA")
							|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
						strNetworkType = "3G";
					} else {
						strNetworkType = _strSubTypeName;
					}

					break;
				}

				Log.e("cocos2d-x",
						"Network getSubtype : "
								+ Integer.valueOf(networkType).toString());
			}
		} else {
			strNetworkType = "没有连接网络";
		}

		Log.e("cocos2d-x", "Network Type : " + strNetworkType);

		return strNetworkType;
	}

	private static long getTotalCpuTime() { // 获取系统总CPU使用时间
		String[] cpuInfos = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		long totalCpu = Long.parseLong(cpuInfos[2])
				+ Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
				+ Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
				+ Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
		return totalCpu;
	}

	private static long getAppCpuTime() { // 获取应用占用的CPU时间
		String[] cpuInfos = null;
		try {
			int pid = android.os.Process.myPid();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/" + pid + "/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		long appCpuTime = Long.parseLong(cpuInfos[13])
				+ Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
				+ Long.parseLong(cpuInfos[16]);
		return appCpuTime;
	}

}
