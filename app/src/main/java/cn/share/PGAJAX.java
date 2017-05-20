package cn.share;

import android.graphics.Bitmap;
import android.util.Log;

import cn.vipapps.AJAX;
import cn.vipapps.ARRAY;
import cn.vipapps.CALLBACK;
import cn.vipapps.CONFIG;
import cn.vipapps.DIALOG;
import cn.vipapps.MESSAGE;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//封装的网络通信类
@SuppressWarnings("deprecation")
public class PGAJAX {
    static String getUrl(String method) {
        return Common.URL_BASE + "/rest/"+method;
    }



    public static void getString(final String method, Map<String, Object> params_, final boolean isSilence, AJAX.Mode mode,
                                 final CALLBACK<String> callback) {
        String url = getUrl(method);
        Map<String, Object> headers = new HashMap<String, Object>();
        if (!ARRAY.contains(Common.APIS_GUEST, method)) {
            headers.put("COOKIE", CONFIG.get(Common.CONFIG_TOKEN));
        }
        AJAX.setHeaders(headers);
        if (!isSilence) {
            DIALOG.loading();
        }
        Map<String, Object> params = params_;
        params.put("method", method);
        AJAX.getString(url, params, mode, new CALLBACK<String>() {

            @Override
            public void run(boolean isError, String string) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                if (ARRAY.contains(Common.APIS_TOKEN, method)) {
                    Header[] allHeaderFields = AJAX.HEADERS;
                    for (Header header : allHeaderFields) {
                        if (header.getName().equals("Set-Cookie")) {
                            CONFIG.set(Common.CONFIG_TOKEN, header.getValue());
                        }
                    }
                } else {

                }
                if (callback != null) {
                    callback.run(false, string);
                }

            }

        });
    }

    public static void getJSON(final String method, Map<String, Object> params, final boolean isSilence, AJAX.Mode mode,
                               final CALLBACK<JSONObject> callback) {

        String url = getUrl(method);
        if (!ARRAY.contains(Common.APIS_GUEST, method)) {
            params.put("token", CONFIG.get(Common.CONFIG_TOKEN));
        }
        if (!isSilence) {
            DIALOG.loading();
        }

        AJAX.getJSON(url, params, mode, new CALLBACK<JSONObject>() {

            @Override
            public void run(boolean isError, JSONObject json) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                if (callback != null) {
                    int code = json.optInt("code");

                    if (code != 0) {
                        String message = json.optString("msg");
                        Log.e("json: ", json+"");
                        if (code == 10000) {
                            DIALOG.alert(message, new CALLBACK<Object>() {
                                @Override
                                public void run(boolean isError, Object result) {
                                    MESSAGE.send(Common.MSG_LOGIN, null);
                                }
                            });
                        }else {
                            DIALOG.alert(message);
                        }
                        callback.run(true, json);
                    } else {
                        if (ARRAY.contains(Common.APIS_TOKEN, method)) {
                            String token = json.optJSONObject("data").optString("token");
                            CONFIG.set(Common.CONFIG_TOKEN, token);

                        } else {

                        }
                        callback.run(false, json);
                    }

                }

            }

        });
    }


    public static void upload(final String method, Map<String, Object> params_, Map<String, InputStream> files,
                              final boolean isSilence, AJAX.Mode mode, final CALLBACK<JSONObject> callback) {
        String url = getUrl(method);
//        Map<String, Object> headers = new HashMap<String, Object>();
        if (!ARRAY.contains(Common.APIS_GUEST, method)) {
            params_.put("token", CONFIG.get(Common.CONFIG_TOKEN));
        }
        if (!isSilence) {
            DIALOG.loading();
        }
        Map<String, Object> params = params_;
        AJAX.upload(url, params, files, mode, new CALLBACK<JSONObject>() {

            @Override
            public void run(boolean isError, JSONObject json) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                if (ARRAY.contains(Common.APIS_TOKEN, method)) {
                    Map<String, Object> allHeaderFields = AJAX.getHeaders();
                    String Set_Cookie = (String) allHeaderFields.get("Set-Cookie");
                    CONFIG.set(Common.CONFIG_TOKEN, Set_Cookie);
                } else {

                }
                if (callback != null) {
                    int code = json.optInt("code");

                    if (code != 0) {
                        String message = json.optString("msg");
                        Log.e("json: ", json+"");
                        if (code == 10000) {
                            DIALOG.alert(message, new CALLBACK<Object>() {
                                @Override
                                public void run(boolean isError, Object result) {
                                    MESSAGE.send(Common.MSG_LOGIN, null);
                                }
                            });
                        }else {
                            DIALOG.alert(message);
                        }
                        callback.run(true, json);
                    } else {
                        if (ARRAY.contains(Common.APIS_TOKEN, method)) {
                            String token = json.optJSONObject("data").optString("token");
                            CONFIG.set(Common.CONFIG_TOKEN, token);

                        } else {

                        }
                        callback.run(false, json);
                    }

                }

            }

        });
    }

    public static void getImage(String url, final boolean isSilence, final CALLBACK<Bitmap> callback) {
        if (!isSilence) {
            DIALOG.loading();
        }
//        url = String.format(getImgUrl() + url);
        AJAX.getImage(url, new CALLBACK<Bitmap>() {

            @Override
            public void run(boolean isError, Bitmap image) {
                if (!isSilence) {
                    DIALOG.done();

                }
                if (isError) {
                    callback.run(true, null);
                    return;
                }
                callback.run(false, image);

            }

        });
    }
}
