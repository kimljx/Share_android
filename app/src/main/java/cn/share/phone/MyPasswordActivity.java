package cn.share.phone;

import android.os.Bundle;
import android.widget.EditText;

import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPasswordActivity extends PGACTIVITY {

    @BindView(R.id.mpw_old)EditText mpw_old;
    @BindView(R.id.mpw_new)EditText mpw_new;
    @BindView(R.id.mpw_again)EditText mpw_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_password);
        ButterKnife.bind(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("修改密码");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                if (isError){
                    return;
                }
                DIALOG.alert("修改密码", new CALLBACK<Object>() {
                    @Override
                    public void run(boolean isError, Object result) {
                        String old = mpw_old.getText().toString();
                        String news = mpw_new.getText().toString();
                        String again = mpw_again.getText().toString();
                        RestBLL.mod_user_password(old, news, again, new CALLBACK<JSONObject>() {
                            @Override
                            public void run(boolean isError, JSONObject result) {
                                if (isError){
                                    return;
                                }
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
