package cn.share.phone;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffLeaveActivity extends PGACTIVITY {

    @BindView(R.id.name)TextView name;
    @BindView(R.id.time)TextView time;
    @BindView(R.id.start)TextView start;
    @BindView(R.id.end)TextView end;
    @BindView(R.id.type)TextView type;
    @BindView(R.id.remarks)TextView remarks;
    @BindView(R.id.status)TextView status;
    @BindView(R.id.approver)TextView approver;
    @BindView(R.id.approval_time)TextView approval_time;
    @BindView(R.id.approval_adj)TextView approval_adj;
    String leaveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detail);
        ButterKnife.bind(this);
        String string = getIntent().getStringExtra("leave");
        JSONObject leave = JSON.parse(string);
        leaveId = leave.optString("id");
        reloadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("请假详情");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MESSAGE.send(Common.MSG_LEAVELIST,null);
    }

    void reloadData(){
        RestBLL.get_leaves_info(leaveId, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject leave) {
                Log.e("run: leave",leave+"" );
                name.setText(leave.optString("account"));
                time.setText(leave.optString("create_time"));
                start.setText(leave.optString("start"));
                end.setText(leave.optString("end"));
                type.setText(Common.leaveType(leave.optString("type")));
                status.setText(Common.approvalStatus(leave.optString("status")));
                approver.setText(leave.optString("check_account"));
                approval_time.setText(leave.optString("check_time"));
                approval_adj.setText(leave.optString("remark"));
                remarks.setText(leave.optString("content"));
            }
        });
    }

}
