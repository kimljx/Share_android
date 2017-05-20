package cn.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Administrator on 2017/4/25.
 * 地图工具类
 */

public class NativeUtil {

    /***
     * @param originlat 起点维度
     * @param originlon 起点经度
     * @param destinationlat 终点
     * @param mode 出行方式 导航模式，固定为transit（公交）、 driving（驾车）、walking（步行）和riding（骑行）. 默认:driving
     * 具体看百度地图开放者平台官网  http://lbsyun.baidu.com/index.php?title=uri/api/android
     */
  /*// 公交路线规划
  //i1.setData(Uri.parse("baidumap://map/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=name:西直门&mode=transit&sy=3&index=0&target=1"));
  // 驾车路线规划
  i1.setData(Uri.parse("baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=name:西直门&mode=driving"));
  // 步行路线规划
  i1.setData(Uri.parse("baidumap://map/direction?region=beijing&origin=39.98871,116.43234&destination=40.057406655722,116.2964407172&mode=walking"));
  startActivity(i1);*/
    public static void goToBaiduNaviActivity(Context context, double originlat, double originlon,
                                             double destinationlat, double destinationlon, String mode) {
        String locationStr = "baidumap://map/direction?origin="
                + GCJ02ToBD09(originlon, originlat)[1]
                + ","
                + GCJ02ToBD09(originlon, originlat)[0]
                + "&destination="
                + GCJ02ToBD09(destinationlon, destinationlat)[1]
                + ","
                + GCJ02ToBD09(destinationlon, destinationlat)[0]
                + "&mode="
                + mode;
        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse(/*stringBuffer.toString()*/locationStr));
        intent.setPackage("com.baidu.BaiduMap");
        context.startActivity(intent);
    }
    private static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
    public static Double[] GCJ02ToBD09(Double gcj_lon, Double gcj_lat) {
        double z =
                Math.sqrt(gcj_lon * gcj_lon + gcj_lat * gcj_lat) + 0.00002 * Math.sin(gcj_lat * x_PI);
        double theta = Math.atan2(gcj_lat, gcj_lon) + 0.000003 * Math.cos(gcj_lon * x_PI);
        Double[] arr = new Double[2];
        arr[0] = z * Math.cos(theta) + 0.0065;
        arr[1] = z * Math.sin(theta) + 0.006;
        return arr;
    }
    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:02
     * <h3>UpdateTime</h3> 2016/6/27,13:02
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param packageName 包名  百度的包名为 com.baidu.BaiduMap，高德com.autonavi.minimap,腾讯
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }




    public static void startNative_Gaodenew(Context context, String dlat, String dlon, String t) {
        String locationStr = "androidamap://route?sourceApplication="
                + "CloudPatient"
                +
                "&slat="
                + ""
                + "&slon="
                + ""
                +
                "&sname="
                + ""
                + "&dlat="
                + dlat
                + "&dlon="
                + dlon
                + "&dname="
                + ""
                + "&dev="
                + "1"
                + "&m="
                + "0"
                + "&t="
                + t
                + "&showType="
                + "1";
        Intent intent = new Intent("android.intent.action.VIEW",
                Uri.parse(/*stringBuffer.toString()*/locationStr));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }
}
