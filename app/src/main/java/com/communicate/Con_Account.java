package com.communicate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.model.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * g
 * Created by Administrator on 2015/7/22.
 */
public class Con_Account extends Con_Base {
    private Map<String, String> params;
    private String result;
    private Handler handler;
    private static final String URL_USERACCOUNT = "/view/userIdMap_androidSingleMapOnPagingView";

    private  HttpURLConnection conn ;
    private String resultData = "";
    private URL url = null;

    public Con_Account(Handler handler) {
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

    public void getAccountList(final int userId, final int start, final int limit) {
        class downloadApkThread extends Thread {
            @Override
            public void run() {
                try {
                    params = new HashMap<>();
                    params.put("id", String.valueOf(userId));
                    params.put("start", String.valueOf(start));
                    params.put("limit", String.valueOf(limit));
                    initCon_Post(URL_USERACCOUNT);
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
                List<Account> dataList_account = new ArrayList<>();
                try {
                    rows = new JSONObject(result).getJSONArray("rows");
                    total = new JSONObject(result).getInt("total");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject jsonObject = (JSONObject) rows.opt(i);
                        Account account = new Account();
                        try {
                            account.setData(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            continue;
                        }
                        dataList_account.add(account);
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
                data.putParcelableArrayList("rows", (ArrayList<? extends Parcelable>) dataList_account);
                data.putString("method", "user_account");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new downloadApkThread().start();
    }

    //112
   /* private void Init() {
        try {
            url = new URL("http://10.140.0.42:8080/HuResources/view/user_comboByNameKey");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);// Post请求不能使用缓存
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
            conn.setRequestProperty("Content - Type", "application / x - www - form - urlencoded");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SetParams(Map<String, String> params) {
        try {
            String content = "";
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            for (String key : params.keySet()) {
                content += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode( params.get(key), "UTF-8") ;
                Log.i("Map","Key = " + key + ", Value = " + params.get(key));
            }
            dos.writeBytes(content);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDate() throws IOException {

        resultData = "";
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferReader = new BufferedReader(isr);
        String inputLine = "";
        while ((inputLine = bufferReader.readLine()) != null) {
            resultData += inputLine + "\n";
        }
        isr.close();
        conn.disconnect();

        Log.i("con", resultData);
        return resultData ;

    }*/
}
