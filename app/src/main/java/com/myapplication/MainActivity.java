package com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.Adapter_GradView;
import com.communicate.Con_User;
import com.model.User;
import com.tool.LoadingDialog;

import java.io.IOException;
import java.util.HashMap;


public class MainActivity extends Activity {

    private Context mContext;
    private LoadingDialog dialog;
    private int userId = 0;
    private GridView gridView;
    private TextView tv_main_name;
    private TextView tv_main_department;
    private TextView tv_main_average_money;
    private TextView tv_main_expect_money;
    private TextView tv_main_need_money;

    private String[] titles = new String[]
            {"个人协储", "部门信息", "网点余额", "设置", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
    private int[] images = new int[]{
            R.drawable.user_account, R.drawable.department_user_account, R.drawable.shop_account,
            R.drawable.setting, R.drawable.xiechu, R.drawable.xiechu,
            R.drawable.xiechu, R.drawable.xiechu, R.drawable.xiechu
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initWidget();
        initData();
        initEvent();
        checkLogin();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
            switch (method) {
                case "checkLogin":
                    boolean success = data.getBoolean("success");
                    if (!success) {
                        Intent intent = new Intent(MainActivity.this, Activity_Login.class);
                        startActivityForResult(intent, 1);
                    } else {
                        Log.i("main", "get_userInfo");
                        initUserInfo();
                    }
                    break;
                case "userInfo":
                    User user = data.getParcelable("user");
                    tv_main_name.setText(user.getName());
                    tv_main_department.setText(user.getDepartment());
                    tv_main_average_money.setText(user.getScore());
                    tv_main_expect_money.setText(user.getExpectScoer());
                    tv_main_need_money.setText(user.getMoney());
                    break;
            }
        }
    };
    private void initWidget() {
        dialog = new LoadingDialog(this);
        tv_main_name = (TextView) findViewById(R.id.tv_main_name);
        tv_main_department = (TextView) findViewById(R.id.tv_main_department);
        tv_main_average_money = (TextView) findViewById(R.id.tv_main_average_money);
        tv_main_expect_money = (TextView) findViewById(R.id.tv_main_expect_money);
        tv_main_need_money = (TextView) findViewById(R.id.tv_main_need_money);
        gridView = (GridView)findViewById(R.id.gridView);
    }
    private  void initData(){
        mContext = this;
        gridView.setAdapter(new Adapter_GradView(titles, images, this));
    }

    private void initUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        userId = Integer.parseInt(sharedPreferences.getString("userId", "0"));
        Con_User con_user = new Con_User(handler);
        con_user.getUserinfo(userId);
    }
    private void checkLogin() {
        class checkLoginThread extends Thread {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                userId = Integer.parseInt(sharedPreferences.getString("userId", "0"));
                Message msg = new Message();
                Bundle data = new Bundle();
                if (userId == 0) {
                    data.putBoolean("success", false);
                } else {
                    data.putBoolean("success", true);
                }
                data.putString("method", "checkLogin");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new checkLoginThread().start();
    }
    private void initEvent(){
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (id == 0) {
                    Intent intent = new Intent(MainActivity.this, Activity_AccountList.class);
                    startActivityForResult(intent, 1);
                } else if (id == 1) {
                    Intent intent = new Intent(MainActivity.this, Activity_DepartmentUserAccount.class);
                    startActivityForResult(intent, 1);
                } else if (id == 2) {
                    Intent intent = new Intent(MainActivity.this, Activity_DepartmentAccount.class);
                    startActivityForResult(intent, 1);
                } else if (id == 3) {
                    Intent intent = new Intent(MainActivity.this, Activity_Setting.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 200) {
            Log.i("main", "login_in");
            initUserInfo();
        } else {
            checkLogin();
        }
    }
}
