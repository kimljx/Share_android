package cn.share.phone;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CAMERA;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;
import cn.vipapps.MESSAGE;
//分享页中的添加图片页面
public class AddImageActivity extends PGACTIVITY {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        imageView = (ImageView) findViewById(R.id.imgbnt_add);
        //点击后选择照相或者从相册获取图片
        findViewById(R.id.imgbnt_add).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //底部弹框（actionsheet）
                DIALOG.chooseWithTitles(AddImageActivity.this,
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

    private Bitmap img;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            imageView.setImageBitmap(img);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("添加图片");
        this.navigationBar().rightNavButton("确定", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                //保存图片发送广播并结束本页面
                CONFIG.setImage(Common.CONFIG_IMG,img);
                MESSAGE.send(Common.MSG_IMG,null);
                finish();
            }
        });
    }
}
