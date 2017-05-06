package cn.share.phone.uc;


import android.content.Intent;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.GuestLoginActivity;
import cn.share.phone.MainActivity;
import cn.share.phone.RegisterActivity;
import cn.share.phone.SplashActivity;
import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;

public class PGACTIVITY extends ACTIVITY {

	@SuppressWarnings("deprecation")
	protected void onStart() {
		super.onStart();
		this.initNavigationBar(R.id.navigationBar,this.getResources().getColor(R.color.V));
//		if (Common.isGuest()){
//			this.navigationBar().rightNavButton("登录", new CALLBACK() {
//				@Override
//				public void run(boolean isError, Object result) {
//					Intent intent = new Intent(context, GuestLoginActivity.class);
//					startActivity(intent);
//				}
//			});
//
//			this.navigationBar().leftNavButton("注册", new CALLBACK() {
//				@Override
//				public void run(boolean isError, Object result) {
//					Intent intent = new Intent(context, RegisterActivity.class);
//					startActivity(intent);
//				}
//			});
//		}
	}
}
