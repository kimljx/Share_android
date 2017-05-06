package cn.vipapps;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WEB {
	public static void load(WebView webView, String url) {
//		Map<String, String> headers = new HashMap<String, String>();
//		headers.put("content-type", "text/html");
//		headers.put("COOKIE", (String) CONFIG.get(Common.CONFIG_TOKEN));
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		webView.setWebChromeClient(new MyWebChromeClient());
		//webView.loadData("","text/html","UTF-8");
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl(url);
		//Log.e("COOKIE"," "+CONFIG.get(Common.CONFIG_TOKEN));
	}
	static private class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onConsoleMessage(ConsoleMessage cm) {
			Log.d("test", cm.message() + " -- From line "
					+ cm.lineNumber() + " of "
					+ cm.sourceId() );
			return true;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
