package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.share.Common;
import cn.share.PGAJAX;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CAMERA;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;

import org.json.JSONObject;

import cn.vipapps.MESSAGE;
import uc.CircleImageView;
import uc.TableRow;

public class MyProfileActivity extends PGACTIVITY {

    CircleImageView mp_avatar;
    TextView mp_name, mp_username, mp_userstatus;
    TableRow mp_password,mp_sex;
    RelativeLayout re;
    Bitmap bitmap;

    void init() {
        mp_avatar = (CircleImageView) this.findViewById(R.id.mp_avatar);
        mp_name = (TextView) this.findViewById(R.id.mp_name);
        mp_username = (TextView) this.findViewById(R.id.mp_username);
        mp_userstatus = (TextView) this.findViewById(R.id.sex);
        mp_password = (TableRow) this.findViewById(R.id.mp_password);
        mp_sex = (TableRow) this.findViewById(R.id.mp_sex);
        re = (RelativeLayout)this.findViewById(R.id.re);
        mp_avatar.setImageDrawable(null);
    }

    String[] typeChoose= new String[]{
            "男","女","保密"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile2);
        init();

        //修改密码
        mp_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, MyPasswordActivity.class);
                startActivity(intent);
            }
        });
        //修改性别
        mp_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DIALOG.pickerView(context,getResources().getColor(R.color.V),typeChoose,new CALLBACK<Integer>(){

                    @Override
                    public void run(boolean isError, Integer result) {
                        final String sex = Common.leaveType(String.valueOf(result+1));
                        RestBLL.updateSex(sex, new CALLBACK<JSONObject>() {
                            @Override
                            public void run(boolean isError, JSONObject result) {
                                if (isError){
                                    return;
                                }
                                mp_username.setText(sex);
                            }
                        });
                    }
                });
            }
        });
        //修改头像
        mp_avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DIALOG.chooseWithTitles(MyProfileActivity.this,
                        new CALLBACK<Integer>() {

                            @Override
                            public void run(boolean isError, Integer result) {
                                if (isError) {
                                    return;
                                }
                                CALLBACK<Bitmap> callback = new CALLBACK<Bitmap>() {

                                    @Override
                                    public void run(boolean isError, Bitmap result) {
                                        if (isError) {
                                            return;
                                        }
                                        bitmap = result;
                                        RestBLL.uploadAvatar(bitmap, new CALLBACK<JSONObject>() {
                                            @Override
                                            public void run(boolean isError, JSONObject result) {
                                                if (isError){
                                                    return;
                                                }
                                                MESSAGE.send(Common.MSG_HASLOGIN,null);
                                                runOnUiThread(runnable);
                                            }
                                        });

                                    }

                                };
                                switch (result) {
                                    case 0:
                                        CAMERA.openGallery(callback);
                                        break;
                                    case 1:
                                        CAMERA.openCamera(callback);
                                        break;
                                    default:
                                        break;
                                }

                            }

                        }, "打开相册", "现在拍照");

            }

        });
        fillData();
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mp_avatar.setImageBitmap(bitmap);
            Drawable drawable =new BitmapDrawable(bitmap);
            re.setBackground(drawable);
        }
    };

    void fillData() {
        RestBLL.my(new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }

                mp_username.setText(result.optString("sex"));
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
                        Drawable drawable =new BitmapDrawable(result);
                        re.setBackground(drawable);
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