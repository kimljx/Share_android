package cn.share.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;
import cn.vipapps.MESSAGE;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FaultFormActivity extends PGACTIVITY {

    @BindView(R.id.fault_name)
    TextView fault_name;
    @BindView(R.id.nameID)
    EditText nameID;
    @BindView(R.id.fault)
    EditText fault;
    @BindView(R.id.scan)
    ImageView scan;

    String id;
    String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_form);
        ButterKnife.bind(FaultFormActivity.this);
        nameID.setOnKeyListener(onKey);
        nameID.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            id = nameID.getText().toString();
            getNameWithId();
        }
    };


    @OnClick(R.id.scan)
    public void scanImg() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
//                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
                DIALOG.alert("扫描二维码失败！");
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                qrcode = intentResult.getContents();
                getNameWithCode();
                Log.e("onActivityResult: ", qrcode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("故障报修");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                String content = fault.getText().toString();
                final String ids;
                final String type;
                if (qrcode != null){
                    ids = qrcode;
                    type = "1";
                }else if (id != null){
                    ids = id ;
                    type = "2";
                }else {
                    type = "0";
                    ids = null;
                }
                Log.e("id: ", ids);
                RestBLL.add_breakdown(type,ids, content, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        DIALOG.alert("提交成功", new CALLBACK<Object>() {
                            @Override
                            public void run(boolean isError, Object result) {
                                MESSAGE.send(Common.MSG_FAULTREFESH, null);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }


    void getNameWithCode() {
        RestBLL.get_device_info(qrcode, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                String name = result.optJSONObject("device_info").optString("name");
                fault_name.setText(name);
                nameID.setText(result.optJSONObject("device_info").optString("id"));

            }
        });
    }

    void getNameWithId() {
        RestBLL.get_device_info2(id, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                String name = result.optJSONObject("device_info").optString("name");
                fault_name.setText(name);
            }
        });
    }

    View.OnKeyListener onKey = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int i, KeyEvent keyEvent) {

            if (KeyEvent.KEYCODE_ENTER == i) {
                id = nameID.getText().toString();
                getNameWithId();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                return true;

            }
            return false;
        }

    };
}
