package cn.vipapps.ui;

import android.annotation.SuppressLint;
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
import cn.vipapps.ui.picker.OnWheelScrollListener;
import cn.vipapps.ui.picker.adapter.NumericWheelAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerView extends Dialog {

	private LinearLayout _linearLayout_1;
	private LinearLayout _linearLayout_2;
	private LinearLayout _linearLayout_3;
	private Button button_ok, button_con;
	private UIPickerView year;
	private UIPickerView month;
	private UIPickerView day;

	private int mMonth = 0;

	private Context context;
	private int color;

	private CALLBACK<String> callback;

	public DatePickerView(Context context, int color, CALLBACK<String> callback) {
		super(context, android.R.style.Theme_Panel);
		this.context = context;
		this.callback = callback;
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
		_linearLayout_3 = new LinearLayout(context);
		_linearLayout_3.setOrientation(LinearLayout.HORIZONTAL);
		button_ok = new Button(context);
		button_con = new Button(context);
		init();
		_init();
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

	@SuppressLint("RtlHardcoded")
	private void init() {//初始化
		LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		_linearLayout_1.setBackgroundColor(Color.WHITE);
		_linearLayout_2.setLayoutParams(linearParams);
		_linearLayout_3.setLayoutParams(linearParams);
		LinearLayout.LayoutParams linearParams_2 = (LinearLayout.LayoutParams) _linearLayout_2
				.getLayoutParams();
		linearParams_2.weight = 1;
		button_ok.setLayoutParams(linearParams_2);
		button_con.setLayoutParams(linearParams_2);
		button_ok.setText("确认");
		button_ok.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		button_con.setText("取消");
		button_con.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		getButton(button_ok);
		getButton(button_con);
		_linearLayout_1.addView(_linearLayout_2);
		_linearLayout_1.addView(_linearLayout_3);
		_linearLayout_2.addView(button_con);
		_linearLayout_2.addView(button_ok);

		button_con.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				DatePickerView.this.dismiss();
			}
		});

		button_ok.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			public void onClick(View arg0) {

				try {
					String time = (year.getCurrentItem() + 1950) + "-"
							+ (month.getCurrentItem() + 1) + "-"
							+ (day.getCurrentItem() + 1);
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd");
					Date date = simpleDateFormat.parse(time);
					// SimpleDateFormat的format(Date date)方法将Date转换为String
					String formattedTime = simpleDateFormat.format(date);
					callback.run(false, formattedTime);
					DatePickerView.this.dismiss();
				} catch (ParseException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			}
		});
	}

	private void _init() {

		int curMonth = mMonth + 1;

		year = new UIPickerView(context);
		month = new UIPickerView(context);
		day = new UIPickerView(context);

		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		int norMonth = c.get(Calendar.MONTH);
		int norDate = c.get(Calendar.DATE);

		LinearLayout.LayoutParams linearParams_3 = (LinearLayout.LayoutParams) _linearLayout_3
				.getLayoutParams();
		linearParams_3.weight = 1;
		_linearLayout_3.addView(year, linearParams_3);
		_linearLayout_3.addView(month, linearParams_3);
		_linearLayout_3.addView(day, linearParams_3);
		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				context, 1950, 2030);
		numericWheelAdapter1.setLabel("年");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				context, 1, 12, "%02d");
		numericWheelAdapter2.setLabel("月");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		initDay(norYear, curMonth);
		day.setCyclic(true);

		year.setVisibleItems(7);// 设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);

		year.setCurrentItem(norYear - 1950);
		month.setCurrentItem(norMonth);
		day.setCurrentItem(norDate - 1);
	}

	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
				context, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(UIPickerView wheel) {

		}

		@Override
		public void onScrollingFinished(UIPickerView wheel) {
			int n_year = year.getCurrentItem() + 1950;// 年
			int n_month = month.getCurrentItem() + 1;// 月
			initDay(n_year, n_month);
		}
	};

	@SuppressWarnings("deprecation")
	public void anim(DatePickerView whell) {
		WindowManager windowManager = ((Activity) ACTIVITY.context)
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = whell.getWindow().getAttributes();
		lp.width = display.getWidth(); // 设置宽度
		whell.getWindow().setAttributes(lp);
		// whell.getWindow().setWindowAnimations(
		// MResource.getIdByName(
		// ((Activity) ACTIVITY.context).getApplication(),
		// "style", "dialogWindowAnim"));
	}

	// public TranslateAnimation translate_in() {
	// TranslateAnimation tran = new TranslateAnimation(0, 0, 480, 0);
	// tran.setDuration(250);
	// return tran;
	// }
	//
	// public TranslateAnimation translate_on() {
	// TranslateAnimation tran = new TranslateAnimation(0, 0, 0, 480);
	// tran.setDuration(250);
	// return tran;
	// }

	private void getButton(Button button) {
		button.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		button.setHeight(dip2px(context, 50));
		button.setTextColor(Color.WHITE);
		button.setPadding(dip2px(context, 11), 0, dip2px(context, 11), 0);
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
