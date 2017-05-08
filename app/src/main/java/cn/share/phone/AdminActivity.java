package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.MESSAGE;

public class AdminActivity extends PGACTIVITY implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MESSAGE.send(Common.MSG_CHANGEBAR,null);
        this.navigationBar().title("人事");
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.ad_myprofile:
                startActivity(new Intent(AdminActivity.this,MyActivity.class));
                break;
            case R.id.ad_staff:
//                startActivity(new Intent(AdminActivity.this,StaffListActivity.class));
                break;
            case R.id.ad_leave:
                startActivity(new Intent(AdminActivity.this,LeaveListActivity.class));
                break;
            case R.id.ad_myleave:
                startActivity(new Intent(AdminActivity.this,MyLeavesActivity.class));
                break;
            default:
                break;
        }
    }
}
