package cn.share.phone;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CONFIG;
import cn.vipapps.MESSAGE;
import cn.vipapps.WEB;

public class WorkActivity extends PGACTIVITY {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        webView = (WebView) findViewById(R.id.work_webview);
        String token = CONFIG.getString(Common.CONFIG_TOKEN);
        String url = String.format("%s/app/index.php?type=1&token=%s",Common.URL_BASE,token);
        Log.e( "onCreate:  url ",url );
        WEB.load(webView,url);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MESSAGE.send(Common.MSG_CHANGEBAR,new Bundle());
    }

}
