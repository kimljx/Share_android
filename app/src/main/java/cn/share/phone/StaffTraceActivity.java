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

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolylineOptions;
import cn.share.R;
import cn.share.RestBLL;
import cn.share.phone.uc.PGACTIVITY;
import cn.vipapps.CALLBACK;
import cn.vipapps.STRING;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StaffTraceActivity extends PGACTIVITY {


    private String staffId;
    private String staffName;
    private MapView mMapView;
    private AMap aMap;
    private LinearLayout.LayoutParams mParams;
    private LinearLayout linearLayout;
    private LatLng centerCDpoint = new LatLng(30.723342, 103.825434);// 国色天香(纬度，经度)
    String start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_trace);
        staffId = getIntent().getStringExtra("staffId");
        staffName = getIntent().getStringExtra("staffName");
        linearLayout = (LinearLayout) findViewById(R.id.activity_staff_trace);
//        mMapView = (MapView) findViewById(R.id.mMapView);
        initialCenter();
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.addView(mMapView, mParams);
//        AMap aMap = mMapView.getMap();
        init();
        Log.e("StaffTraceActivity: ", sHA1(context));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.navigationBar().title(staffName);
        this.navigationBar().rightNavButton("时间段", new CALLBACK() {
            @Override
            public void run(boolean isError, Object result) {
                Intent intent = new Intent(StaffTraceActivity.this, StaffTraceTimeActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();

    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }

    /**
     * 在地图上画线
     */
    public void setUpMap(List<LatLng> list) {

        if (list.size() > 0) {

            PolylineOptions polt = new PolylineOptions();

            for (int i = 0; i < list.size(); i++) {

                polt.add(list.get(i));

            }
            polt.width(5).geodesic(true).color(Color.RED);
            aMap.addPolyline(polt);

        } else {

            Toast.makeText(this, "没有移动轨迹", Toast.LENGTH_LONG).show();
        }

    }

    void clearTrace(){
        latLngs.clear();
        aMap.clear();
    }

    private void initialCenter() {
        AMapOptions aOptions = new AMapOptions();
        aOptions.camera(new CameraPosition(centerCDpoint, 10f, 0, 0));
        mMapView = new MapView(this, aOptions);

    }

    private void getStaffPosition(JSONArray trace) {
        latLngs.clear();
        if (trace!=null) {
            for (int i = 0; i < trace.length(); i++) {
                String content = trace.optJSONObject(i).optString("content");

                if (content == null || content == "") {
                    continue;
                }
                Object[] contents = STRING.parse(content, ",");
                if (contents == null || contents.length < 2) {
                    continue;
                }
                double v = Double.parseDouble(contents[0].toString());
                double v1 = Double.parseDouble(contents[1].toString());
                latLngs.add(new LatLng(v, v1));
            }
        }

//        latLngs.add(new LatLng(30.727694, 103.828962));
//        latLngs.add(new LatLng(30.721726, 103.82907));
//        latLngs.add(new LatLng(30.718774, 103.825637));
//        latLngs.add(new LatLng(30.723342, 103.825434));
        setUpMap(latLngs);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data!=null) {
                clearTrace();
                String sta = data.getStringExtra("start");
                String end = data.getStringExtra("end");
                RestBLL.get_staff_position(sta, end, staffId, new CALLBACK<JSONArray>() {
                    @Override
                    public void run(boolean isError, JSONArray result) {
                        Log.e("get_staff_position: ", result + "");
                        if (result.length() < 1) {

                            return;
                        }
                        getStaffPosition(result);
                    }
                });
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onResume();
    }

    public static String sHA1(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        byte[] cert = info.signatures[0].toByteArray();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] publicKey = md.digest(cert);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < publicKey.length; i++) {
            String appendString = Integer.toHexString(0xFF & publicKey[i])
                    .toUpperCase(Locale.US);
            if (appendString.length() == 1)
                hexString.append("0");
            hexString.append(appendString);
            hexString.append(":");
        }
        String result = hexString.toString();
        return result.substring(0, result.length() - 1);
//        return null;
    }
}
