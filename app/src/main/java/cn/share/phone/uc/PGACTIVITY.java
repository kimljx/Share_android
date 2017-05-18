package cn.share.phone.uc;


import cn.share.R;
import cn.vipapps.android.ACTIVITY;

public class PGACTIVITY extends ACTIVITY {

	@SuppressWarnings("deprecation")
	protected void onStart() {
		super.onStart();
		this.initNavigationBar(R.id.navigationBar,this.getResources().getColor(R.color.V));
	}
}
