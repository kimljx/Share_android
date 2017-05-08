package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.share.Common;
import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;

import org.json.JSONObject;

import uc.CircleImageView;
import uc.TableRow;

public class MyProfileActivity extends PGACTIVITY {

    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_usertype, mp_userphone,
            mp_useremail, mp_userremark, mp_userrole, mp_userstatus;
    TableRow mp_password,mp_sex;

    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
//        mp_usertype = (TextView) this.findViewById(R.id.mp_usertype);
//        mp_userphone = (TextView) this.findViewById(R.id.mp_userphone);
//        mp_useremail = (TextView) this.findViewById(R.id.mp_useremail);
//        mp_userremark = (TextView) this.findViewById(R.id.mp_userremark);
//        mp_userrole = (TextView) this.findViewById(R.id.mp_userrole);
        mp_userstatus = (TextView) this.findViewById(R.id.sex);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_sex = (TableRow) this.findViewById(R.id.mp_sex);
        mp_avatar.setImageDrawable(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile2);
        init();
        //
        mp_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, MyPasswordActivity.class);
                startActivity(intent);
            }
        });

        //
        fillData();
    }

    void fillData() {
        RestBLL.my(new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
//
                mp_name.setText(result.optString("userName"));
                //头像
                final String url = result.optString("pictureUrl");
                PGAJAX.getImage(url, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (isError) {
                            mp_avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                            return;
                        }
                        if (result == null) {
                            mp_avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));
                            return;
                        }
                        mp_avatar.setImageBitmap(result);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("我");
        if (Common.isGuest()) {

        } else {
            this.navigationBar().rightNavButton("登出", new CALLBACK() {
                @Override
                public void run(boolean isError, Object result) {

                    DIALOG.confirm("确认登出？", new CALLBACK<Object>() {
                        @Override
                        public void run(boolean isError, Object results) {

                            RestBLL.logout(new CALLBACK<JSONObject>() {
                                @Override
                                public void run(boolean isError, JSONObject result) {
                                    if (isError) {
                                        return;
                                    }
                                    finish();
                                }
                            });
                        }

                    }, new CALLBACK<Object>() {
                        @Override
                        public void run(boolean isError, Object results) {

                        }
                    });
                }
            });
        }

    }
}