package com.myapplication;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.application.R;
import com.fragment.Fragment_DepartmentAccountList;
import com.fragment.Fragment_DepartmentUserAccountList;


public class Activity_DepartmentUserAccount extends ActionBarActivity {

    FragmentManager fragmentManager;
    Fragment_DepartmentUserAccountList fragment_departmentUserAccountList;

    ImageButton imageButton_Back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity__department_user_account);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);
        initWidget();
        initData();
        initEvent();
    }
    private void initWidget() {
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        fragment_departmentUserAccountList = new Fragment_DepartmentUserAccountList();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_departmentUserAccountList);
        fragmentTransaction.commit();
    }

    private void initData() {

    }

    private void initEvent() {
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleBackButtonClick();
            }
        });
    }
    private void onTitleBackButtonClick(){
        Activity_DepartmentUserAccount.this.setResult(RESULT_OK);
        Activity_DepartmentUserAccount.this.finish();
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
        getMenuInflater().inflate(R.menu.menu_activity__department_user_account, menu);
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
}
