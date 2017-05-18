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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static cn.share.Common.PAGESIZE;
import static cn.share.Common.isGuest;

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
                JSONObject user = result.optJSONObject("data");
                CONFIG.setJSON(Common.CONFIG_USER, user);

                String register_id = JPushInterface.getRegistrationID(ACTIVITY.context);

                pushRegister(register_id, new CALLBACK<JSONObject>() {
                    @Override
                    public void run(boolean isError, JSONObject message) {

                        callback.run(false, result.optJSONObject("data"));                    }
                });
//                callback.run(false, result.optJSONObject("data"));
            }
        });
    }
    //2退出登录
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
                MESSAGE.send(Common.MSG_HASLOGIN,null);
                MESSAGE.send(Common.MSG_LOGIN, null);
                callback.run(false, result);


            }
        });

    }


    //3我的信息
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


    //4用户的信息
    public static void user(String userId,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(userId)) {
            DIALOG.alert("请选择用户！");
            return;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("userId",userId);
        PGAJAX.getJSON("user", param, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
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
    //7注册
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

    //8修改头像
    public static void uploadAvatar(Bitmap avatar, final CALLBACK<JSONObject> callback) {

        Map<String, Object> param = new HashMap<>();
        Map<String, InputStream> files;
        if (avatar != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
            final InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            files = new HashMap<String, InputStream>() {
                {
                    put("file", isBm);
                }
            };
        } else {
            files = null;
        }
        PGAJAX.upload("uploadAvatar", param, files,false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    /**
     * 9.	用户注册极光ID接口
     功能： 用户上传注册的极光ID
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/pushRegister

     请求参数	类型	是否必须	描述
     token	string	是	Token值
     registerId	String	是	客户端极光registerId

     * @param callback
     */
    public static void pushRegister(String registerId ,final CALLBACK<JSONObject> callback) {


        Map<String, Object> params = new HashMap<>();
        params.put("registerId",registerId);
        PGAJAX.getJSON("pushRegister", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    /**
     * 10.	通知列表接口
     功能：用于获取登录用户通知列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/notificationList


     请求参数	类型	是否必须	描述
     token	string	是	Token值
     isread	Int 例如0	是	0-未读，1-已读

     * @param callback
     */
    public static void notificationList(String isread,final CALLBACK<JSONArray> callback) {


        Map<String, Object> params = new HashMap<>();
        params.put("isread",isread);
        PGAJAX.getJSON("notificationList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }


    /**
     * 11.	通知详情接口
     功能： 用于获取通知详情接口
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/notification

     请求参数	类型	是否必须	描述
     notificationId	String	是	通知id
     token	string	是	Token值

     * @param callback
     */
    public static void notification(String notificationId, final CALLBACK<JSONObject> callback) {


        Map<String, Object> params = new HashMap<>();
        params.put("notificationId",notificationId);
        PGAJAX.getJSON("notification", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONObject("data"));
            }
        });
    }



    /**
     *
     * 12.	添加分享接口
     功能： 用于添加分享
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/addMessage

     请求参数	类型	是否必须	描述
     token	string	是	Token值
     messageInfo	String	是	分享内容
     file	File	否	分享图片（一张）

     * @param callback
     */

    public static void addMessage( String messageInfo, Bitmap img, final CALLBACK<JSONObject> callback) {

        if (STRING.empty(messageInfo)) {
            DIALOG.alert("请输入分享内容！");
            return;
        }

        Map<String, Object> param = new HashMap<>();
        param.put("messageInfo", messageInfo);
        Map<String, InputStream> files;
        if (img != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, baos);
            final InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            files = new HashMap<String, InputStream>() {
                {
                    put("file", isBm);
                }
            };
        } else {
            files = null;
        }
        PGAJAX.upload("addMessage", param,files, false, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {
                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    //13.	所有分享列表接口列表
    //token	string	否	Token值
    public static void messageList(final CALLBACK<JSONArray> callback) {


        Map<String, Object> params = new HashMap<>();

        PGAJAX.getJSON("messageList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                Log.e( "run: ", result.optJSONArray("data")+"");
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     * 14.	登录用户评论过的消息列表接口：
     功能：用于获取登录用户评论过的消息列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/myCommentList

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void myCommentList(final CALLBACK<JSONArray> callback) {

        Map<String, Object> params = new HashMap<>();

        PGAJAX.getJSON("myCommentList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     * 15.	消息的评论列表接口
     功能：用于获取消息的评论列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/commentList

     请求参数	类型	是否必须	描述
     messageId	string	是	分享的消息id
     */
    public static void commentList(String messageId,final CALLBACK<JSONArray> callback) {
        if (STRING.empty(messageId)) {
            DIALOG.alert("消息Id为空！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("messageId",messageId);
        PGAJAX.getJSON("commentList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     * 16.	添加评论接口
     功能： 用于添加评论
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/addComment

     请求参数	类型	是否必须	描述
     token	string	是	Token值
     commentInfo	String	是	评论内容
     messageId	string	是	分享的消息id

     */

    public static void addComment(String messageId,String commentInfo,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(messageId)) {
            DIALOG.alert("消息Id为空！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("messageId",messageId);
        params.put("commentInfo",commentInfo);

        PGAJAX.getJSON("addComment", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    /**
     * 17.	收藏接口
     功能：用于收藏/取消收藏。
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/collect

     请求参数	类型	是否必须	描述
     token	string	是	Token值
     messageId	string	是	分享的消息id
     */

    public static void collect(String messageId,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(messageId)) {
            DIALOG.alert("消息Id为空！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("messageId",messageId);

        PGAJAX.getJSON("collect", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    /**
     * 18.	收藏列表接口
     功能： 用于收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/collectList

     请求参数	类型	是否必须	描述
     userId	string	是	用户id

     */

    public static void collectList(String userId,final CALLBACK<JSONArray> callback) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);

        PGAJAX.getJSON("collectList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     *19.	我的收藏列表接口：
     功能：用于获取我的收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/myCollectList

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void myCollectList(final CALLBACK<JSONArray> callback) {

        Map<String, Object> params = new HashMap<>();
        PGAJAX.getJSON("myCollectList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     *20.	单条分享列表接口：
     功能：用于获取我的收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/message

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void message(String messageId,final CALLBACK<JSONObject> callback) {
        if (STRING.empty(messageId)) {
            DIALOG.alert("消息Id为空！");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("messageId",messageId);
        PGAJAX.getJSON("message", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONObject("data"));
            }
        });
    }

    /**
     *21.	我的分享列表接口：
     功能：用于获取我的收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/myCollectList

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void myMessageList(final CALLBACK<JSONArray> callback) {

        Map<String, Object> params = new HashMap<>();
        PGAJAX.getJSON("myMessageList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }

    /**
     *22.	用户分享列表接口：
     功能：用于获取我的收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/myCollectList

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void userMessageList(String userId,final CALLBACK<JSONArray> callback) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        PGAJAX.getJSON("userMessageList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result.optJSONArray("data"));
            }
        });
    }


    /**
     *22.	删除分享列表接口：
     功能：用于获取我的收藏列表
     HTTP请求方式: POST
     URL：	http://120.25.202.192:80/share/rest/myCollectList

     请求参数	类型	是否必须	描述
     token	string	是	Token值

     */
    public static void delMessageList(String messages, final CALLBACK<JSONObject> callback) {

        Map<String, Object> params = new HashMap<>();
        params.put("messages",messages);
        Log.e( "delMessageList: ", params+"");
        PGAJAX.getJSON("delMessageList", params, true, AJAX.Mode.POST, new CALLBACK<JSONObject>() {
            @Override
            public void run(boolean isError, JSONObject result) {

                if (isError) {
                    return;
                }
                callback.run(false, result);
            }
        });
    }

    /**
     * 保存分享的图片
     * 将Bitmap转换成文件
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm,String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path , fileName);
        if (FSO.exists(path,fileName)){

        }else {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }

        return myCaptureFile;
    }

}
