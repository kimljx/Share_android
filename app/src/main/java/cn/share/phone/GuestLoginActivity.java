package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;

import org.json.JSONObject;

public class GuestLoginActivity extends PGACTIVITY {

    EditText  etUserName, etPassword;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_guest_login);
        etUserName = (EditText) findViewById(R.id.usernameEditText);
        etPassword = (EditText) findViewById(R.id.passwordEditText);
        button = (Button)findViewById(R.id.btn_guestlogin_login) ;
        button.setEnabled(true);
        //

        isLogin();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        button.setEnabled(true);
                    }
                }, 2000);
//                etComName.setVisibility(View.INVISIBLE);
//                etComName.setText("ldly");
//                etUserName.setText("wxy");
//                etPassword.setText("admin@1234");
                final String name = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                RestBLL.login(name, password, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError) {
                            return;
                        }
                        CONFIG.set("USERNAME",name);
                        CONFIG.set("PASSWORD",password);
                        Log.e("run: ", CONFIG.getString(Common.CONFIG_TOKEN));
                        Intent intent = new Intent(GuestLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return true;
        }
        return false;
    }
}
