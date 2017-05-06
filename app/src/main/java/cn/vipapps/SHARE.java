package cn.vipapps;//package cn.vipapps;
//
//import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//import android.graphics.Bitmap;
//import android.util.Log;
//
//import cn.share.phone.SplashActivity;
//import cn.vipapps.android.ACTIVITY;
//
//public class SHARE {
//
////	public static void share(MediaType platform, String title, String text, Bitmap image, String url,
////			final CALLBACK<Boolean> callback) {
////		try {
////
////			//
////			FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
////			FrontiaSocialShare mSocialShare = Frontia.getSocialShare();
////			mSocialShare.setContext(ACTIVITY.context);
////			mImageContent.setTitle(title);// "百度开发中心");
////			mImageContent.setContent(text);// "欢迎使用百度社会化分享组件，相关问题请邮件dev_support@baidu.com");
////			mImageContent.setLinkUrl(url);// "http://developer.baidu.com/");
////			mImageContent.setImageData(image);
////			// mImageContent.setImageUri(Uri.parse("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png"));
////			mSocialShare.share(mImageContent, platform.toString(), new FrontiaSocialShareListener() {
////
////				@Override
////				public void onCancel() {
////					callback.run(true, false);
////				}
////
////				@SuppressWarnings({ "unchecked", "rawtypes" })
////				@Override
////				public void onFailure(int arg0, String arg1) {
////					DIALOG.alert(arg1, new CALLBACK() {
////
////						@Override
////						public void run(boolean isError, Object result) {
////
////							callback.run(false, false);
////						}
////
////					});
////				}
////
////				@Override
////				public void onSuccess() {
////					callback.run(false, true);
////				}
////
////			}, true);
////		} catch (Exception e) {
////
////			e.printStackTrace();
////			DIALOG.alert("分享失败!");
////
////		}
////	}
//	public static void share( String title, String text, Bitmap image, String url,
//			final CALLBACK<Boolean> callback) {
//		try {
//			IWXAPI api = WXAPIFactory.createWXAPI(ACTIVITY.context, SplashActivity.WEIXIN_KEY,true);
//			api.registerApp(SplashActivity.WEIXIN_KEY);
//
//			WXWebpageObject webpage = new WXWebpageObject();
//			webpage.webpageUrl = url;
//			WXMediaMessage msg = new WXMediaMessage(webpage);
//			msg.title = title;
//			msg.description = text;
//			SendMessageToWX.Req req = new SendMessageToWX.Req();
//			req.transaction = java.util.UUID.randomUUID().toString();
//			req.message = msg;
//			req.scene = SendMessageToWX.Req.WXSceneSession;
//			api.sendReq(req);
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			DIALOG.alert("分享失败!");
//
//		}
//	}
//}
