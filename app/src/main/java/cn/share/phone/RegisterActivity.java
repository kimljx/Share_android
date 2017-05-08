package cn.share.phone;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterActivity extends PGACTIVITY {

    ImageView avatar;
    LinearLayout shiftsLayout;
    TextView name,name2,phone,duty,
//            shifts,report_should,
            report_real,up_time,down_time;
    private String staffId;


    void init(){
        shiftsLayout = (LinearLayout)findViewById(R.id.shiftsLayout);
        avatar = (ImageView)findViewById(R.id.avatar);
        name = (TextView)findViewById(R.id.name);
        name2 = (TextView)findViewById(R.id.name2);
        phone = (TextView)findViewById(R.id.phone);
//        shifts = (TextView)findViewById(R.id.shifts);
        duty = (TextView)findViewById(R.id.duty);
//        report_should = (TextView)findViewById(R.id.report_should);
        report_real = (TextView)findViewById(R.id.report_real);
        up_time = (TextView)findViewById(R.id.up_time);
        down_time = (TextView)findViewById(R.id.down_time);
        staffId = getIntent().getStringExtra("staffId");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);
        init();
        reloadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("员工详情");
    }

    void reloadData(){
//        RestBLL.get_staff_info(staffId, new CALLBACK<JSONObject>() {
//            @Override
//            public void run(boolean isError, JSONObject result) {
//                Log.e("run: get_staff_info", result+"");
//                JSONObject work_info = result.optJSONObject("work_info");
//                JSONArray works = work_info.optJSONArray("work");
//                JSONObject work = new JSONObject();
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ACTIVITY.dp2px(25));
//                if (works!=null) {
//                    for (int i = 0;i<works.length();i++) {
//                        TextView tv = new TextView(RegisterActivity.this);
//                        tv.setLayoutParams(lp);
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            tv.setTextAppearance(R.style.H2);
////                        }
//                        tv.setTextColor(getResources().getColor(R.color.black));
//                        tv.setTextSize(ACTIVITY.dp2px(5));
//                        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                        work = works.optJSONObject(i);
//                        tv.setText(work.optString("name"));
//                        shiftsLayout.addView(tv);
//                    }
//                }else {
//                    shiftsLayout.setLayoutParams(new RelativeLayout.LayoutParams(
//                            RelativeLayout.LayoutParams.MATCH_PARENT,ACTIVITY.dp2px(25)));
//                }
//                Log.e("run: get_staff_info", work_info+"");
//                String userId = result.optString("id");
//                name.setText(result.optString("name"));
//                name2.setText(result.optString("account"));
//                phone.setText(result.optString("phone"));
//                //班次
////                if (works !=null){
////                    shifts.setText(works.optString("name"));
////                }if
////                if (work.optString("name")!=null){
////                    shifts.setText(work.optString("name"));
////                }
//
//                duty.setText(work.optString("name"));
//                //
//                String re = work_info.optString("report_count");
//                String sta = work_info.optString("work_start");
//                String en = work_info.optString("work_end");
//                report_real.setText(re+"次");
//                up_time.setText(sta);
//                down_time.setText(en);
//                String url = String.format("user_%s.png",userId);
//                PGAJAX.getImage(url,true, new CALLBACK<Bitmap>() {
//                    @Override
//                    public void run(boolean isError, Bitmap result) {
//                        if (isError){
//                            avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
//                            return;
//                        }
//                        if (result == null){
//                            avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
//                            return;
//                        }
//                        avatar.setImageBitmap(result);
//                    }
//                });
//            }
//        });
    }
}
