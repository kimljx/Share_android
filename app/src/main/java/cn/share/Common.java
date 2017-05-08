package cn.share;

import android.content.Context;
import android.content.pm.PackageManager;

import cn.vipapps.CONFIG;
import cn.vipapps.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static final String URL_BASE = "http://120.25.202.192:80/share";

    //不用读取TOKEN的API
    public static final String[] APIS_GUEST =
            new String[]{"login","register","user"
                    ,"commentList","collectList"};


    //要设置（改变）TOKEN的API
    public static String[] APIS_TOKEN = new String[]{"login"};
    ////////////////////////////// CONFIG /////////////////////////////
    public static final String CONFIG_TOKEN = "TOKEN";
    public static final String CONFIG_PROFILE = "PROFILE";
    public static final String CONFIG_PUSH_MESSAGE = "PUSH_MESSAGE";
    public static final String CONFIG_PUSH_EXTRA = "PUSH_EXTRA";
//    public static String CONFIG_QRCODE = "QRCODE";
//    public static String CONFIG_EQUIPMENT = "EQUIPMENT";
    public static String CONFIG_USER = "USER";
    public static final String CONFIG_DEVICE_SN ="DEVICE_SN" ;

    ////////////////////////// MSG //////////////////////////////////////
    public static final String MSG_LOGIN = "LOGIN";
    public static final String MSG_CLEARIMG = "CLEARIMG";
    public static String MSG_FAULTREFESH = "FAULTREFESH";
    public static String MSG_REPAIRREFESH = "REPAIRREFESH";
    public static final String MSG_CHANGEBAR = "CHANGEBAR";
    public static final String MSG_LEAVELIST = "LEAVELIST";
    public static String MSG_NOTIFICATION = "NOTIFICATION";
    public static final String MSG_LEAVEADDREFESH = "LEAVEADDREFESH";
    public static final String MSG_MESSAGEREFESH = "MESSAGEREFESH";

    public static String MSG_HOMELIST = "HOMELIST";

    //全局常量
    public static final int PAGESIZE = 20;


    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }


    public static boolean isGuest() {
        return CONFIG.get(CONFIG_TOKEN) == null;
    }


    //判断用户是否拥有账号权限
    public static boolean hasUsers() {

        JSONObject profile = JSON.parse(CONFIG.get(CONFIG_PROFILE));
        JSONArray permisson = profile.optJSONArray("permissons");
        boolean role = JSON.contains(permisson, "users");

        return role;
    }

    public static String leaveType(String num) {
        String type;
        switch (num) {
            case "1":
                type = "病假";
                break;
            case "2":
                type = "事假";
                break;
            case "3":
                type = "婚假";
                break;
            case "4":
                type = "丧假";
                break;
            case "5":
                type = "产假";
                break;
            case "6":
                type = "年假";
                break;
            case "7":
                type = "工伤假";
                break;
            case "8":
                type = "公出假";
                break;
            default:
                type = "不详";
                break;

        }
        return type;
    }

    public static String approvalStatus(String num) {
        String status;
        switch (num) {
            case "0":
                status = "待审批";
                break;
            case "1":
                status = "同意";
                break;
            case "2":
                status = "拒绝";
                break;
            default:
                status = "待审批";
                break;
        }
        return status;
    }

    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }

    // strTime要转换的string类型的时间，formatType要转换的格式:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 比较两个日期之间的大小
     *
     * @param s1
     * @param s2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareStringDate(String s1, String s2) {
        Date d1 = stringToDate(s1);
        Date d2 = stringToDate(s2);
        return compareDate(d1, d2);
    }

}
