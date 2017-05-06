package cn.vipapps.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;
import cn.vipapps.ui.picker.adapter.ArrayWheelAdapter;

public class WhellPickerView extends Dialog {

	private LinearLayout _linearLayout_1;
	private LinearLayout _linearLayout_2;
	private Button button_ok, button_con;
	private UIPickerView picker;

	private Context context;
	private int color;

	private CALLBACK<Integer> callback;
	private Object[] obj;

	public WhellPickerView(Context context, int color, Object[] obj,
						   CALLBACK<Integer> callback) {
		super(context, android.R.style.Theme_Panel);
		this.context = context;
		this.callback = callback;
		this.obj = obj;
		this.color = color;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {//创建事件
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);

		RelativeLayout relative = new RelativeLayout(context);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
		_linearLayout_1 = new LinearLayout(context);
		_linearLayout_1.setOrientation(LinearLayout.VERTICAL);
		_linearLayout_2 = new LinearLayout(context);
		_linearLayout_2.setOrientation(LinearLayout.HORIZONTAL);
		button_ok = new Button(context);
		button_con = new Button(context);
		picker = new UIPickerView(context);
		init();
		relative.setBackgroundColor(Color.argb(88, 00, 00, 00));
		relative.addView(_linearLayout_1, params);
		setContentView(relative);
		relative.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void init() {//初始化
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		_linearLayout_1.setBackgroundColor(Color.WHITE);
		_linearLayout_2.setLayoutParams(linearParams);
		LinearLayout.LayoutParams linearParams_2 = (LinearLayout.LayoutParams) _linearLayout_2
				.getLayoutParams();
		linearParams_2.weight = 1;
		button_ok.setLayoutParams(linearParams_2);
		button_con.setLayoutParams(linearParams_2);
		button_ok.setText("确认");
		button_ok.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
		button_con.setText("取消");
		button_con.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
		getButton(button_ok);
		getButton(button_con);
		_linearLayout_1.addView(_linearLayout_2);
		_linearLayout_2.addView(button_con);
		_linearLayout_2.addView(button_ok);

		ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<Object>(
				context, obj);
		picker.setLayoutParams(linearParams);
		picker.setViewAdapter(arrayWheelAdapter);
		picker.setVisibleItems(7);// 设置显示行数
		_linearLayout_1.addView(picker);

		button_con.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				WhellPickerView.this.dismiss();
			}
		});

		button_ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				callback.run(false, picker.getCurrentItem());
				WhellPickerView.this.dismiss();
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void anim(WhellPickerView whell) {
		WindowManager windowManager = ((Activity) ACTIVITY.context)
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = whell.getWindow().getAttributes();
		lp.width = display.getWidth(); // 设置宽度
		whell.getWindow().setAttributes(lp);
//		whell.getWindow().setWindowAnimations(
//				MResource.getIdByName(
//						((Activity) ACTIVITY.context).getApplication(),
//						"style", "dialogWindowAnim"));
	}

	@SuppressWarnings("deprecation")
	private void getButton(Button button) {
		button.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		button.setHeight(dip2px(context, 50));
		button.setPadding(dip2px(context, 11), 0, dip2px(context, 11), 0);
		//button.setTextColor(Color.WHITE);
//		button.setTextColor(this.getContext().getResources().getColor(R.color.V));
		button.setBackgroundColor(color);
		button.setTextSize(18);
	}

	@SuppressWarnings("unused")
	private Drawable shape() {
		GradientDrawable dr = new GradientDrawable();
		dr.setColor(Color.parseColor("#DFDFDF"));
		GradientDrawable dr1 = new GradientDrawable();
		dr1.setColor(Color.parseColor("#FFFFFF"));
		Drawable drawable1 = dr;
		Drawable drawable2 = dr1;
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { -android.R.attr.state_pressed, },
				drawable2);
		drawable.addState(new int[] { android.R.attr.state_pressed }, drawable1);
		return drawable;
	}

	private static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
