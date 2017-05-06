package cn.share.phone;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import cn.share.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomScanActivity extends CaptureActivity implements DecoratedBarcodeView.TorchListener { // 实现相关接口
    // 添加一个按钮用来控制闪光灯，同时添加两个按钮表示其他功能，先用Toast表示

    //    @BindView(R.id.btn_switch) Button swichLight;
//    @BindView(R.id.btn_hint1) Button hint1Show;
//    @BindView(R.id.btn_hint2) Button hint2Show;
    @BindView(R.id.dbv_custom)
    DecoratedBarcodeView mDBV;

    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scan);
        ButterKnife.bind(this);

        mDBV.setTorchListener(this);

        // 如果没有闪光灯功能，就去掉相关按钮
//        if(!hasFlash()) {
//            swichLight.setVisibility(View.GONE);
//        }

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
    }

    // torch 手电筒
    @Override
    public void onTorchOn() {
        Toast.makeText(this, "torch on", Toast.LENGTH_LONG).show();
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        Toast.makeText(this, "torch off", Toast.LENGTH_LONG).show();
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    // 点击切换闪光灯
//    @OnClick(R.id.btn_switch)
//    public void swichLight(){
//        if(isLightOn){
//            mDBV.setTorchOff();
//        }else{
//            mDBV.setTorchOn();
//        }
//    }
}
