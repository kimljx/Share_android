//package cn.share.phone;
//
//import android.app.ProgressDialog;
//import android.content.res.Configuration;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.AMapException;
//import com.amap.api.maps.AMapUtils;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.NaviPara;
//import com.amap.api.maps.overlay.PoiOverlay;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.core.PoiItem;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//
//import java.util.List;
//
//import cn.share.R;
//
//public class StaffListActivity extends AppCompatActivity implements LocationSource,AMapLocationListener , View.OnClickListener,PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {
//
//    private Button button1;
//    private AMap aMap;
//    private OnLocationChangedListener  mListener;
//    private MapView mMapView;
//    //声明AMapLocationClient类对象
//    public AMapLocationClient mapLocationClient;
//    //声明AMapLocationClientOption对象
//    public AMapLocationClientOption mapLocationClientOption;
//    ////////
//    private ProgressDialog progDialog = null;// 搜索时进度条
//    private String keyWord;
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;//搜索
//    private PoiSearch.SearchBound searchBound;
//    private int currentPage;// 当前页面，从0开始计数
//    private PoiResult poiResults; // poi返回的结果
//    private String city = "";//搜索城市
//    private EditText editText1;
//    private LatLonPoint latLonPoint;
//    private Spinner spinner;
//    private int juli = 1000;
//    ////////
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_staff_list);
//        mMapView = (MapView)findViewById(R.id.map);
//        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
//        mMapView.onCreate(savedInstanceState);
//        init();
//    }
//
//    private void init(){
//
//        if (aMap==null){
//            aMap = mMapView.getMap();
//        }
//        setUp();
//    }
//
//    private void setUp() {
//
//        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
//        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        aMap.setMyLocationEnabled(true);
//        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//
//        button1 = (Button) findViewById(R.id.search_button);
//        button1.setOnClickListener(this);
//        spinner = (Spinner)findViewById(R.id.juli);
//        String[] ints = {"1000","2000","3000","4000","5000"};
//        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,ints);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<!--?--> parent, View view, int position, long id) {
//                juli = Integer.valueOf(String.valueOf(spinner.getSelectedItem()));
//                Log.i("---",juli-10+"");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<!--?--> parent) {
//
//            }
//        });
//        editText1 = (EditText) findViewById(R.id.search_edit);
//
//        //editText1.addTextChangedListener(this);
//        aMap.setOnMarkerClickListener(this);
//        aMap.setInfoWindowAdapter(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
//        mapLocationClient.onDestroy();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
//        mMapView.onResume();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
//        mMapView.onPause();
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mapLocationClient.stopLocation();
//    }
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
//        mMapView.onSaveInstanceState(outState);
//    }
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation!=null){
//            if (aMapLocation.getErrorCode()==0){
//
//                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//
//                StringBuilder stringBuilder = new StringBuilder();
//                //定位成功回调信息，设置相关消息
//                int type = aMapLocation.getLocationType();
//                String address = aMapLocation.getAddress();
//                stringBuilder.append(type+address);
//                city = aMapLocation.getCity();
//                //获得小点
//                if (latLonPoint==null){
//                    latLonPoint = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
//                }else {
//                    latLonPoint.setLatitude(aMapLocation.getLatitude());
//                    latLonPoint.setLongitude(aMapLocation.getLongitude());
//                }
//                //Toast.makeText(this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
//            }else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
//                Log.i("erro info：",aMapLocation.getErrorCode()+"---"+aMapLocation.getErrorInfo());
//            }
//        }
//    }
//    //激活定位
//    @Override
//    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener = onLocationChangedListener;
//        if (mapLocationClient==null){
//            //初始化AMapLocationClient，并绑定监听
//            mapLocationClient = new AMapLocationClient(getApplicationContext());
//
//            //初始化定位参数
//            mapLocationClientOption = new AMapLocationClientOption();
//            //设置定位精度
//            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //是否返回地址信息
//            mapLocationClientOption.setNeedAddress(true);
//            //是否只定位一次
//            mapLocationClientOption.setOnceLocation(false);
//            //设置是否强制刷新WIFI，默认为强制刷新
//            mapLocationClientOption.setWifiActiveScan(true);
//            //是否允许模拟位置
//            mapLocationClientOption.setMockEnable(false);
//            //定位时间间隔
//            mapLocationClientOption.setInterval(2000);
//            //给定位客户端对象设置定位参数
//            mapLocationClient.setLocationOption(mapLocationClientOption);
//            //绑定监听
//            mapLocationClient.setLocationListener(this);
//            //开启定位
//            mapLocationClient.startLocation();
//        }
//
//    }
//    //停止定位
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mapLocationClient!=null){
//            mapLocationClient.stopLocation();
//            mapLocationClient.onDestroy();
//        }
//        mapLocationClient = null;
//    }
//    long firstTime = 0;
//    //双击退出
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode==KeyEvent.KEYCODE_BACK){
//            long secondTime = System.currentTimeMillis();
//            if (secondTime-firstTime>2000){
//                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
//                firstTime = System.currentTimeMillis();
//                return true;
//            }else {
//                finish();
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            Log.d("=========", "横屏");
//        }else {
//            Log.d("=========", "竖屏");
//        }
//    }
//    /**
//     * 点击搜索按钮
//     */
//    private void search(){
//        keyWord = editText1.getText().toString();
//        Log.i("---", keyWord);
//        if ("".equals(keyWord)){
//            Toast.makeText(MainActivity.this, "请输入搜索关键字",Toast.LENGTH_SHORT).show();
//            return;
//        }else {
//            doSearchQuery();
//        }
//    }
//    /**
//     * 搜索操作
//     */
//    private void doSearchQuery() {
//        showProgressDialog();
//        currentPage = 0;
//        //第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query = new PoiSearch.Query(keyWord,"",city);
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第一页
//        poiSearch = new PoiSearch(this,query);
//        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
//        //点附近2000米内的搜索结果
//        if (latLonPoint!=null){
//            searchBound = new PoiSearch.SearchBound(latLonPoint,juli);
//            poiSearch.setBound(searchBound);
//        }
//        poiSearch.searchPOIAsyn();//开始搜索
//    }
//    /**
//     * 显示进度框
//     */
//    private void showProgressDialog() {
//        if (progDialog == null)
//            progDialog = new ProgressDialog(this);
//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setIndeterminate(false);
//        progDialog.setCancelable(false);
//        progDialog.setMessage("正在搜索:\n" + keyWord);
//        progDialog.show();
//    }
//
//    /**
//     * 隐藏进度框
//     */
//    private void dissmissProgressDialog() {
//        if (progDialog != null) {
//            progDialog.dismiss();
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.search_button:
//                Log.i("---","搜索操作");
//                search();
//                break;
//        }
//    }
//    /**
//     * poi没有搜索到数据，返回一些推荐城市的信息
//     */
//    private void showSuggestCity(List<suggestioncity> cities) {
//        String infomation = "推荐城市\n";
//        for (int i = 0; i < cities.size(); i++) {
//            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
//                    + cities.get(i).getCityCode() + "城市编码:"
//                    + cities.get(i).getAdCode() + "\n";
//        }
//        Toast.makeText(MainActivity.this, infomation,Toast.LENGTH_SHORT).show();
//
//    }
//
//
//    /**
//     * POI信息查询回调方法
//     */
//    @Override
//    public void onPoiSearched(PoiResult poiResult, int i) {
//        dissmissProgressDialog();// 隐藏对话框
//        if (i == 1000) {
//            Log.i("---","查询结果:"+i);
//            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
//                if (poiResult.getQuery().equals(query)) {// 是否是同一条
//                    poiResults = poiResult;
//                    // 取得搜索到的poiitems有多少页
//                    List<poiitem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    List<suggestioncity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//
//                    if (poiItems != null && poiItems.size() > 0) {
//                        aMap.clear();// 清理之前的图标
//                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
//                        poiOverlay.removeFromMap();
//                        poiOverlay.addToMap();
//                        poiOverlay.zoomToSpan();
//                    } else if (suggestionCities != null
//                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
//                    } else {
//                        Toast.makeText(MainActivity.this, "未找到结果",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else {
//                Toast.makeText(MainActivity.this, "该距离内没有找到结果",Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Log.i("---","查询结果:"+i);
//            Toast.makeText(MainActivity.this, "异常代码---"+i,Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//    }
//
//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        marker.showInfoWindow();
//        return false;
//    }
//    /**
//     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
//     */
//    public void startAMapNavi(Marker marker) {
//        // 构造导航参数
//        NaviPara naviPara = new NaviPara();
//        // 设置终点位置
//        naviPara.setTargetPoint(marker.getPosition());
//        // 设置导航策略，这里是避免拥堵
//        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);
//
//        // 调起高德地图导航
//        try {
//            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
//        } catch (AMapException e) {
//
//            // 如果没安装会进入异常，调起下载页面
//            AMapUtils.getLatestAMapApp(getApplicationContext());
//
//        }
//
//    }
//    @Override
//    public View getInfoWindow(final Marker marker) {
//        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
//                null);
//        TextView title = (TextView) view.findViewById(R.id.title);
//        title.setText(marker.getTitle());
//
//        TextView snippet = (TextView) view.findViewById(R.id.snippet);
//        snippet.setText(marker.getSnippet());
//        ImageButton button = (ImageButton) view
//                .findViewById(R.id.start_amap_app);
//        // 调起高德地图app
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startAMapNavi(marker);
//            }
//        });
//        return view;
//    }
//
//    @Override
//    public View getInfoContents(Marker marker) {
//        return null;
//    }
//}