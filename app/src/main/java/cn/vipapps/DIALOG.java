package cn.vipapps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

import cn.vipapps.android.ACTIVITY;
import cn.vipapps.ui.DatePickerView;
import cn.vipapps.ui.DateTimePickerView;
import cn.vipapps.ui.TimePickerView;
import cn.vipapps.ui.WhellPickerView;

import java.util.HashMap;
import java.util.Map;

import uc.ActionSheet;

public class DIALOG {
	private static String common_yes = "确定", common_no = "取消", common_no_input;

	private static ProgressDialog _loading;
	private static String str;

	private static Map<String, ProgressDialog> id = new HashMap<String, ProgressDialog>();

	/**
	 * loading 等待框
	 */
	public static void loading() {
		str = ACTIVITY.context.toString();
		LOG.d(ACTIVITY.context.toString());
		try {
			if (id.get(str) == null) {
				_loading = new ProgressDialog(ACTIVITY.context);
				// _loading.setMessage("正在处理……");
				id.put(str, _loading);
			}
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					id.get(str).show();
				}

			});
		} catch (Exception ex) {
			DIALOG.warning(true, ex);
		}
	}

	/**
	 * done 结束等待框
	 */
	public static void done() {
		if (id.get(str) == null) {
			return;
		}
		id.get(str).dismiss();
	}

	/**
	 * toast 提示框
	 */
	public static void toast(String message) {
		Toast.makeText(ACTIVITY.context, message, Toast.LENGTH_SHORT).show();

	}

	/**
	 * 消息框
	 * 
	 * @param message
	 *            :标题
	 * @param callback
	 *            :需要实现的方法
	 */
	public static void alert(final String message, final CALLBACK<Object> callback) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(ACTIVITY.context).setTitle(null).setMessage(message)
						.setPositiveButton(common_yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (callback != null) {
									callback.run(true, null);
								}

							}
						}).show();
			}

		});

	}

	/**
	 * 消息框
	 * 
	 * @param message
	 *            :标题
	 */
	public static void alert(String message) {
		alert(message, null);
	}

	/**
	 * 确认框
	 * 
	 * @param message
	 *            :标题
	 * @param callback
	 *            :需要实现的方法
	 */
	public static void confirm(String message, final CALLBACK<Object> callback) {
		new AlertDialog.Builder(ACTIVITY.context).setTitle(null).setMessage(message)
				.setPositiveButton(common_yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.run(false, null);
					}
				}).setNegativeButton(common_no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.run(true, null);
					}
				}).show().setCancelable(false);
	}

	/**
	 * 确认框
	 * 
	 * @param message
	 *            :标题
	 * @param callback
	 *            :需要实现的方法
	 * @param callback_no
	 *            :需要实现的方法
	 */
	public static void confirm(String message, final CALLBACK<Object> callback, final CALLBACK<Object> callback_no) {
		new AlertDialog.Builder(ACTIVITY.context).setTitle(null).setMessage(message)
				.setPositiveButton(common_yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback.run(false, null);
					}
				}).setNegativeButton(common_no, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						callback_no.run(true, null);
					}
				}).show().setCancelable(false);
	}

	@SuppressLint("HandlerLeak")
	public static void warning(boolean isSilence, Exception e) {
		e.printStackTrace();
		if (!isSilence) {
			return;
		}
		warning(isSilence, e.getMessage());
	}

	public static void warning(boolean isSilence, boolean isSystem, Exception e) {
		e.printStackTrace();
		if (!isSilence) {
			return;
		}
		warning(isSystem, e.getMessage());
	}

	public static void warning(boolean isSilence, String text) {
		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("message", isSilence ? "连接服务器异常，请检查网络。" : text);
		message.setData(bundle);
		if (!isSilence){
			new Handler(ACTIVITY.context.getMainLooper()) {
				@Override
				public void handleMessage(Message message) {
					Bundle bundle = message.getData();
					DIALOG.alert(bundle.getString("message"));
				}
			}.sendMessage(message);
		}
	}

	/**
	 * warning 错误警告框
	 */
	public static void warning(Exception e) {
		warning(false, e);
	}

	/*
	 * public static void choice(String[] items, final CALLBACK<Integer>
	 * callback) { new AlertDialog.Builder(ACTIVITY.context) .setItems(items,
	 * new DialogInterface.OnClickListener() { public void
	 * onClick(DialogInterface dialog, int which) { callback.run(false, which);
	 * dialog.dismiss(); } }).setNegativeButton(common_yes, null).show()
	 * .setCancelable(false); }
	 * 
	 * public static void singleChoice(String[] items, final CALLBACK<Integer>
	 * callback) { new AlertDialog.Builder(ACTIVITY.context)
	 * .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
	 * public void onClick(DialogInterface dialog, int which) {
	 * callback.run(false, which); dialog.dismiss(); }
	 * }).setNegativeButton(common_yes, null).show() .setCancelable(false); }
	 */

	/**
	 * input 输入框
	 * 
	 * @param title
	 *            :标题
	 * @param callback
	 *            :需要实现的方法
	 */
	public static void input(String title, final CALLBACK<String> callback) {
		final EditText inputServer = new EditText(ACTIVITY.context);
		new AlertDialog.Builder(ACTIVITY.context).setTitle(title).setView(inputServer)
				.setNegativeButton(common_no, null)
				.setPositiveButton(common_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String string = inputServer.getText().toString();
						if (string == "") {
							toast(common_no_input);
							return;
						}
						callback.run(false, string);
						dialog.dismiss();
					}
				}).show().setCancelable(false);
	}

	/**
	 * chooseWithTitles 列表选择框
	 * 
	 * @param context
	 *  provinces
	 *            列表数组
	 * @param callback
	 *            回调方法
	 */
	public static void chooseWithTitles(Context context, final CALLBACK<Integer> callback, String... titles) {
		ActionSheet.createBuilder(context, ((FragmentActivity) context).getSupportFragmentManager())
				.setCancelButtonTitle("取消").setOtherButtonTitles(titles).setCancelableOnTouchOutside(true)
				.setListener(new ActionSheet.ActionSheetListener() {

					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
						callback.run(true, -1);

					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						callback.run(false, index);

					}
				}).show();

	}

	/**
	 * datePickerView 日期选择框
	 * 
	 * @param context
	 * @param callback
	 *            回调方法
	 */
	public static void datePickerView(Context context, int color, CALLBACK<String> callback) {
		DatePickerView datePicker = new DatePickerView(context, color, callback);
		datePicker.show();
		datePicker.anim(datePicker);
	}

	/**
	 * dateTimePickerView 日期时间滚筒选择框
	 * 
	 * @param context
	 * @param callback
	 *            回调方法
	 */
	public static void dateTimePickerView(Context context, int color, CALLBACK<String> callback) {
		DateTimePickerView datePicker = new DateTimePickerView(context, color, callback);
		datePicker.show();
		datePicker.anim(datePicker);
	}
	/**
	 * timePickerView 日期滚筒选择框
	 * 
	 * @param context
	 * @param callback
	 *            回调方法
	 */
	public static void timePickerView(Context context, int color, CALLBACK<String> callback) {
		TimePickerView datePicker = new TimePickerView(context, color, callback);
		datePicker.show();
		datePicker.anim(datePicker);
	}

	/**
	 * whellPickerView 滚筒选择框
	 * 
	 * @param context
	 * @param callback
	 *            回调方法
	 */
	public static void pickerView(Context context, int color, Object[] obj, CALLBACK<Integer> callback) {
		WhellPickerView datePicker = new WhellPickerView(context, color, obj, callback);
		datePicker.show();
		datePicker.anim(datePicker);
	}

//	public static void photo(final CALLBACK<Bitmap> callback) {
//		final String[] titles = new String[] { "拍照", "相册" };
//		DIALOG.chooseWithTitles(ACTIVITY.context, new CALLBACK<Integer>() {
//
//			@Override
//			public void run(boolean isError, Integer result) {
//				if (isError) {
//					return;
//				}
//				switch (result) {
//				case 0:
//					CAMERA.openCamera(callback);
//					break;
//				case 1:
//					CAMERA.openGallery(callback);
//					break;
//				default:
//					break;
//				}
//			}
//		}, titles);
//	}
}
