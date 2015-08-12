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
import com.model.User;
import com.tool.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class Activity_Login extends FragmentActivity implements
        Fragment_Login.OnFragmentInteractionListener,
        Fragment_LoginSelect.OnFragmentInteractionListener {


    private Context mContext;
    private LoadingDialog dialog;
    private boolean isFragmentSelectShow;
    private int userId;
    private int departmentId ;
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
        dialog = new LoadingDialog(this);
        fragment_login = new Fragment_Login();
        fragment_loginSelect = new Fragment_LoginSelect();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_login);
        fragmentTransaction.commit();
    }

    private void initData() {
        mContext = this;
        dialog.setCanceledOnTouchOutside(false);
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

        return super.onOptionsItemSelected(item);
    }

    public void selectUserId(String result) {
        JSONArray rows;
        rows = null;
        try {
            rows = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rows.length() == 0) {
            Toast.makeText(this, "该用户不存在！！", Toast.LENGTH_SHORT).show();
        } else if (rows.length() > 1) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    R.anim.my_slide_in_right, R.anim.abc_fade_out);
            if (!fragment_loginSelect.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_login).add(R.id.frame, fragment_loginSelect).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_login).show(fragment_loginSelect).commit(); // 隐藏当前的fragment，显示下一个
            }
            isFragmentSelectShow = true;
            fragment_loginSelect.setListData(rows);
        } else {
            JSONObject jsonObject = (JSONObject) rows.opt(0);
            try {
                this.userId = Integer.parseInt(jsonObject.getString("value"));
                //this.departmentId = Integer.parseInt(jsonObject.getString("departmentId"));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "用户id不存在！！", Toast.LENGTH_SHORT).show();
                return;
            }
            checkPassword();
        }
    }

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
            backFragment_Login();
            if (method.equals("userList")) {
                selectUserId(data.getString("result"));
            } else if (method.equals("checkPassword")) {
                if (data.getBoolean("success")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("userId", String.valueOf(userId));
                    editor.putString("departmentId", data.getString("departmentId"));
                    editor.commit();
                    dialog.dismiss();
                    Activity_Login.this.setResult(200);
                    Activity_Login.this.finish();
                } else {
                    Toast.makeText(mContext, data.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    public void onSubmit(Bundle bundle) {
        String name = bundle.getString("name");
        password = bundle.getString("password");
        dialog.show();
        Log.i("loing", "clike");
        Con_Login con_login = new Con_Login(handler);
        con_login.getUserList(name);
    }

    @Override
    public void onFragmentUserSelect(int userId) {
        this.userId = userId;
       // this.departmentId = departmentId;
        checkPassword();
    }

    private void checkPassword() {
        dialog.show();
        Con_Login con_login = new Con_Login(handler);
        con_login.checkPassword(userId, password);
    }

    @Override
    public void onBackPressed() {
    }
}
