package com.communicate;

import com.tool.ParseXml;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by 扬洋 on 2015/8/2.
 */
public class Con_UpdataXml {
    private HashMap<String, String> mHashMap;

    public HashMap<String, String> getXml() {
        ParseXml service = new ParseXml();
        try {
            String path = "http://192.168.1.146:8080/picweb/xml/version.xml";
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            mHashMap = service.parseXml(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mHashMap;
    }
}
