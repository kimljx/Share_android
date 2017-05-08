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
import cn.vipapps.JSON;
import cn.vipapps.MESSAGE;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uc.ListRow;

public class RepairFormActivity extends PGACTIVITY {


    @BindView(R.id.repair_name)
    TextView repair_name;
    @BindView(R.id.nameID)
    EditText nameID;
    @BindView(R.id.Repair)
    EditText Repair;
    @BindView(R.id.scan)
    ImageView scan;

    String id;
    String spareId = "0";
    String qrcode;
    private static int spareRequestCode = 4;

    @OnClick(R.id.scan)
    public void scanImg() {
        new IntentIntegrator(this).initiateScan();
    }

    @BindView(R.id.spare)
    TextView spare;

    @BindView(R.id.spareRow)
    ListRow spareRow;

    @OnClick(R.id.spareRow)
    public void spareRow() {
        Intent intent = new Intent(RepairFormActivity.this, RepairSpareActivity.class);
        startActivityForResult(intent, spareRequestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_form);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == spareRequestCode) {
            String result = data.getStringExtra("result");
            JSONObject asset = JSON.parse(result);
            spare.setText(asset.optString("name"));
            spareId = asset.optString("id");

        } else {
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
//                    Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
                    DIALOG.alert("扫描二维码失败！");
                } else {
                    Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                    // ScanResult 为 获取到的字符串
                    qrcode = intentResult.getContents();

                    Log.e("onActivityResult: ",qrcode );
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("故障处理");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                String content = Repair.getText().toString();
                String assets_id = spareId;
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
//                RestBLL.mod_breakdown(type,ids, content,assets_id,1, new CALLBACK<JSONObject>() {
//                    @Override
//                    public void run(boolean isError, JSONObject result) {
//                        DIALOG.alert("提交成功", new CALLBACK<Object>() {
//                            @Override
//                            public void run(boolean isError, Object result) {
//                                MESSAGE.send(Common.MSG_FAULTREFESH,null);
//                                finish();
//                            }
//                        });
//                    }
//                });
            }
        });
    }

}
