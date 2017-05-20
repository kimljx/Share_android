package cn.share;

import android.content.Context;
import android.content.pm.PackageManager;

import cn.vipapps.CONFIG;

//公共类
public class Common {
    //云端
    public static final String URL_BASE = "http://120.25.202.192:80/share";
    //局域端
//    public static final String URL_BASE = "http://192.168.1.106:8080";
    //不用读取TOKEN的API
    public static final String[] APIS_GUEST =
            new String[]{"login","register","user"
                    ,"commentList","collectList","message"};



    //要设置（改变）TOKEN的API
    public static String[] APIS_TOKEN = new String[]{"login"};
    ////////////////////////////// CONFIG /////////////////////////////
    public static final String CONFIG_TOKEN = "TOKEN";
    public static final String CONFIG_PUSH_MESSAGE = "PUSH_MESSAGE";
    public static String CONFIG_USER = "USER";
    public static String CONFIG_IMG = "IMG";
    ////////////////////////// MSG //////////////////////////////////////
    public static final String MSG_LOGIN = "LOGIN";
    public static String MSG_NOTIFICATION = "NOTIFICATION";
    public static final String MSG_MESSAGEREFESH = "MESSAGEREFESH";
    public static String MSG_IMG = "MSGIMG";
    public static String MSG_HOMELIST = "HOMELIST";
    public static String MSG_COMMENT = "COMMENT";
    public static String MSG_SHARE = "SHARE";
    public static String MSG_HASLOGIN = "HASLOGIN";


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


//修改性别
    public static String leaveType(String num) {
        String type;
        switch (num) {
            case "1":
                type = "男";
                break;
            case "2":
                type = "女";
                break;
            case "3":
                type = "保密";
                break;
            default:
                type = "保密";
                break;

        }
        return type;
    }
}
