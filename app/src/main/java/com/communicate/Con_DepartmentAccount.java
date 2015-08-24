package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.model.Department;
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
 * Created by 扬洋 on 2015/7/30.
 */
public class Con_DepartmentAccount extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_DEPARTMENT_USER_ACCOUNT = "/direct/user_androidDepartmentScore";
    private static final String URL_DEPARTMENT_ACCOUNT = "/direct/branch_androidBranchInfo";

    private HttpURLConnection conn;
    private String resultData = "";
    private URL url = null;

    public Con_DepartmentAccount(Handler handler) {
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

    public void getDepartmentUserAccountList(final int departmentId) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    params.put("departmentId", String.valueOf(departmentId));
                    initCon_Post(URL_DEPARTMENT_USER_ACCOUNT);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                List<User> dataList_user = new ArrayList<>();
                JSONArray js_data;
                try {
                    js_data = new JSONObject(result).getJSONArray("rows");
                    for (int i = 0; i < js_data.length(); i++) {
                        JSONObject jsonObject = (JSONObject) js_data.opt(i);
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
                data.putParcelableArrayList("rows", (ArrayList<? extends Parcelable>) dataList_user);
                data.putString("method", "department_user_list");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

    public void getDepartmentAccountList() {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    initCon_Post(URL_DEPARTMENT_ACCOUNT);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                List<Department> dataList_department = new ArrayList<>();
                JSONArray js_data;
                try {
                    js_data = new JSONObject(result).getJSONArray("rows");
                    for (int i = 0; i < js_data.length(); i++) {
                        JSONObject jsonObject = (JSONObject) js_data.opt(i);
                        Department department = new Department();
                        try {
                            department.setData(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }
                        dataList_department.add(department);
                    }
                } catch (JSONException e) {
                    result = "数据转换失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putParcelableArrayList("rows", (ArrayList<? extends Parcelable>) dataList_department);
                data.putString("method", "department_list");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }
}

