package cn.vipapps.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;
import cn.vipapps.MESSAGE;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ios.ui.UINavigationBar;

@SuppressLint("SimpleDateFormat")
public class ACTIVITY extends FragmentActivity {

	public static Context context;
	private static File outFile;
	private static CALLBACK<Bitmap> imageCallback;
	private static CALLBACK<Uri> uriCallback;
	private static final int IMAGE_CAPTURE = 100;
	private static final int IMAGE_CAPTURE_PATH = 101;
	private static final int VIDEO_CAPTURE = 200;
	private static final int GET_CONTENT = 300;
	private static final int GET_CONTENT_PATH = 301;
	private static final int GET_MINI = 400;
	private static final int GET_URI = 500;
	ActionBar actionBar;

	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

	public android.app.ActionBar getActionBar() {
		return (actionBar != null ? actionBar : super.getActionBar());
	}

	protected void fixActionBar(int rootViewID, int layoutID) {
		actionBar = new ActionBar(this, layoutID);
		actionBar.setIcon(context.getApplicationInfo().loadIcon(context.getPackageManager()));
		((ViewGroup) this.findViewById(rootViewID)).addView(actionBar._actionBarView, 0);
		actionBar.setTitle(this.getTitle());
	}

	protected ActionBar getActionBar2() {
		return actionBar;
	}

	public final int getStringID(String resID) {
		return getResources().getIdentifier(resID, "string", getPackageName());
	}

	public final int getDrawableID(String resID) {
		return getResources().getIdentifier(resID, "drawable", getPackageName());
	}

	protected final int getRawID(String resID) {
		return getResources().getIdentifier(resID, "raw", getPackageName());
	}

