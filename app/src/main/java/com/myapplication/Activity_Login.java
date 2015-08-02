package com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.application.R;
import com.communicate.Con_Login;
import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;
import com.fragment.Fragment_Login;
import com.fragment.Fragment_LoginSelect;


public class Activity_Login extends FragmentActivity implements
        Fragment_Login.OnFragmentInteractionListener,
        Fragment_LoginSelect.OnFragmentInteractionListener {

    private int userId;
    private String password;
    private boolean isIndexView ;
    FragmentManager fragmentManager ;
    Fragment_Login fragment_login;
    Fragment_LoginSelect fragment_loginSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        fragment_login = new Fragment_Login();
        fragment_loginSelect = new Fragment_LoginSelect();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_login);
        fragmentTransaction.commit();
        /*fragment_accountInfo = new Fragment_AccountInfo();
        fragment_accountList = new Fragment_AccountList();
        fragment_accountSearch = new Fragment_AccountSearch();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_accountList);
        fragmentTransaction.commit();
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        imageButton_Search = (ImageButton) findViewById(R.id.title_imageButton_search);*/
    }
    private  void initData(){

    }

    private void initEvent(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__login, menu);
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

    @Override
    public void onSubmit(Bundle bundle) {
        String name = bundle.getString("name");
        password = bundle.getString("password");
        Con_Login con_login = new Con_Login();
        con_login.getUserList(name);
        if (true) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.my_slide_in_right, R.anim.abc_fade_out);
            if (!fragment_loginSelect.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_login).add(R.id.frame, fragment_loginSelect).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_login).show(fragment_loginSelect).commit(); // 隐藏当前的fragment，显示下一个
            }
        } else {
            this.userId = 1;
            checkPassword();
        }
    }

    @Override
    public void onFragmentUserSelect(int userId) {
        this.userId = userId;
        checkPassword();
    }

    private void checkPassword() {
        Con_Login con_login = new Con_Login();
        String result = con_login.checkPassword(userId, password);
        if (result == "ok") {
            SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
            editor.putString("userId", result);
            Activity_Login.this.setResult(RESULT_OK);
            Activity_Login.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        Log.i("Activity_Login", "onBackPressed");
        return;
    }
}
