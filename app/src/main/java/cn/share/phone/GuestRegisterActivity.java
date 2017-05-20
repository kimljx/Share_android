package cn.share.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;

//注册
public class GuestRegisterActivity extends PGACTIVITY {
    EditText etUserName, etPassword;
    Button button;

    public static void startActivity(Context context){
        Intent intent=new Intent(context,GuestRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);
        etUserName = (EditText) findViewById(R.id.usernameEditText);
        etPassword = (EditText) findViewById(R.id.passwordEditText);
        button = (Button)findViewById(R.id.btn_guestlogin_login) ;
        button.setEnabled(true);

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
                RestBLL.register(name, password, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError) {
                            return;
                        }
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
        this.navigationBar().title("注册");
    }
}
