package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import cn.share.Common;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.DIALOG;
import cn.vipapps.MESSAGE;

public class AddCommentActivity extends PGACTIVITY {

    String messageId;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        messageId = getIntent().getStringExtra("messageId");
        editText = (EditText)this.findViewById(R.id.info);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.navigationBar().title("评论");
        this.navigationBar().rightNavButton("提交", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                RestBLL.addComment(messageId, editText.getText().toString(), new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject result) {
                        if (isError){

                        }
                        DIALOG.alert("评论成功!", new CALLBACK<Object>() {
                            @Override
                            public void run(boolean isError, Object result) {
                                MESSAGE.send(Common.MSG_COMMENT,null);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

}
