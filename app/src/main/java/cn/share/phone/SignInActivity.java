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
import cn.share.Location;
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

public class SignInActivity extends PGACTIVITY {


    @BindView(R.id.nameID)
    EditText nameID;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.equipment)
    TextView equipment;
    String id;
    private String qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
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
//                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
                DIALOG.alert("扫描二维码失败！");
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                qrcode = intentResult.getContents();
//                equipment.setText(id);

                Log.e("onActivityResult: ", qrcode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("上岗");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
//                RestBLL.set_work_record(qrcode, id, new CALLBACK<JSONObject>() {
//                    @Override
//                    public void run(boolean isError, JSONObject result) {
//                        DIALOG.alert("上岗成功！", new CALLBACK<Object>() {
//                            @Override
//                            public void run(boolean isError, Object result) {
//                                Location.getInstance().startPosition(new CALLBACK<String>() {
//                                    @Override
//                                    public void run(boolean isError, String result) {
//
//                                        RestBLL.set_staff_position(result, new CALLBACK<JSONObject>() {
//                                            @Override
//                                            public void run(boolean isError, JSONObject result) {
////                                                DIALOG.alert(result+"   "+new Date());
//                                                SignInActivity.this.finish();
//                                            }
//                                        });
//                                    }
//                                });
//
//                            }
//                        });
//                    }
//                });
            }
        });
    }



}
