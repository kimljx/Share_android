package cn.share;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import cn.vipapps.AJAX;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;
import cn.vipapps.FSO;
import cn.vipapps.MESSAGE;
import cn.vipapps.STRING;
import cn.vipapps.android.ACTIVITY;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static cn.share.Common.PAGESIZE;

/**
 * Created by luo on 2017/2/20.
 */

public class RestBLL {


    //1登录
    public static void login( String name, String password, final CALLBACK<JSONObject> callback) {

        if (STRING.empty(name)) {
            DIALOG.alert("请输入用户名！");
            return;
        }
        if (STRING.empty(password)) {
            DIALOG.alert("请输入密码！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);

        PGAJAX.getJSON("login", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, final JSONObject result) {
                if (isError) {
                    return;
                }
                JSONObject user = result.optJSONArray("data").optJSONObject(0);
                CONFIG.setJSON(Common.CONFIG_USER, user);

                String register_id = JPushInterface.getRegistrationID(ACTIVITY.context);
                String client_type = "Android";
//                set_user_register_info(register_id, client_type, new CALLBACK<JSONObject>() {
//                    @Override
//                    public void run(boolean isError, JSONObject message) {
//                        callback.run(false, result);
//                    }
//                });
                callback.run(false, result.optJSONObject("data"));

            }
        });
    }

    //2注册
    public static void register( String name, String password, final CALLBACK<JSONObject> callback) {

        if (STRING.empty(name)) {
            DIALOG.alert("请输入用户名！");
            return;
        }
        if (STRING.empty(password)) {
            DIALOG.alert("请输入密码！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("password", password);
        PGAJAX.getJSON("register", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, final JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);

            }
        });
    }

    //3退出登录
    public static void logout(final CALLBACK<JSONObject> callback) {
        Map<String, Object> param = new HashMap<>();

        PGAJAX.getJSON("logout", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, final JSONObject result) {

                if (isError) {
                    return;
                }
                CONFIG.set(Common.CONFIG_TOKEN, null);
                CONFIG.setJSON(Common.CONFIG_USER, null);
                MESSAGE.send(Common.MSG_LOGIN, null);
                callback.run(false, result);


            }
        });

    }

    //4我的信息
    public static void my(final CALLBACK<JSONObject> callback) {
        Map<String, Object> param = new HashMap<>();

        PGAJAX.getJSON("my", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONObject("data"));
            }
        });
    }

    //用户的信息
    public static void user(String userId,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(userId)) {
            DIALOG.alert("请选择用户！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId",userId);
        PGAJAX.getJSON("my", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONObject("data"));
            }
        });
    }

    //5修改性别
    public static void updateSex(String sex,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(sex)) {
            DIALOG.alert("请选择用户！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("sex",sex);
        PGAJAX.getJSON("updateSex", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    //6修改密码
    public static void changePassword(String oldPassword, String newPassword, String againPassword, final CALLBACK<JSONObject> callback) {
        if (STRING.empty(oldPassword)) {
            DIALOG.alert("请输入旧密码！");
            return;
        }
        if (STRING.empty(newPassword)) {
            DIALOG.alert("请输入新密码！");
            return;
        }
        if (STRING.empty(againPassword)) {
            DIALOG.alert("请再次输入新密码！");
            return;
        }
        if (!newPassword.equals(againPassword)) {
            DIALOG.alert("两次密码不同！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("oldPassword", oldPassword);
        param.put("newPassword", newPassword);
        PGAJAX.getJSON("changePassword", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    //7修改头像
    public static void uploadAvatar(Bitmap avatar, final CALLBACK<JSONObject> callback) {

        Map<String, Object> param = new HashMap<>();
        File path = ACTIVITY.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file=new File("avatar.png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            avatar.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        param.put("file",file);
        PGAJAX.getJSON("updateSex", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONObject("data"));
            }
        });
    }


    //8分享消息列表
    public static void message_list(final CALLBACK<JSONArray> callback) {


        Map<String, Object> params = new HashMap<>();

        PGAJAX.getJSON("message_list", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    //9新增消息分享
    public static void addMessage(String oldPassword, String newPassword, String againPassword, final CALLBACK<JSONObject> callback) {
        if (STRING.empty(oldPassword)) {
            DIALOG.alert("请输入旧密码！");
            return;
        }
        if (STRING.empty(newPassword)) {
            DIALOG.alert("请输入新密码！");
            return;
        }
        if (STRING.empty(againPassword)) {
            DIALOG.alert("请再次输入新密码！");
            return;
        }
        if (!newPassword.equals(againPassword)) {
            DIALOG.alert("两次密码不同！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("oldPassword", oldPassword);
        param.put("newPassword", newPassword);
        PGAJAX.getJSON("addMessage", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

}
