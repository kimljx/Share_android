package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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

public class GuestLoginActivity extends PGACTIVITY {

    EditText  etUserName, etPassword;
    TextView tvlizc;//立即注册
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_guest_login);
        etUserName = (EditText) findViewById(R.id.usernameEditText);
        etPassword = (EditText) findViewById(R.id.passwordEditText);
        button = (Button)findViewById(R.id.btn_guestlogin_login) ;
        tvlizc=(TextView)findViewById(R.id.txt_lijizc) ;
        button.setEnabled(true);
        //

        isLogin();
        tvlizc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GuestLoginActivity.this, GuestRegisterActivity.class));//跳转到注册界面
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        button.setEnabled(true);
                    }
                }, 2000);
                final String name = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                Log.e("onClick: ", name+password);
                RestBLL.login(name, password, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError) {
                            return;
                        }
                        DIALOG.toast("登录成功！");
                        CONFIG.set("USERNAME",name);
                        CONFIG.set("PASSWORD",password);
                        Log.e("run: ", CONFIG.getString(Common.CONFIG_TOKEN));
                        MESSAGE.send(Common.MSG_HASLOGIN,null);
                        finish();
                    }
                });

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("登录");
        button.setEnabled(true);
    }

    void isLogin(){
        String token = CONFIG.getString(Common.CONFIG_TOKEN);
        if (token != null){
            String cName = (String) CONFIG.get("COMNAME");
            String uName = (String)CONFIG.get("USERNAME");
            String ePassword = (String)CONFIG.get("PASSWORD");
            etUserName.setText(uName);
            etPassword.setText(ePassword);
        }else {

        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//            return true;
//        }
//        return false;
//    }
}
