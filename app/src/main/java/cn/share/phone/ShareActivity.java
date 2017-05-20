package cn.share.phone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.IMAGE;
import cn.vipapps.MESSAGE;

//分享页面
public class ShareActivity extends PGACTIVITY {

    Bitmap img;
    ImageView imageView;
    TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        imageView = (ImageView) findViewById(R.id.imgbnt_add);
        info = (TextView) findViewById(R.id.info);
        //刷新页面数据
        MESSAGE.receive(Common.MSG_IMG, new CALLBACK<Bundle>() {
            @Override
            public void run(boolean isError, Bundle result) {
                img = CONFIG.getImage(Common.CONFIG_IMG);
                imageView.setImageBitmap(img);
            }
        });
        //进入添加图片页面
        this.findViewById(R.id.addimg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShareActivity.this, AddImageActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title("分享");
        this.navigationBar().rightNavButton("发送", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                //压缩图片并上传
                img =  IMAGE.zoom(img, 200, 200);
                RestBLL.addMessage(info.getText().toString(), img, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError){
                            return;
                        }
                    }
                });
            }
        });

    }

}
