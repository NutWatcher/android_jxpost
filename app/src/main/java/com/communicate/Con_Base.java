package com.communicate;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by 扬洋 on 2015/8/1.
 */
public class Con_Base {
    private HttpURLConnection conn;
    private String resultData = "";
    private URL url = null;
    private String name;
    private String con_url;

    private static final String NAME = "MyApplication";
    private static final String CON_URL = "http://10.140.0.42:8080/HuResources";

    Con_Base() {
        setName(NAME);
        setCon_url(CON_URL);


    }

    public String getCon_url() {
        return con_url;
    }

    public void setCon_url(String con_url) {
        this.con_url = con_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initCon_Post(String URL) {
        try {
            conn.disconnect();
            url = new URL(getCon_url() + URL);
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

    public void SetParams(Map<String, String> params) {
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
        return resultData;
    }


}