package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CAMERA;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;
import cn.vipapps.IMAGE;
import cn.vipapps.MESSAGE;
import cn.vipapps.WEB;

import static android.R.attr.bitmap;

public class ShareActivity extends PGACTIVITY {

    Bitmap img;
    ImageView imageView;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        imageView = (ImageView)findViewById(R.id.imgbnt_add);
        info = (TextView)findViewById(R.id.info);
        findViewById(R.id.imgbnt_add).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                DIALOG.chooseWithTitles(ShareActivity.this,
                        new CALLBACK<Integer>() {

                            @Override
                            public void run(boolean isError, Integer result) {
                                Log.e("CALLBACK<Integer> ",result+"" );
                                if (isError) {
                                    Log.e("CALLBACK<Integer> ","error" );
                                    return;
                                }

                                CALLBACK<Bitmap> callback = new CALLBACK<Bitmap>() {

                                    @Override
                                    public void run(boolean isError, Bitmap result) {
                                        if (isError) {
                                            Log.e("CALLBACK<Bitmap> ","error" );
                                            return;
                                        }
                                        Log.e("CALLBACK<Bitmap> ",result.toString()+""  );
                                        img = result;
                                        runOnUiThread(runnable);
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
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageView.setImageBitmap(img);
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("分享");
        this.navigationBar().rightNavButton("发送", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                RestBLL.addMessage(info.getText().toString(), img, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError){
                            return;
                        }
                        DIALOG.alert("分享成功！");
                    }
                });
            }
        });

    }

}
