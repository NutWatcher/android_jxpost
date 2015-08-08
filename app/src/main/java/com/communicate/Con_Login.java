package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * g
 * Created by 扬洋 on 2015/8/1.
 */
public class Con_Login extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private JSONObject jsonObject;
    private static final String URL_USERLIST = "/view/user_comboByNameKey";
    private static final String URL_LOGIN = "/direct/user_androidLogin";

    public Con_Login(Handler handler) {
        this.handler = handler;
    }

    private void setFailed() {
        Log.i("con_login", "链接服务器出错");
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("result", result);
        data.putString("method", "con_failed");
        msg.setData(data);
        handler.sendMessage(msg);
    }
    public void getUserList(final String name) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    params.put("q", name);
                    initCon_Post(URL_USERLIST);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = null;
                    setFailed();
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("method", "userList");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

    public void checkPassword(final int userId, final String password) {
        class downloadApkThread1 extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    params.put("userId", String.valueOf(userId));
                    params.put("password", password);
                    initCon_Post(URL_LOGIN);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = null;
                    setFailed();
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                try {
                    jsonObject = new JSONObject(result);
                    boolean success = jsonObject.getBoolean("success");
                    String message = jsonObject.getString("message");
                    data.putBoolean("success", success);
                    data.putString("message", message);
                    data.putString("method", "checkPassword");
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    data.putBoolean("success", false);
                    data.putString("message", "登录错误!");
                    msg.setData(data);
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }

            }
        }
        new downloadApkThread1().start();
    }

}
