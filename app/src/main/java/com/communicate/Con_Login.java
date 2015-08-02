package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 扬洋 on 2015/8/1.
 */
public class Con_Login extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_USERLIST = "/userlist";
    private static final String URL_LOGIN = "/login";

    public Con_Login(Handler handler) {
        this.handler = handler;
    }

    public void getUserList(final String name) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                params = new HashMap<>();
                params.put("q", name);
                initCon_Post(URL_USERLIST);
                SetParams(params);
                try {
                    result = getDate();
                } catch (IOException e) {
                    result = null;
                    e.printStackTrace();
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
                params = new HashMap<>();
                params.put("userId", String.valueOf(userId));
                params.put("password", password);
                initCon_Post(URL_LOGIN);
                SetParams(params);
                try {
                    result = getDate();
                } catch (IOException e) {
                    result = null;
                    e.printStackTrace();
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("result", result);
                data.putString("method", "checkPassword");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread1().start();
    }

}
