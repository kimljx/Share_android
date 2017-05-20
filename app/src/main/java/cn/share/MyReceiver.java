package cn.share;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.share.phone.SplashActivity;
import cn.vipapps.CONFIG;
import cn.vipapps.MESSAGE;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 * 接收极光推送推送下来的详细内容
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();


        try {
            if (bundle != null) {
                MESSAGE.send(Common.MSG_NOTIFICATION, null);
//			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
                printBundle(bundle);
                if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                    Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                    // send the Registration Id to your server...

                } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                    Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));


                } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                    Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                    int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notificationId);

                } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

                    Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
//                    1:程序在前台运行
//                    2:程序在后台运行
//                    3:程序未启动
                    //点击通知后唤醒APP
                    switch (getAppSatus(context, "cn.share")) {
                        case 1:
                            Log.e("onReceiver  1", "the ");
//                            MESSAGE.send(Common.MSG_PUSH, null);
                            break;
                        case 2:
                            Log.e("onReceiver  2", "the app process is alive");
                            final Intent mainIntent;
                            String token = CONFIG.getString(Common.CONFIG_TOKEN);
//                            if (token != null){
//                                mainIntent = new Intent(context, MainActivity.class);
//                            }else {
                                mainIntent = new Intent(context, SplashActivity.class);
//                            }
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            MESSAGE.send(Common.MSG_PUSH, null);
                            context.startActivity(mainIntent);

                            break;
                        case 3:
                            Log.e("onReceiver  3", "the app process is dead");
                            Intent launchIntent = context.getPackageManager().
                                    getLaunchIntentForPackage("cn.share");
                            launchIntent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                            MESSAGE.send(Common.MSG_PUSH, null);
                            context.startActivity(launchIntent);

                            break;
                        default:
                            Log.e("onReceiver   d", "the");
//                            MESSAGE.send(Common.MSG_PUSH, null);
                            break;
                    }


                    // 打开自定义的Activity
                    // Intent i = new Intent(context, TestActivity.class);
                    // i.putExtras(bundle);
                    // // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    // Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // context.startActivity(i);

                } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                    Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
                    // 打开一个网页等..

                } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                } else {
                    Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                }
            }
        } catch (Exception e) {

        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();

        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));

            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        CONFIG.set(Common.CONFIG_PUSH_MESSAGE, bundle.getString(JPushInterface.EXTRA_ALERT));
        return sb.toString();
    }



    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public int getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }
}
