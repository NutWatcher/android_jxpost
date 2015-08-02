package com.tool;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by 扬洋 on 2015/8/2.
 */
public class MyThread extends Thread {
    private Class onwClass;
    private Object o;
    private Map<String, String> params;
    private String name;
    private String method;

    public MyThread(String class_name, String method, Map<String, String> map) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        onwClass = Class.forName(class_name);
        o = onwClass.newInstance();
        params = map;
        this.method = method;
    }

    public void run() {
    }
}
