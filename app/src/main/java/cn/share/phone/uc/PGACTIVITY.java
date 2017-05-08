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

	}
}
