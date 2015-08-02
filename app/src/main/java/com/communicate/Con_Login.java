package com.communicate;

import android.util.Log;

import com.myapplication.MyApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 扬洋 on 2015/8/1.
 */
public class Con_Login extends Con_Base {
    private Map<String, String> params;
    private String result;
    private static final String URL_USERLIST = "/HuResources";
    private static final String URL_LOGIN = "/login";

    public Con_Login() {
    }

    public String getUserList(String name) {
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
        return result;
    }

    public String checkPassword(int userId, String password) {
        params = new HashMap<>();
        params.put("userId", String.valueOf(userId));
        params.put("password", password);
        initCon_Post(URL_USERLIST);
        SetParams(params);
        try {
            result = getDate();
        } catch (IOException e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }
}