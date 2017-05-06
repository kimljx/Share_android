package cn.share.phone;

import android.os.Bundle;
import android.webkit.WebView;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CONFIG;
import cn.vipapps.MESSAGE;
import cn.vipapps.WEB;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends PGACTIVITY {

    @BindView(R.id.webview)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        String messageId = getIntent().getStringExtra("messageId");
        String token = CONFIG.getString(Common.CONFIG_TOKEN);
        String url = String.format("%s/app/msg.php?&id=%s&token=%s"	,Common.URL_BASE,messageId,token);
        WEB.load(webView,url);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MESSAGE.send(Common.MSG_MESSAGEREFESH,null);
    }
}
