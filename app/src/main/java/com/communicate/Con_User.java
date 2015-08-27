package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.model.Account;
import com.model.User;

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

/**
 * g
 * Created by 扬洋 on 2015/8/8.
 */
public class Con_User extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_USERINFO = "/direct/user_androidUserScore";
    private static final String URL_RANK_TOP = "/direct/user_top";
    private static final String URL_RANK_BOTTOM = "/direct/user_bottom";

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
    public void getUserRank(final boolean isTop){
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    if (isTop) {
                        initCon_Post(URL_RANK_TOP);
                    }
                    else{
                        initCon_Post(URL_RANK_BOTTOM);
                    }

                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                JSONArray rows;
                int total;
                List<User> dataList_user = new ArrayList<>();
                try {
                    rows = new JSONObject(result).getJSONArray("rows");
                    total = new JSONObject(result).getInt("total");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject jsonObject = (JSONObject) rows.opt(i);
                        User user = new User();
                        try {
                            user.setData(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }
                        dataList_user.add(user);
                    }
                } catch (JSONException e) {
                    result = "数据转换失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("total", total);
                data.putParcelableArrayList("rows", (ArrayList<? extends Parcelable>) dataList_user);
                data.putString("method", "user_rank_list");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

}
