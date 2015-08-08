package com.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_Account;
import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;
import com.model.Account;
import com.tool.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class Activity_AccountList extends FragmentActivity
        implements
        Fragment_AccountList.OnFragmentInteractionListener,
        Fragment_AccountInfo.OnFragmentInteractionListener,
        Fragment_AccountSearch.OnFragmentInteractionListener {

    private Context mContext;
    private int userId;
    private boolean isIndexView ;
    private LoadingDialog dialog;
    TextView title_text;
    ImageButton imageButton_Back ;
    ImageButton imageButton_Search ;
    FragmentManager fragmentManager ;
    Fragment_AccountList fragment_accountList ;
    Fragment_AccountInfo fragment_accountInfo ;
    Fragment_AccountSearch fragment_accountSearch;
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
                int total = 0;
                fragment_accountList.setViewData(rows);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_account_list);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        dialog = new LoadingDialog(this);
        fragment_accountInfo = new Fragment_AccountInfo();
        fragment_accountList = new Fragment_AccountList();
        fragment_accountSearch = new Fragment_AccountSearch();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_accountList);
        fragmentTransaction.commit();
        title_text = (TextView) findViewById(R.id.title_text);
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        imageButton_Search = (ImageButton) findViewById(R.id.title_imageButton_search);
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
        imageButton_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleSearchButtonClick();
            }
        });
    }
    public void switchContent() {
        if (isIndexView) {
            //imageButton_Search.setVisibility(View.GONE);
            isIndexView = !isIndexView ;
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.my_slide_in_right, R.anim.abc_fade_out);
            if (fragment_accountSearch.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.remove(fragment_accountSearch); // 隐藏当前的fragment，显示下一个
            }
            if (!fragment_accountInfo.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_accountList).add(R.id.frame, fragment_accountInfo).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_accountList).show(fragment_accountInfo).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
        else {
            //imageButton_Search.setVisibility(View.VISIBLE);
            isIndexView = !isIndexView ;
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, R.anim.my_slide_out_right);
            if (!fragment_accountList.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_accountInfo).add(R.id.frame, fragment_accountList).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_accountInfo).show(fragment_accountList).commit(); // 隐藏当前的fragment，显示下一个
            }
            title_text.setText("账户列表");
        }
    }
    private void onTitleBackButtonClick(){
        if (fragment_accountSearch.isAdded()) {    // 先判断是否被add过
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment_accountSearch).commit(); // 隐藏当前的fragment，显示下一个
        } else if (isIndexView) {
            Activity_AccountList.this.setResult(RESULT_OK);
            Activity_AccountList.this.finish();
        }
        else{
            switchContent();
        }
    }

    private void onTitleSearchButtonClick() {
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragment_accountSearch.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.add(R.id.frame_search, fragment_accountSearch).commit();
        } else {
            fragmentTransaction.remove(fragment_accountSearch).commit(); // 隐藏当前的fragment，显示下一个
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
        getMenuInflater().inflate(R.menu.menu_activity__account_list, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentSearchInteraction(Map<String, String> params) {
        onTitleSearchButtonClick();
        if (fragment_accountList != null) {
            fragment_accountList.onGetSearchData(params);
        }
    }

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
      /*  if (fragment_accountSearch.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.remove(fragment_accountSearch); // 隐藏当前的fragment，显示下一个
        }*/
        if (!fragment_accountInfo.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(fragment_accountList).add(R.id.frame, fragment_accountInfo).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(fragment_accountList).show(fragment_accountInfo).commit(); // 隐藏当前的fragment，显示下一个
        }
        title_text.setText("账户信息");
        fragment_accountInfo.setData(account);
    }
}
