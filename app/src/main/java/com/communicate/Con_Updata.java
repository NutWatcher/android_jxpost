package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Con_Updata extends Con_Base{
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_USERACCOUNT = "/direct/param_androidSystemInfo";

    private HttpURLConnection conn ;
    private String resultData = "";
    private URL url = null;

    public Con_Updata(Handler handler) {
        this.handler = handler;
    }

    private void setFailed(String result) {
        Log.i("con_login", "连接服务器出错：" + result);
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("result", result);
        data.putString("method", "con_failed");
        msg.setData(data);
        handler.sendMessage(msg);
    }

    public void getAccountList(final int userId, final int start, final int limit) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    initCon_Post(URL_USERACCOUNT);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                Double version;
                String url = "";
                try {
                    version = new JSONObject(result).getDouble("version");
                    url = new JSONObject(result).getString("url");
                } catch (JSONException e) {
                    result = "数据转换失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                version = 0.1;
                data.putDouble("version", version);
                data.putString("url", url);
                data.putString("method", "app_version");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

}
