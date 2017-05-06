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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;
import cn.vipapps.ui.picker.OnWheelScrollListener;
import cn.vipapps.ui.picker.adapter.ArrayWheelAdapter;
import cn.vipapps.ui.picker.adapter.NumericWheelAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerView extends Dialog {

    private UIPickerView year;
    private UIPickerView month;
    private UIPickerView day;
    private UIPickerView min;
    private UIPickerView sec;
//	private UIPickerView wek;

    private LinearLayout _linearLayout_1;
    private LinearLayout _linearLayout_2;
    private LinearLayout _linearLayout_3;
    private Button button_ok, button_con;

    private int mMonth = 0;

    private Context context;
    private CALLBACK<String> callback;
    private int color;

    //年的间隔
    static int midYear  = 20; 
    LinearLayout ll;

    boolean isMonthSetted = false, isDaySetted = false;

    public DateTimePickerView(Context context, int color,
                              CALLBACK<String> callback) {
        super(context, android.R.style.Theme_Panel);
        this.context = context;
        this.callback = callback;
        this.color = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建事件
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
        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

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
                DateTimePickerView.this.dismiss();
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    Calendar c = Calendar.getInstance();
                    int norYear = c.get(Calendar.YEAR);
                    String time = (year.getCurrentItem() + norYear - midYear) + "-"
                            + (month.getCurrentItem() + 1) + "-"
                            + (day.getCurrentItem() + 1) + " "
                            + +(min.getCurrentItem()) + ":"
                            + (sec.getCurrentItem());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm");
                    Date date = simpleDateFormat.parse(time);
                    // SimpleDateFormat的format(Date date)方法将Date转换为String
                    String formattedTime = simpleDateFormat.format(date);

                    callback.run(false, formattedTime);
                    DateTimePickerView.this.dismiss();
                } catch (ParseException e) {
                    // TODO 自动生成的 catch 块
                    e.printStackTrace();
                }
            }
        });
    }

    private void _init() {

        SimpleDateFormat mDateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat sDateFormat = new SimpleDateFormat("mm");

        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
        int norMonth = c.get(Calendar.MONTH);
        int norDate = c.get(Calendar.DATE);
        String norMin = mDateFormat.format(new Date());
        String norSec = sDateFormat.format(new Date());

        int curMonth = norMonth + 1;

        //初始化日期
        year = new UIPickerView(context);
        month = new UIPickerView(context);
        day = new UIPickerView(context);
        min = new UIPickerView(context);
        sec = new UIPickerView(context);
//		wek = new UIPickerView(context);

        LinearLayout.LayoutParams linearParams_3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);//(android.widget.LinearLayout.LayoutParams) _linearLayout_3.getLayoutParams();
        linearParams_3.weight = 1;

        //添加视图
//		_linearLayout_3.addView(wek, linearParams_3);
        _linearLayout_3.addView(year, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _linearLayout_3.addView(month, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _linearLayout_3.addView(day, linearParams_3);
        _linearLayout_3.addView(min, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _linearLayout_3.addView(sec, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//		NumericWheelAdapter numericWheelAdapter5 = new NumericWheelAdapter(
//				context, 1, 7);
//		numericWheelAdapter5.setFrontLabel("星期");
//		wek.setViewAdapter(numericWheelAdapter5);
//		wek.setCyclic(true);
//		wek.addScrollingListener(scrollListener);

        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
                context, norYear - midYear, norYear + midYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
                context, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        initDay(norYear, curMonth);
        day.setCyclic(true);

        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(
                context, 0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        min.setViewAdapter(numericWheelAdapter3);
        min.setCyclic(true);
        min.addScrollingListener(scrollListener);

        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(
                context, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        sec.setViewAdapter(numericWheelAdapter4);
        sec.setCyclic(true);
        sec.addScrollingListener(scrollListener);

//		wek.setVisibleItems(7);
        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);
        min.setVisibleItems(7);
        sec.setVisibleItems(7);


        year.setCurrentItem(midYear);
        month.setCurrentItem(norMonth);
        day.setCurrentItem(norDate - 1);
        min.setCurrentItem(Integer.parseInt(norMin));
        sec.setCurrentItem(Integer.parseInt(norSec));
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(UIPickerView wheel) {

        }

        @Override
        public void onScrollingFinished(UIPickerView wheel) {
            Calendar c = Calendar.getInstance();
//            int i = year.getCurrentItem();
//            int j = c.get(Calendar.YEAR);
            int n_year = year.getCurrentItem() + c.get(Calendar.YEAR) - midYear;
            int n_month = month.getCurrentItem() + 1;

            initDay(n_year, n_month);

        }
    };

    /**
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

    /**
     */
    private void initDay(int arg1, int arg2) {
        /*NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
				context, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("日");
		day.setViewAdapter(numericWheelAdapter);*/
        int days = getDay(arg1, arg2);
        ArrayList items = new ArrayList();
        for (int d = 1; d <= days; d++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, arg1);
            cal.set(Calendar.MONTH, arg2 - 1);
            cal.set(Calendar.DAY_OF_MONTH, d);
            String item = new SimpleDateFormat("dd日  EEEE").format(cal.getTime());
            items.add(item);
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<Object>(
                context, items.toArray());
        day.setViewAdapter(arrayWheelAdapter);
    }

    @SuppressWarnings("deprecation")
    public void anim(DateTimePickerView whell) {
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

    private void getButton(Button button) {
        button.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        button.setHeight(dip2px(context, 50));
        button.setPadding(dip2px(context, 11), 0, dip2px(context, 11), 0);
        button.setTextColor(Color.WHITE);
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
        drawable.addState(new int[]{-android.R.attr.state_pressed,},
                drawable2);
        drawable.addState(new int[]{android.R.attr.state_pressed}, drawable1);
        return drawable;
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
