package cn.share.phone;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;


import cn.share.Location;
import cn.share.R;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.STRING;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends PGACTIVITY {

//
//    private String staffId;
//    private String staffName;
//    private MapView mMapView;
//    private AMap aMap;
//    private LinearLayout.LayoutParams mParams;
//    private LinearLayout linearLayout;
//    private LatLng centerCDpoint = new LatLng(30.697218,104.073694);// (纬度，经度)
//    String start, end;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_staff_trace);
//        linearLayout = (LinearLayout) findViewById(R.id.activity_staff_trace);
//        initialCenter();
//        mMapView.onCreate(savedInstanceState);// 此方法必须重写
//        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        linearLayout.addView(mMapView, mParams);
//
//        init();
//        Log.e("LocationActivity: ", sHA1(context));
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        this.navigationBar().title("定位");
//
//    }
//
//    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
//
//    private void init() {
//
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//        }
//        aMap.setLocationSource(new LocationSource() {
//            @Override
//            public void activate(OnLocationChangedListener onLocationChangedListener) {
//
//            }
//
//            @Override
//            public void deactivate() {
//
//            }
//        });
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        aMap.setMyLocationEnabled(true);
//        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//    }
//
////    /**
////     * 在地图上画线
////     */
////    public void setUpMap(List<LatLng> list) {
////
////        if (list.size() > 0) {
////
////            PolylineOptions polt = new PolylineOptions();
////
////            for (int i = 0; i < list.size(); i++) {
////
////                polt.add(list.get(i));
////
////            }
////            polt.width(5).geodesic(true).color(Color.RED);
////            aMap.addPolyline(polt);
////
////        } else {
////
////            Toast.makeText(this, "没有移动轨迹", Toast.LENGTH_LONG).show();
////        }
////
////    }
//
////    void clearTrace(){
////        latLngs.clear();
////        aMap.clear();
////    }
//
//    private void initialCenter() {
////        Location.getInstance().startPosition(new CALLBACK<LatLng>() {
////            @Override
////            public void run(boolean isError, LatLng result) {
////                centerCDpoint = result;
////
////            }
////        });
////        centerCDpoint=new LatLng(
////                aMap.getMyLocation().getLatitude(),aMap.getMyLocation().getLongitude()
////        ) ;
//        AMapOptions aOptions = new AMapOptions();
//        aOptions.camera(new CameraPosition(centerCDpoint, 10f, 0, 0));
//        mMapView = new MapView(this, aOptions);
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mMapView.onResume();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mMapView.onResume();
//    }
//
//    public static String sHA1(Context context) {
//        PackageInfo info = null;
//        try {
//            info = context.getPackageManager().getPackageInfo(
//                    context.getPackageName(), PackageManager.GET_SIGNATURES);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        byte[] cert = info.signatures[0].toByteArray();
//        MessageDigest md = null;
//        try {
//            md = MessageDigest.getInstance("SHA1");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        byte[] publicKey = md.digest(cert);
//        StringBuffer hexString = new StringBuffer();
//        for (int i = 0; i < publicKey.length; i++) {
//            String appendString = Integer.toHexString(0xFF & publicKey[i])
//                    .toUpperCase(Locale.US);
//            if (appendString.length() == 1)
//                hexString.append("0");
//            hexString.append(appendString);
//            hexString.append(":");
//        }
//        String result = hexString.toString();
//        return result.substring(0, result.length() - 1);
////        return null;
//    }
}
