package com.communicate;

import android.util.Log;
import android.util.Xml;

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
import java.util.Map;

/**
 * Created by Administrator on 2015/7/22.
 */
public class Con_Account {


    private  HttpURLConnection conn ;
    private String resultData = "";
    private URL url = null;

    Con_Account(Map<String, String> params) {
        Init();
        SetParams(params);
    }
    //112
    private void Init() {
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

    }
}
