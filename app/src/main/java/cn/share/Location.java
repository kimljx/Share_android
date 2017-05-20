package cn.share;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

import cn.vipapps.CALLBACK;
import cn.vipapps.android.ACTIVITY;

/**
 * Created by luo on 2017/3/22.
 */
//定位类
public class Location {

    CALLBACK locationCallback;

    private static  Location location = null;

    private Location(){
       mLocationOption = new AMapLocationClientOption();
        mlocationClient = new AMapLocationClient(ACTIVITY.context);
    }

    //单列模式
    public static Location getInstance(){
        if (location == null){
            location = new Location();
        }
        return location;
    }

    AMapLocationClientOption mLocationOption;
    AMapLocationClient mlocationClient;

    //回调监听事件
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    String w = Double.toString(amapLocation.getLatitude());//获取纬度
                    String j = Double.toString(amapLocation.getLongitude());//获取经度
                    LatLng p = new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                    String position = w + "," + j;
                    locationCallback.run(false, p);
                } else {
                    locationCallback.run(true, null);
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    //打开定位
    public void startPosition(final CALLBACK<LatLng> callback) {

        locationCallback = callback;
        //设置精度
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，1200000
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mlocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mlocationClient.startLocation();
        Log.e("startLocation", "startLocation");
    }

    //关闭定位
    public void stopPosition() {
        //关闭定位
        mlocationClient.stopLocation();
        Log.e("stopLocation", "stopLocation");
    }


}
