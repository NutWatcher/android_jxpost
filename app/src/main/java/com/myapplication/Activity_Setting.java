package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_Updata;
import com.communicate.Con_UpdataXml;
import com.fragment.Fragment_About;
import com.fragment.Fragment_Setting;
import com.model.Account;
import com.tool.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Activity_Setting extends FragmentActivity implements
        Fragment_Setting.OnFragmentInteractionListener,
        Fragment_About.OnFragmentInteractionListener {

    private boolean isIndexView;
    private TextView tv_logout;
    private TextView tv_about;
    private TextView tv_new_version;
    private Context mContext;
    private TextView tv_title;
    private LoadingDialog loadingDialog ;

    ImageButton imageButton_Back;
    ImageButton imageButton_Setting;

    Fragment_Setting fragment_setting;
    Fragment_About fragment_about;
    FragmentManager fragmentManager;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (loadingDialog.isShowing()) {
                loadingDialog.hide();
            }
            if (method.equals("con_failed")) {
                String result = data.getString("result");
                if (result == null) {
                    result = "网络连接失败";
                }
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            }
            else if (method.equals("app_version")) {
                isUpdate(data);
            }
            else if (method.equals("app_not_updata")) {
                Toast.makeText(mContext, "当前已经是最新版本！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    public Activity_Setting() {
        this.mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity__setting);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        loadingDialog = new LoadingDialog(this);
        tv_title = (TextView) findViewById(R.id.title_text);
        fragment_setting = new Fragment_Setting();
        fragment_about = new Fragment_About();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_setting);
        fragmentTransaction.commit();
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        imageButton_Setting = (ImageButton) findViewById(R.id.title_imageButton_setting);
    }

    private void initData() {
        isIndexView = true;
        tv_title.setText("设置");
        imageButton_Setting.setVisibility(View.INVISIBLE);
    }

    private void initEvent() {
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleBackButtonClick();
            }
        });
    }

    private void onTitleBackButtonClick() {
        if (isIndexView) {
            Activity_Setting.this.setResult(RESULT_OK);
            Activity_Setting.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        onTitleBackButtonClick();
        // super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void isUpdate(final Bundle data) {
        class checkVersionThread extends Thread {
            @Override
            public void run() {
                Double new_version = data.getDouble("version");
                String url = data.getString("url");
                int versionCode = getVersionCode(mContext);

                if (new_version > versionCode) {
                    Uri uri = Uri.parse(url);
                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                    it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    startActivity(it);
                }
                else{
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("method", "app_not_updata");
                    msg.setData(data);
                    handler.sendMessage(msg);

                }
            }
        }
        new checkVersionThread().start();
    }

    /**
     * 获取软件版本号
     *
     *
     *
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.myapplication", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLogout() {
        Log.i("setting", "logout");
        SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("userId", "0");
        editor.commit();
        Activity_Setting.this.setResult(RESULT_OK);
        Activity_Setting.this.finish();
    }

    @Override
    protected void onDestroy() {
        loadingDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onNewVersion() {
        loadingDialog.show();
        Con_Updata con_updata = new Con_Updata(handler);
        con_updata.getAppVersion();
    }
}
