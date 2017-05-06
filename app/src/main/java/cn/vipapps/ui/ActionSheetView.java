package cn.vipapps.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import cn.vipapps.CALLBACK;

public class ActionSheetView {

	public static void showListDialog(final Context context,
									  final String[] titles, final CALLBACK<Integer> callback) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setItems(titles, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				callback.run(false, which);
				// final AlertDialog ad = new AlertDialog.Builder(context)
				// .setMessage("你选择的是：" + which + ": " + provinces[which])
				// .show();
				// Handler handler = new Handler();
				// Runnable runnable = new Runnable() {
				// @Override
				// public void run() {
				// ad.dismiss();
				// }
				// };
				// handler.postDelayed(runnable, 5 * 1000);
			}
		});
		builder.create().show();
	}
}
