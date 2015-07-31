package com.communicate;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by 扬洋 on 2015/7/30.
 */
public class Con_DepartmentAccount {
    private HttpURLConnection conn;
    private String resultData = "";
    private URL url = null;

    Con_DepartmentAccount(Map<String, String> params) {
        Init();
        SetParams(params);
    }

    //112
    private void Init() {

    }

    private void Connection(URL url) {
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
                content += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
                Log.i("Map", "Key = " + key + ", Value = " + params.get(key));
            }
            dos.writeBytes(content);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDepartmentListDate() throws IOException {
        Connection(new URL("http://10.140.0.42:8080/HuResources/view/user_comboByNameKey"));
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
        return resultData;
    }

    public String getDepartmentUserListDate() throws IOException {
        Connection(new URL("http://10.140.0.42:8080/HuResources/view/user_comboByNameKey"));
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
        return resultData;

    }
}

