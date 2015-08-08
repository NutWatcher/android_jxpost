package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * g
 * Created by 扬洋 on 2015/8/8.
 */
public class Con_User extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_USERINFO = "/direct/user_androidUserScore";

    private HttpURLConnection conn;
    private String resultData = "";
    private URL url = null;

    public Con_User(Handler handler) {
        this.handler = handler;
    }

    private void setFailed(String result) {
        Log.i("con_login", "连接服务器出错");
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("result", result);
        data.putString("method", "con_failed");
        msg.setData(data);
        handler.sendMessage(msg);
    }

    public void getUserinfo(final int userId) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    params.put("id", String.valueOf(userId));
                    initCon_Post(URL_USERINFO);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                User user = new User();
                try {
                    user.setData(new JSONObject(result));
                } catch (JSONException e) {
                    result = "数据转换失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putParcelable("user", user);
                data.putString("method", "userInfo");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

}
