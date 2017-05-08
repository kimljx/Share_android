package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentActivity extends PGACTIVITY {

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
        ButterKnife.bind(CommentActivity.this);

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
//                RestBLL.add_breakdown(type,ids, content, new CALLBACK<JSONObject>() {
//                    @Override
//                    public void run(boolean isError, JSONObject result) {
//                        DIALOG.alert("提交成功", new CALLBACK<Object>() {
//                            @Override
//                            public void run(boolean isError, Object result) {
//                                MESSAGE.send(Common.MSG_FAULTREFESH, null);
//                                finish();
//                            }
//                        });
//                    }
//                });
            }
        });
    }


}
