package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class MyActivity extends PGACTIVITY implements View.OnClickListener{

    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_usertype, mp_userphone,
            mp_useremail, mp_userremark, mp_userrole, mp_userstatus;
    TableRow mp_password;

    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
//        mp_usertype = (TextView) this.findViewById(R.id.mp_usertype);
//        mp_userphone = (TextView) this.findViewById(R.id.mp_userphone);
//        mp_useremail = (TextView) this.findViewById(R.id.mp_useremail);
//        mp_userremark = (TextView) this.findViewById(R.id.mp_userremark);
//        mp_userrole = (TextView) this.findViewById(R.id.mp_userrole);
//        mp_userstatus = (TextView) this.findViewById(R.id.mp_userstatus);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_avatar.setImageDrawable(null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
        //判断是否登录，显示不同界面
        if (CONFIG.getString(Common.CONFIG_TOKEN) == null){
            this.findViewById(R.id.notLogin).setVisibility(View.VISIBLE);
            this.findViewById(R.id.mylogin).setVisibility(View.GONE);
            this.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MyActivity.this,GuestLoginActivity.class));
                }
            });
        }else {
            this.findViewById(R.id.notLogin).setVisibility(View.GONE);
            this.findViewById(R.id.mylogin).setVisibility(View.VISIBLE);
            reloadeData();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("我的");


    }

    void reloadeData(){
        RestBLL.my(new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                mp_name.setText(result.optString("userName"));
                //头像
                final String avatarurl = result.optString("pictureUrl");

                PGAJAX.getImage(avatarurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {

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
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.up:
                startActivity(new Intent(MyActivity.this,MyProfileActivity.class));
                break;
            case R.id.fault:
                startActivity(new Intent(MyActivity.this,HomeListActivity.class));
                break;
//            case R.id.repair:
//                startActivity(new Intent(MyActivity.this,RepairListActivity.class));
//                break;
            case R.id.data:
                startActivity(new Intent(MyActivity.this,DataFormActivity.class));
                break;
            default:
                break;
        }
    }
}