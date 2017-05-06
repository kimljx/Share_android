package cn.vipapps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import cn.vipapps.android.ACTIVITY;

public class CAMERA extends ACTIVITY {

	private static final int VIDEO_CAPTURE = 200;


	/**
	 * 打开相机
	 * 
	 * @param
	 *            ：android.permission.CAMERA,
	 *            android.permission.WRITE_EXTERNAL_STORAGE
	 * */
	public static void openCamera(final CALLBACK<Bitmap> callback) {
		ACTIVITY.openCamera(new CALLBACK<Bitmap>() {
			public void run(boolean isError, Bitmap result) {
				callback.run(false, result);
			}
		});
	}
	public static void openCameraUri(final CALLBACK<Uri> callback) {
		ACTIVITY.openCameraUri(new CALLBACK<Uri>() {
			public void run(boolean isError, Uri result) {
				callback.run(false, result);
			}
		});
	}
	/**
	 * 打开录像机
	 *
	 * @param
	 *
	 *            ：android.permission.RECORD_AUDIO,
	 *            android.permission.WRITE_EXTERNAL_STORAGE
	 * */
	public static void openRecord() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		((Activity) ACTIVITY.context).startActivityForResult(intent,
				VIDEO_CAPTURE);
	}

	/**
	 * 打开相册
	 * */
	public static void openUri(final CALLBACK<Uri> callback) {
		ACTIVITY.openUri(new CALLBACK<Uri>() {
			public void run(boolean isError, Uri result) {
				callback.run(false, result);
			}
		});
	}
	public static void openGallery(final CALLBACK<Bitmap> callback) {
		ACTIVITY.openGallery(new CALLBACK<Bitmap>() {
			public void run(boolean isError, Bitmap result) {
				callback.run(false, result);
			}
		});
	}
	public static void openMini(final CALLBACK<Bitmap> callback) {
		ACTIVITY.openMini(new CALLBACK<Bitmap>() {
			public void run(boolean isError, Bitmap result) {
				callback.run(false, result);
			}
		});
	}
	public static void openGalleryUri(final CALLBACK<Uri> callback) {
		ACTIVITY.openGalleryUri(new CALLBACK<Uri>() {
			public void run(boolean isError, Uri result) {
				callback.run(false, result);
			}
		});
	}
}