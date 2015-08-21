package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

import com.model.Org;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/21.
 * dd
 */
public class Con_Org extends Con_Base{
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_ORG_BALANCE = "/direct/branch_androidBranchInfo";

    public Con_Org(Handler handler) {
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

    public void getOrgBalanceList() {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    initCon_Post(URL_ORG_BALANCE);
                    SetParams(params);
                    result = getDate();
                } catch (IOException e) {
                    result = "网络连接失败";
                    setFailed(result);
                    e.printStackTrace();
                    return;
                }
                List<Org> dataList_department = new ArrayList<>();
                JSONArray js_data;
                try {
                    js_data = new JSONArray(result);
                    for (int i = 0; i < js_data.length(); i++) {
                        JSONObject jsonObject = (JSONObject) js_data.opt(i);
                        Org org = new Org();
                        try {
                            org.setData(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }
                        dataList_department.add(org);
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