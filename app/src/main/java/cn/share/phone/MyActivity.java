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

import org.json.JSONObject;

import cn.vipapps.MESSAGE;
import uc.CircleImageView;
import uc.TableRow;

public class MyActivity extends PGACTIVITY implements View.OnClickListener {

    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_usertype, mp_userphone,
            mp_useremail, mp_userremark, mp_userrole, mp_userstatus;
    TableRow mp_password;

    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_avatar.setImageDrawable(null);
    }

    void isLogin() {
        //判断是否登录，显示不同界面
        if (CONFIG.getString(Common.CONFIG_TOKEN) == null) {
            this.findViewById(R.id.notLogin).setVisibility(View.VISIBLE);
            this.findViewById(R.id.mylogin).setVisibility(View.GONE);
            this.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MyActivity.this, GuestLoginActivity.class));
                }
            });
        } else {
            this.findViewById(R.id.notLogin).setVisibility(View.GONE);
            this.findViewById(R.id.mylogin).setVisibility(View.VISIBLE);
            reloadeData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
        isLogin();
        MESSAGE.receive(Common.MSG_HASLOGIN, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                isLogin();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("我的");
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (bitmap != null){
                mp_avatar.setImageBitmap(bitmap);
            }else {
                mp_avatar.setImageDrawable(getResources().getDrawable(R.mipmap.avatar));

            }
            if (userName != null){
                mp_name.setText(userName);
            }

        }
    };
    String userName;
    Bitmap bitmap;
    void reloadeData() {
        RestBLL.my(new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError){
                    return;
                }
                userName = result.optString("userName");
                //头像
                final String avatarurl = result.optString("pictureUrl");
                PGAJAX.getImage(avatarurl, true, new CALLBACK<Bitmap>() {
                    @Override
                    public void run(boolean isError, Bitmap result) {
                        if (isError) {
                            bitmap = null;
                            return;
                        }
                        bitmap = result;
                        runOnUiThread(runnable);

                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my:
                startActivity(new Intent(MyActivity.this, MyProfileActivity.class));
                break;
            case R.id.collect:
                Intent i1 = new Intent(MyActivity.this, MyListActivity.class);
                i1.putExtra("type","我的收藏");
                startActivity(i1);                break;
            case R.id.comment:
                Intent i2 = new Intent(MyActivity.this, MyListActivity.class);
                i2.putExtra("type","我的评论");
                startActivity(i2);                break;
            case R.id.share:
                Intent i3 = new Intent(MyActivity.this, MyListActivity.class);
                i3.putExtra("type","我的分享");
                startActivity(i3);
                break;
            default:
                break;
        }
    }
}