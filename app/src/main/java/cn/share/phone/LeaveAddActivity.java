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
import cn.vipapps.MESSAGE;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uc.ListRow;

public class LeaveAddActivity extends PGACTIVITY {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.startRow)
    ListRow startRow;
    @BindView(R.id.endRow)
    ListRow endRow;
    @BindView(R.id.typeRow)
    ListRow typeRow;
    @BindView(R.id.start)
    TextView start;
    @BindView(R.id.end)
    TextView end;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.content)
    EditText content;
    @OnClick(R.id.startRow)
    void startRow() {
        DIALOG.dateTimePickerView(this, getResources().getColor(R.color.V), new CALLBACK<String>() {
            @Override
            public void run(boolean isError, String result) {
                start.setText(result);
            }
        });
    }

    @OnClick(R.id.endRow)
    void endRow() {

        DIALOG.dateTimePickerView(this, getResources().getColor(R.color.V), new CALLBACK<String>() {
            @Override
            public void run(boolean isError, String result) {
                end.setText(result);
            }
        });
    }
    @OnClick(R.id.typeRow)
    void typeRow() {
        DIALOG.pickerView(context,getResources().getColor(R.color.V),typeChoose,new CALLBACK<Integer>(){

            @Override
            public void run(boolean isError, Integer result) {
                type.setText(Common.leaveType(String.valueOf(result+1)));
                typeId = result+1;
            }
        });
    }

    String[] typeChoose= new String[]{
            "病假","事假","婚假","丧假","产假","年假","工伤假","公出假"
    };
    int typeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_add);
        ButterKnife.bind(this);
        JSONObject object = CONFIG.getJSON(Common.CONFIG_USER);
        name.setText(object.optString("name"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("请假申请");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                String contents = content.getText().toString();
                String starts = start.getText().toString();
                String ends = end.getText().toString();
                if (Common.compareStringDate(starts, ends)) {
                    DIALOG.alert("结束时间小于开始时间！");
                    return;
                }
//                RestBLL.add_leaves(contents, starts, ends,typeId, new CALLBACK<JSONObject>() {
//                    @Override
//                    public void run(boolean isError, JSONObject result) {
//                        DIALOG.confirm("提交成功！", new CALLBACK<Object>() {
//                            @Override
//                            public void run(boolean isError, Object result) {
//                                MESSAGE.send(Common.MSG_LEAVEADDREFESH,null);
//                                finish();
//                            }
//                        });
//                    }
//                });
            }
        });
    }



}
