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

public class MyActivity extends PGACTIVITY {

    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_usertype, mp_userphone,
            mp_useremail, mp_userremark, mp_userrole, mp_userstatus;
    TableRow mp_password;

    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
        mp_usertype = (TextView) this.findViewById(R.id.mp_usertype);
        mp_userphone = (TextView) this.findViewById(R.id.mp_userphone);
        mp_useremail = (TextView) this.findViewById(R.id.mp_useremail);
        mp_userremark = (TextView) this.findViewById(R.id.mp_userremark);
        mp_userrole = (TextView) this.findViewById(R.id.mp_userrole);
        mp_userstatus = (TextView) this.findViewById(R.id.mp_userstatus);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_avatar.setImageDrawable(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
        //
        mp_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyActivity.this, MyPasswordActivity.class);
                startActivity(intent);
            }
        });

        //
        fillData();
    }

    void fillData() {
        RestBLL.get_user_info(new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                JSONObject user = result.optJSONObject("user_info");
                mp_name.setText(user.optString("account"));
                mp_username.setText(user.optString("name"));
                //
                mp_usertype.setText(user.optString("type").equals("1") ? "乐园账号" : "企业账号");
                mp_userphone.setText(user.optString("phone"));
                mp_useremail.setText(user.optString("email"));
                mp_userremark.setText(user.optString("remark"));
                mp_userrole.setText(user.optString("role"));
//                mp_userrole.setText("段时间后点击按电视剧的计算机电视剧大大可发货的空间十分了十分平稳");
                //
                mp_userstatus.setText(user.optString("status").equals("1") ? "有效" : "无效");
                String userId = user.optString("id");
                String url = String.format("user_%s.png", userId);
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