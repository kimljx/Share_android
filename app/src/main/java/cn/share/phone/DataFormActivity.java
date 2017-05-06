package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uc.ListRow;

public class DataFormActivity extends PGACTIVITY {

    final static int numberRequestCode = 1;
    final static int timeRequestCode = 2;
    @BindView(R.id.job_name)TextView job_name;
    @BindView(R.id.time)TextView time;
    @BindView(R.id.number)TextView number;
    @BindView(R.id.timeRow)
    ListRow timeRow;
    @OnClick(R.id.timeRow)
    public void timeRow(){
        Intent intent = new Intent(DataFormActivity.this,DataTimeListActivity.class);
        startActivityForResult(intent,timeRequestCode);
    }
    @BindView(R.id.numberRow)
    ListRow numberRow;
    @OnClick(R.id.numberRow)
    public void numberRow(){
        Intent intent = new Intent(DataFormActivity.this,DataPeopleListActivity.class);
        startActivityForResult(intent,numberRequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form);
        ButterKnife.bind(this);
        job_name.setText(CONFIG.getString(Common.CONFIG_EQUIPMENT));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("数据上报");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                String time_line = time.getText().toString();
                String count = number.getText().toString();
                RestBLL.device_data_upload(time_line, count, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        DIALOG.alert("上报成功！", new CALLBACK<Object>() {
                            @Override
                            public void run(boolean isError, Object result) {
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }else {
            String result = data.getStringExtra("result");

            switch (requestCode) {
                case timeRequestCode:
                    time.setText(result);
                    break;
                case numberRequestCode:
                    number.setText(result);
                    break;
                default:
                    break;
            }
        }
    }
}
