package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddShareActivity extends PGACTIVITY {

//    @BindView(R.id.start)TextView start;
//    @BindView(R.id.end)TextView end;
//
//    @OnClick(R.id.start)
//    void startClick(){
//        DIALOG.dateTimePickerView(this, getResources().getColor(R.color.V), new CALLBACK<String>() {
//            @Override
//            public void run(boolean isError, String result) {
//                if (isError){
//
//                }
//                start.setText(result);
//            }
//        });
//    }
//    @OnClick(R.id.end)
//    void endClick(){
//        DIALOG.dateTimePickerView(this, getResources().getColor(R.color.V), new CALLBACK<String>() {
//            @Override
//            public void run(boolean isError, String result) {
//                if (isError){
//
//                }
//                end.setText(result);
//            }
//        });
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_staff_trace_time);
        startActivity(new Intent(AddShareActivity.this,ShareActivity.class));
//        ButterKnife.bind(this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        this.navigationBar().title("选择轨迹时间");
//        this.navigationBar().rightNavButton("提交", new CALLBACK() {
//            @Override
//            public void run(boolean isError, Object result) {
//                if (Common.compareStringDate(start.getText().toString(), end.getText().toString())) {
//                    DIALOG.alert("结束时间小于开始时间！");
//                    return;
//                }
//                Intent intent = new Intent();
//                intent.putExtra("start",start.getText().toString());
//                intent.putExtra("end",end.getText().toString());
//                AddShareActivity.this.setResult(2,intent);
//                finish();
//            }
//        });
//    }
}
