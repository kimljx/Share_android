package cn.share.phone;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeaveDetailActivity extends PGACTIVITY {



    @BindView(R.id.name)TextView name;
    @BindView(R.id.time)TextView time;
    @BindView(R.id.start)TextView start;
    @BindView(R.id.end)TextView end;
    @BindView(R.id.type)TextView type;
    @BindView(R.id.approver)TextView approver;
    @BindView(R.id.remark)EditText remark;
    @BindView(R.id.agree)TextView agree;
    @OnClick(R.id.agree)
    void agree(){
        status = 1;
        commit();
    }
    @BindView(R.id.reject)TextView reject;
    @OnClick(R.id.reject)
    void reject(){
        status = 2;
        commit();
    }
    String leaveId;
    String remarks;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval);
        ButterKnife.bind(this);
        String string = getIntent().getStringExtra("leave");
        JSONObject leave = JSON.parse(string);
        if(leave == null){
            return;
        }
        leaveId = leave.optString("id");
        name.setText(leave.optString("account"));
        time.setText(leave.optString("content"));
        start.setText(leave.optString("start"));
        end.setText(leave.optString("end"));
        type.setText(Common.leaveType(leave.optString("type")));
        JSONObject user = CONFIG.getJSON(Common.CONFIG_USER);
        approver.setText(user.optString("name"));
        remarks = remark.getText().toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("请假审批");
    }

    void commit(){
        DIALOG.confirm("确认提交？", new CALLBACK<Object>() {
            @Override
            public void run(boolean isError, Object result) {
                RestBLL.check_leaves(leaveId, remarks, status, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {

                        finish();

                    }
                });
            }
        }, new CALLBACK<Object>() {
            @Override
            public void run(boolean isError, Object result) {

            }
        });

    }

    @Override
    protected void onStop() {
        MESSAGE.send(Common.MSG_LEAVELIST,null);
        super.onStop();

    }

    @Override
    public void finish() {
        super.finish();
    }
}
