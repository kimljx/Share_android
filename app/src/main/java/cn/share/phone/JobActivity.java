package cn.share.phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.share.Common;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.MESSAGE;

public class JobActivity extends PGACTIVITY implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MESSAGE.send(Common.MSG_CHANGEBAR,null);
        this.navigationBar().title("岗位");
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.up:
                startActivity(new Intent(JobActivity.this,SignInActivity.class));
                break;
            case R.id.down:
                startActivity(new Intent(JobActivity.this,SignOutActivity.class));
                break;
            case R.id.fault:
                startActivity(new Intent(JobActivity.this,FaultListActivity.class));
                break;
            case R.id.repair:
                startActivity(new Intent(JobActivity.this,RepairListActivity.class));
                break;
            case R.id.data:
                startActivity(new Intent(JobActivity.this,DataFormActivity.class));
                break;

            default:
                break;
        }
    }
}
