package com.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;


import com.communicate.Con_Login;
import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;
import com.fragment.Fragment_Login;
import com.fragment.Fragment_LoginSelect;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class Activity_Login extends FragmentActivity implements
        Fragment_Login.OnFragmentInteractionListener,
        Fragment_LoginSelect.OnFragmentInteractionListener {


    private boolean isFragmentSelectShow;
    private int userId;
    private String password;
    private boolean isIndexView;
    FragmentManager fragmentManager;
    Fragment_Login fragment_login;
    Fragment_LoginSelect fragment_loginSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    }

    private void initData() {
        isFragmentSelectShow = false;
    }

    private void initEvent() {
    }

    private void backFragment_Login() {
        if (isFragmentSelectShow) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.my_slide_in_right, R.anim.abc_fade_out);
            if (!fragment_loginSelect.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_loginSelect).add(R.id.frame, fragment_login).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_loginSelect).show(fragment_login).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void onTitleBackButtonClick() {
        backFragment_Login();
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
        getMenuInflater().inflate(R.menu.menu_activity__login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectUserId(String result) {
        Gson gson = new Gson();
        List<User> userlist = gson.fromJson(result, new TypeToken<List<User>>() {
        }.getType());
        if (userlist.size() == 0) {
            Toast.makeText(this, "该用户不存在！！", Toast.LENGTH_SHORT).show();
        } else if (userlist.size() > 1) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.my_slide_in_right, R.anim.abc_fade_out);
            if (!fragment_loginSelect.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_login).add(R.id.frame, fragment_loginSelect).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_login).show(fragment_loginSelect).commit(); // 隐藏当前的fragment，显示下一个
            }
            isFragmentSelectShow = true;
            fragment_loginSelect.setListData(userlist);
        } else {
            this.userId = Integer.parseInt(userlist.get(0).getUserId());
            checkPassword();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            backFragment_Login();
            Log.d("login handler", String.valueOf(userId));
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (method.equals("userList")) {
                selectUserId(data.getString("result"));
            } else if (method.equals("checkPassword")) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(data.getString("result"));
                    String result = jsonObject.getString("success");
                    if (result.equals("ture")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putString("userId", String.valueOf(userId));
                        editor.commit();
                        Activity_Login.this.setResult(RESULT_OK);
                        Activity_Login.this.finish();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onSubmit(Bundle bundle) {
        String name = bundle.getString("name");
        password = bundle.getString("password");
        Con_Login con_login = new Con_Login(handler);
        con_login.getUserList(name);
    }

    @Override
    public void onFragmentUserSelect(int userId) {
        this.userId = userId;
        checkPassword();
    }

    private void checkPassword() {
        Con_Login con_login = new Con_Login(handler);
        con_login.checkPassword(userId, password);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
