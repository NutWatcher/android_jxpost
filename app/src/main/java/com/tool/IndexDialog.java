package com.tool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapplication.R;

/**
 * Created by 扬洋 on 2015/8/16.
 */
public class IndexDialog extends Dialog {

    private TextView tv;
    private WebView myWebView;

    public IndexDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    private IndexDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tools_dialog_index);
        myWebView = (WebView) findViewById(R.id.index_dialog_webview);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                myWebView.clearCache(true);
                myWebView.loadUrl("http://192.168.1.101:3000/");
            }
        });

        // tv = (TextView) this.findViewById(R.id.tv);
        // tv.setText("正在连接...");
    }
}