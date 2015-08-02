package com.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import com.communicate.Con_UpdataXml;
import com.fragment.Fragment_About;
import com.fragment.Fragment_Setting;
import java.util.HashMap;

public class Activity_Setting extends FragmentActivity implements
        Fragment_Setting.OnFragmentInteractionListener,
        Fragment_About.OnFragmentInteractionListener {

    private boolean isIndexView;
    private TextView tv_logout;
    private TextView tv_about;
    private TextView tv_new_version;
    private Context mContext;
    private TextView tv_title;

    ImageButton imageButton_Back;
    ImageButton imageButton_Search;

    Fragment_Setting fragment_setting;
    Fragment_About fragment_about;
    FragmentManager fragmentManager;

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
        tv_title = (TextView) findViewById(R.id.title_text);
        fragment_setting = new Fragment_Setting();
        fragment_about = new Fragment_About();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_setting);
        fragmentTransaction.commit();
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        imageButton_Search = (ImageButton) findViewById(R.id.title_imageButton_search);
    }

    private void initData() {
        isIndexView = true;
        tv_title.setText("设置");
        imageButton_Search.setVisibility(View.GONE);
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
        } else {
            // switchContent();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onTitleBackButtonClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isUpdate() {
        int versionCode = getVersionCode(mContext);
        Con_UpdataXml con_updataXml = new Con_UpdataXml();
        HashMap<String, String> mHashMap = con_updataXml.getXml();
        if (null != mHashMap) {
            int serviceCode = Integer.valueOf(mHashMap.get("version"));
            // 版本判断
            if (serviceCode > versionCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo("com.jxpostt", 0).versionCode;
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
}
