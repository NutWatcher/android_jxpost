package com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.adapter.Adapter_GradView;


public class MainActivity extends Activity {

    private int userId ;
    private GridView gridView;
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

    private void initWidget() {
        gridView = (GridView)findViewById(R.id.gridView);
    }
    private  void initData(){

        gridView.setAdapter(new Adapter_GradView(titles, images, this));


    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("configure", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        userId = Integer.parseInt(sharedPreferences.getString("userId", "0"));
        Log.d("main", String.valueOf(userId));
        if (userId == 0) {
            Intent intent = new Intent(MainActivity.this, Activity_Login.class);
            startActivityForResult(intent, 1);
        }
    }
    private void initEvent(){
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (id == 0) {
                    Intent intent = new Intent(MainActivity.this, Activity_AccountList.class);
                    startActivityForResult(intent, 1);
                } else if (id == 2) {
                    Intent intent = new Intent(MainActivity.this, Activity_DepartmentAccount.class);
                    startActivityForResult(intent, 1);
                } else if (id == 3) {
                    Intent intent = new Intent(MainActivity.this, Activity_Setting.class);
                    startActivityForResult(intent, 1);
                }
                Toast.makeText(MainActivity.this, "id" + id, Toast.LENGTH_SHORT).show();
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
        checkLogin();
    }
}
