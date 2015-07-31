package com.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;
import com.fragment.Fragment_DepartmentAccountList;
import com.fragment.Fragment_DepartmentUserAccountList;


public class Activity_DepartmentAccount extends FragmentActivity implements Fragment_DepartmentAccountList.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    Fragment_DepartmentAccountList fragment_departmentAccountList;
    Fragment_DepartmentUserAccountList fragment_departmentUserAccountList;

    private boolean isIndexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity__department_account);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        fragment_departmentAccountList = new Fragment_DepartmentAccountList();
        fragment_departmentUserAccountList = new Fragment_DepartmentUserAccountList();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_departmentAccountList);
        fragmentTransaction.commit();
    }

    private void initData() {
        isIndexView = true;
    }

    private void initEvent() {
    }

    private void onTitleBackButtonClick() {
        if (isIndexView) {
            Activity_DepartmentAccount.this.setResult(RESULT_OK);
            Activity_DepartmentAccount.this.finish();
        } else {
            isIndexView = true;
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, R.anim.my_slide_out_right);
            if (!fragment_departmentAccountList.isAdded()) {    // 先判断是否被add过
                fragmentTransaction.hide(fragment_departmentUserAccountList).add(R.id.frame, fragment_departmentAccountList).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                fragmentTransaction.hide(fragment_departmentUserAccountList).show(fragment_departmentAccountList).commit(); // 隐藏当前的fragment，显示下一个
            }
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
        getMenuInflater().inflate(R.menu.menu_activity__department_account, menu);
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

    @Override
    public void OnFragment_DepartmentAccountLIst_ListItemClick(int departmentId) {
        isIndexView = false;
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.my_slide_in_right, R.anim.abc_fade_out);
        if (!fragment_departmentUserAccountList.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(fragment_departmentAccountList).add(R.id.frame, fragment_departmentUserAccountList).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(fragment_departmentAccountList).show(fragment_departmentUserAccountList).commit(); // 隐藏当前的fragment，显示下一个
        }
        fragment_departmentUserAccountList.refleshByDepartment(5);
    }
}