	final static float scale() {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int dp2px(float dp) {
		return (int) (dp * scale() + 0.5f);
	}

	public static int px2dp(float px) {
		return (int) (px / scale() + 0.5f);
	}
	/*
	 * public static int dp2sp(float dp) { return (int)
	 * (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
	 * context.getResources().getDisplayMetrics())); }
	 */

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);

	}

	@Override
	protected void onResume() {
		super.onResume();
		context = this;
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
//		try {
//			context = createPackageContext("com.shengdeshi.im", Context.CONTEXT_IGNORE_SECURITY);
//		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//		}
		context = this;
	}

	protected void onDestroy() {
		super.onDestroy();
		MESSAGE.clear(this);
	}

	protected static void openCamera(CALLBACK<Bitmap> callback) {
		imageCallback = callback;
		String state = Environment.getExternalStorageState();
		try {
			if (state.equals(Environment.MEDIA_MOUNTED)) {
			/*	File outDir = new File(String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s",
						Environment.getExternalStorageDirectory(), java.io.File.separator, "Android",
						java.io.File.separator, "data", java.io.File.separator, ACTIVITY.context.getPackageName(),
						java.io.File.separator, "Image"));
				if (!outDir.exists()) {
					outDir.mkdirs();
				}
				outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
				((Activity) ACTIVITY.context).startActivityForResult(intent, IMAGE_CAPTURE);*/
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//intent.putExtra(MediaStore.EXTRA_OUTPUT,
				//		Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
				((Activity) ACTIVITY.context).startActivityForResult(intent, IMAGE_CAPTURE);
			} else {
				DIALOG.toast("���������������������SD���");
			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			DIALOG.toast("������������������");
		}
	}
	protected static void openCameraUri(CALLBACK<Uri> callback) {
		uriCallback = callback;
		String state = Environment.getExternalStorageState();
		try {
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				File outDir = new File(String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s",
						Environment.getExternalStorageDirectory(), File.separator, "Android",
						File.separator, "data", File.separator, ACTIVITY.context.getPackageName(),
						File.separator, "Image"));
				if (!outDir.exists()) {
					outDir.mkdirs();
				}
				outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
				((Activity) ACTIVITY.context).startActivityForResult(intent, IMAGE_CAPTURE_PATH);
			} else {
				DIALOG.toast("���������������������SD���");
			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			DIALOG.toast("������������������");
		}
	}
	protected static void openGallery(CALLBACK<Bitmap> callback) {
		imageCallback = callback;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) ACTIVITY.context).startActivityForResult(intent, GET_CONTENT);
	}
	protected static void openGalleryUri(CALLBACK<Uri> callback) {
		uriCallback = callback;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity) ACTIVITY.context).startActivityForResult(intent, GET_CONTENT_PATH);
	}

	protected static void openMini(CALLBACK<Bitmap> callback) {
		imageCallback = callback;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* ������������������������������ */
		((Activity) ACTIVITY.context).startActivityForResult(intent, GET_MINI);
	}

	protected static void openUri(CALLBACK<Uri> callback) {
		uriCallback = callback;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* ������������������������������ */
		((Activity) ACTIVITY.context).startActivityForResult(intent, GET_URI);
	}
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
			+ "/account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_cache/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;
	private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IMAGE_CAPTURE: {
			try {
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				imageCallback.run(false, bitmap);
			}catch(Exception e){
				e.printStackTrace();
				DIALOG.toast("Take Phone Error!!");
			}
			/*Uri uri = Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME));
			String path = getPath(this,uri);
			if(path!=null){
					try {
						Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
						Log.e("onActivityResult: ", "onActivityResult: "+bitmap );
						imageCallback.run(false,bitmap);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
			}*/
			/*Bundle bundle = data.getExtras();
			if (bundle != null) {
				imageCallback.run(false, (Bitmap) bundle.get("data"));
			}
*/
			break;
		}
		case VIDEO_CAPTURE: {
			if (resultCode == RESULT_OK) {
				Uri uriVideo = data.getData();
				Cursor cursor = this.getContentResolver().query(uriVideo, null, null, null, null);
				if (cursor.moveToNext()) {
					/*
					 * _data������������������������
					 * ���_display_name������������
					 */
					String strVideoPath = cursor.getString(cursor.getColumnIndex("_data"));
					Toast.makeText(this, strVideoPath, Toast.LENGTH_SHORT).show();
					String videoPath = "111111113.3gp";
					Date now = new Date();
					SimpleDateFormat f2 = new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat f3 = new SimpleDateFormat("HHmmss");
					videoPath = f2.format(now) + "_" + f3.format(now) + ".3gp";
					File file = new File(strVideoPath);
					String pFile = file.getParentFile().getPath() + "/";
					String newPath = pFile + videoPath;
					file.renameTo(new File(newPath));
					DIALOG.toast("���������������������������" + newPath);
				}
			}
			break;
		}
		case GET_CONTENT: {
			if (data == null) {
				return;
			}
			ContentResolver resolver = getContentResolver();
			Uri originalUri = data.getData();
			Bitmap image;
			try {
				image = MediaStore.Images.Media.getBitmap(resolver, originalUri);
				imageCallback.run(false, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case GET_MINI: {
			if (data == null) {
				return;
			}
			ContentResolver resolver = getContentResolver();
			Uri originalUri = data.getData();
			Uri thumbURi = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					originalUri.getLastPathSegment());
			Bitmap image;
			try {
				image = MediaStore.Images.Media.getBitmap(resolver, thumbURi);
				imageCallback.run(false, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case GET_URI: {
			if (data == null) {
				return;
			}
			Uri originalUri = data.getData();
			uriCallback.run(false, originalUri);
			break;
		}
		case IMAGE_CAPTURE_PATH: {
			if (data == null) {
				return;
			}
			Uri originalUri = data.getData();
			uriCallback.run(false, originalUri);
			break;
		}
		case GET_CONTENT_PATH: {
			if (data == null) {
				return;
			}
			Uri originalUri = data.getData();
			uriCallback.run(false, originalUri);
			break;
		}
		default:
			break;
		}

	}
	/*
	 * @SuppressWarnings("resource") private static long getFileSize(File file)
	 * { long size = 0; try { if (file.exists()) { FileInputStream fis = null;
	 * fis = new FileInputStream(file); size = fis.available(); } else {
	 * file.createNewFile(); Log.e("������������������", "���������������!"); }
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * return size; }
	 */

	protected void fullscreen() {
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// getActionBar().hide();
		this.getActionBar().hide();
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);//���������������
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);//���������������
	}

	public boolean isAppOnForeground() {
		return isAppOnForeground(this);
	}

	public static boolean isAppOnForeground(Activity activity) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) activity.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = activity.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	@TargetApi(23)
	public static boolean isAllowPermission(String permission) {
		if (Build.VERSION.SDK_INT >= 23) {
			return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
		} else {
			return true;
		}

	}

	private PermissionCallback permissionRunnable;

	public interface PermissionCallback {
		void hasPermission();

		void noPermission();
	}

	/**
	 * Android M运行时权限请求封装
	 * 
	 * @param permissionDes
	 *            权限描述
	 * @param runnable
	 *            请求权限回调
	 * @param permissions
	 *            请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.
	 *            WRITE_CONTACTS
	 */
	public void performCodeWithPermission(String permissionDes, PermissionCallback runnable, String... permissions) {
		if (permissions == null || permissions.length == 0)
			return;
		// this.permissionrequestCode = requestCode;
		this.permissionRunnable = runnable;
		if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)) {
			if (permissionRunnable != null) {
				permissionRunnable.hasPermission();
				permissionRunnable = null;
			}
		} else {
			// permission has not been granted.
			requestPermission(permissionDes, permissionRequestCode, permissions);
		}

	}

	int permissionRequestCode = 100;

	@TargetApi(23)
	@SuppressLint("NewApi")
	private boolean checkPermissionGranted(String[] permissions) {
		boolean flag = true;
		for (String p : permissions) {
			if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	@SuppressLint("NewApi")
	private void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
		if (shouldShowRequestPermissionRationale(permissions)) {

			// 如果用户之前拒绝过此权限，再提示一次准备授权相关权限
			new AlertDialog.Builder(this).setTitle("提示").setMessage(permissionDes)
					.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							requestPermissions(permissions, requestCode);
						}
					}).show();

		} else {
			// Contact permissions have not been granted yet. Request them
			// directly.
			requestPermissions(permissions, requestCode);
		}
	}

	@SuppressLint("NewApi")
	private boolean shouldShowRequestPermissionRationale(String[] permissions) {
		boolean flag = false;
		for (String p : permissions) {
			if (shouldShowRequestPermissionRationale(p)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@SuppressLint("NewApi")
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == permissionRequestCode) {
			if (verifyPermissions(grantResults)) {
				if (permissionRunnable != null) {
					permissionRunnable.hasPermission();
					permissionRunnable = null;
				}
			} else {
				DIALOG.toast("暂无权限执行相关操作！");
				if (permissionRunnable != null) {
					permissionRunnable.noPermission();
					permissionRunnable = null;
				}
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public boolean verifyPermissions(int[] grantResults) {
		// At least one result must be checked.
		if (grantResults.length < 1) {
			return false;
		}

		// Verify that each required permission has been granted, otherwise
		// return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	protected void initNavigationBar(int navigationBar, int barTintColor) {
		_navigationBar = (UINavigationBar) this.findViewById(navigationBar);
		if (_navigationBar == null) {
			return;
		}
		android.app.ActionBar actionBar = this.getActionBar();
		if (this.getActionBar() != null) {
			actionBar.hide();
		}
		_navigationBar.setBackgroundColor(barTintColor);
		_navigationBar.title(this.getTitle().toString());
	}

	UINavigationBar _navigationBar;

	public UINavigationBar navigationBar() {
		return _navigationBar;
	}

	protected void initStatusBar(int barTintColor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			initStatusBar50(barTintColor);

		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			initStatusBar44(barTintColor);

		}
	}

	@TargetApi(23)
	void initStatusBar50(int barTintColor) {
		Window window = getWindow();
		// 取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		// 需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		// 设置状态栏颜色
		window.setStatusBarColor(barTintColor);
		ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
		View mChildView = mContentView.getChildAt(0);
		if (mChildView != null) {
			// 注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子
			// View . 预留出系统 View 的空间.
			ViewCompat.setFitsSystemWindows(mChildView, true);
		}
	}

	@TargetApi(19)
	void initStatusBar44(int barTintColor) {
		Window window = getWindow();
		ViewGroup mContentView = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);

		// First translucent status bar.
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		int statusBarHeight = getStatusBarHeight(this);

		View mChildView = mContentView.getChildAt(0);
		if (mChildView != null) {
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
			// 如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
			if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
				// 不预留系统空间
				ViewCompat.setFitsSystemWindows(mChildView, false);
				lp.topMargin += statusBarHeight;
				mChildView.setLayoutParams(lp);
			}
		}

		View statusBarView = mContentView.getChildAt(0);
		if (statusBarView != null && statusBarView.getLayoutParams() != null
				&& statusBarView.getLayoutParams().height == statusBarHeight) {
			// 避免重复调用时多次添加 View
			statusBarView.setBackgroundColor(barTintColor);
			return;
		}
		statusBarView = new View(this);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				statusBarHeight + 100);
		statusBarView.setBackgroundColor(barTintColor);
		// 向 ContentView 中添加假 View
		mContentView.addView(statusBarView, 0, lp);

	}

	public int getStatusBarHeight(Context activity) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}
}