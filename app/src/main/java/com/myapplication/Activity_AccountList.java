package com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_Account;
import com.db.DB_Account;
import com.fragment.Fragment_AccountClearList;
import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.model.Account;
import com.tool.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class Activity_AccountList extends FragmentActivity
        implements
        Fragment_AccountList.OnFragmentInteractionListener,
        Fragment_AccountInfo.OnFragmentInteractionListener,
        Fragment_AccountClearList.OnFragmentInteractionListener {

    private Context mContext;
    private int userId;
    private int whichFragmentShow = 0;
    private boolean isIndexView ;
    private LoadingDialog dialog;
    private DB_Account dbHelper;

    TextView title_text;
    ImageButton imageButton_Back ;
    ImageButton imageButton_Setting;
    FragmentManager fragmentManager ;
    Fragment_AccountList fragment_accountList ;
    Fragment_AccountInfo fragment_accountInfo ;
    Fragment_AccountClearList fragment_accountClearList;
    Con_Account con_account;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("login handler", String.valueOf(userId));
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (dialog.isShowing()) {
                dialog.hide();
            }
            if (method.equals("con_failed")) {
                String result = data.getString("result");
                if (result == null) {
                    result = "网络连接失败";
                }
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                return;
            }
            if (method.equals("user_account")) {
                List<Account> rows = data.getParcelableArrayList("rows");
                int total = data.getInt("total");
                fragment_accountList.setViewData(data, total);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_account_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);

        Intent intent = getIntent();
        String value = intent.getStringExtra("fragment");
        Log.i("Activity_Account", "value++++++++++++++" + value);

        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        dialog = new LoadingDialog(this);
        fragment_accountInfo = new Fragment_AccountInfo();
        fragment_accountList = new Fragment_AccountList();
        fragment_accountClearList = new Fragment_AccountClearList();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_accountList);
        fragmentTransaction.commit();
        title_text = (TextView) findViewById(R.id.title_text);
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        imageButton_Setting = (ImageButton) findViewById(R.id.title_imageButton_setting);
        dbHelper = new DB_Account(this);
    }
    private  void initData(){
        mContext = this;
        isIndexView = true ;
        con_account = new Con_Account(handler);
        SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        userId = Integer.parseInt(sharedPreferences.getString("userId", "0"));
        if (userId == 0) {
            Activity_AccountList.this.setResult(100);
            Activity_AccountList.this.finish();
        }
        title_text.setText("账户列表");
        Con_Account con_account = new Con_Account(handler);
    }

    private void initEvent(){
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleBackButtonClick();
            }
        });
        imageButton_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleSearchButtonClick();
            }
        });
    }

    public void switchContent(Fragment hide, Fragment show) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.my_slide_in_right, R.anim.abc_fade_out);
        if (!show.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(hide).add(R.id.frame, show).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(hide).show(show).commit(); // 隐藏当前的fragment，显示下一个
        }
    }
    public void switchContent() {
    }

    private void onTitleBackButtonClick() {
        if (whichFragmentShow == 0) {
            Activity_AccountList.this.setResult(RESULT_OK);
            Activity_AccountList.this.finish();
        } else if (whichFragmentShow == 1) {
            switchContent(fragment_accountInfo, fragment_accountList);
            title_text.setText("账户列表");
            whichFragmentShow = 0;
        } else if (whichFragmentShow == 2) {
            switchContent(fragment_accountClearList, fragment_accountList);
            title_text.setText("账户列表");
            whichFragmentShow = 0;
        }
        /*if (fragment_accountSetting.isAdded()) {    // 先判断是否被add过
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment_accountSetting).commit(); // 隐藏当前的fragment，显示下一个
        } else if (isIndexView) {
            Activity_AccountList.this.setResult(RESULT_OK);
            Activity_AccountList.this.finish();
        }
        else{
            switchContent();
        }*/
    }

    private void onCLearAccountButtonClick() {
        //if (fragment_accountClearList.is)
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment_accountClearList.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(fragment_accountInfo).add(R.id.frame, fragment_accountClearList).commit(); //
        } else {
            fragmentTransaction.hide(fragment_accountInfo).show(fragment_accountClearList).commit();  // 隐藏当前的fragment，显示下一个
        }
        whichFragmentShow = 2;
        title_text.setText("账户销户列表");
    }
    private void onTitleSearchButtonClick() {
        openOptionsMenu();
        /*android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment_accountSetting.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.add(R.id.frame_search, fragment_accountSetting).commit();
        } else {
            fragmentTransaction.remove(fragment_accountSetting).commit(); // 隐藏当前的fragment，显示下一个
        }*/
    }

    @Override
    public void onBackPressed() {
        onTitleBackButtonClick();
        System.out.println("按下了back键   onBackPressed()");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__account_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i("acticity_account", "menu");
        onCLearAccountButtonClick();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onTableClearAccount_Clear() {
        class DeleteClearAccountThread extends Thread {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = "delete from Clear_Account";
                try {
                    db.execSQL(sql);
                } catch (SQLException e) {
                    Log.i("err", "update failed");
                    db.close();
                }
                db.close();
            }
        }
        new DeleteClearAccountThread().start();
        switchContent(fragment_accountClearList, fragment_accountList);
    }

  /*  @Override
    public void onFragmentSearchInteraction(Map<String, String> params) {
        onTitleSearchButtonClick();
        if (fragment_accountList != null) {
            fragment_accountList.onGetSearchData(params);
        }
    }*/

    @Override
    public void onFragmentAccountGetData(int start, int limit) {
        con_account.getAccountList(userId, start, limit);
    }

    @Override
    public void onFragmentAccountTouchItem(Account account) {
        Log.i("a_account_list", account.getCustomerName());
        isIndexView = false;
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.my_slide_in_right, R.anim.abc_fade_out);
      /*  if (fragment_accountSetting.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.remove(fragment_accountSetting); // 隐藏当前的fragment，显示下一个
        }*/
        if (!fragment_accountInfo.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(fragment_accountList).add(R.id.frame, fragment_accountInfo).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(fragment_accountList).show(fragment_accountInfo).commit(); // 隐藏当前的fragment，显示下一个
        }
        whichFragmentShow = 1;
        title_text.setText("账户信息");
        fragment_accountInfo.setData(account);
    }

}
