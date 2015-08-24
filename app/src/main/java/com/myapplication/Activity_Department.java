package com.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_DepartmentAccount;
import com.fragment.Fragment_DepartmentAccountList;
import com.fragment.Fragment_DepartmentUserAccountList;
import com.model.Department;
import com.model.User;
import com.tool.LoadingDialog;

import java.util.List;


public class Activity_Department extends FragmentActivity implements
    Fragment_DepartmentAccountList.OnFragmentInteractionListener,
    Fragment_DepartmentUserAccountList.OnFragmentInteractionListener{
    Context mContext;
    LoadingDialog loadingDialog;
    TextView title_text;
    ImageButton imageButton_Back;
    FragmentManager fragmentManager;
    Fragment_DepartmentAccountList fragment_departmentAccountList;
    Fragment_DepartmentUserAccountList fragment_departmentUserAccountList ;

    int flag_now_fragment = 0;
    static int FRAGMENT_DEPARTMENT_USER_VIEW = 1 ;
    static int FRAGMENT_DEPARTMENT_VIEW = 0 ;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (loadingDialog.isShowing()) {
                loadingDialog.hide();
            }
            if (method.equals("con_failed")) {
                String result = data.getString("result");
                if (result == null) {
                    result = "网络连接失败";
                }
                Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                return;
            }
            if (method.equals("department_list")) {
                List<Department> rows = data.getParcelableArrayList("rows");
                fragment_departmentAccountList.setViewData(rows);
            }
            else if (method.equals("department_user_list")) {
                List<User> rows = data.getParcelableArrayList("rows");
                fragment_departmentUserAccountList.setViewData(rows);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity__department);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);
        initWidget();
        initData();
        initEvent();
    }
    private void initWidget() {
        loadingDialog = new LoadingDialog(this);
        title_text = (TextView) findViewById(R.id.title_text);
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        fragment_departmentAccountList = new Fragment_DepartmentAccountList() ;
        fragment_departmentUserAccountList = new Fragment_DepartmentUserAccountList();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_departmentAccountList);
        fragmentTransaction.commit();
    }

    private void initData() {
        mContext = this;
        title_text.setText("部门信息");
    }

    private void initEvent() {
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleBackButtonClick();
            }
        });
    }

    private void changeFragementView(Fragment hide, Fragment show){
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.my_slide_in_right, R.anim.abc_fade_out);
        if (!show.isAdded()) {    // 先判断是否被add过
            fragmentTransaction.hide(hide).add(R.id.frame, show).commit(); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(hide).show(show).commit(); // 隐藏当前的fragment，显示下一个
        }
    }
    private void onTitleBackButtonClick() {
        if (flag_now_fragment == FRAGMENT_DEPARTMENT_USER_VIEW){
            flag_now_fragment = FRAGMENT_DEPARTMENT_VIEW ;
            changeFragementView(fragment_departmentUserAccountList, fragment_departmentAccountList);
            return;
        }
        loadingDialog.dismiss();
        Activity_Department.this.setResult(RESULT_OK);
        Activity_Department.this.finish();
    }

    public void onBackPressed() {
        onTitleBackButtonClick();
        // super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__department, menu);
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentGetUserInfoList() {

    }

    @Override
    public void OnFragment_GetDepartmentAccountLIst() {
        loadingDialog.show();
        Con_DepartmentAccount con_departmentAccount = new Con_DepartmentAccount(handler);
        con_departmentAccount.getDepartmentAccountList();
    }

    @Override
    public void OnFragment_DepartmentAccountLIst_ListItemClick(int departmentId) {
        flag_now_fragment = FRAGMENT_DEPARTMENT_USER_VIEW ;
        changeFragementView(fragment_departmentAccountList, fragment_departmentUserAccountList);
        loadingDialog.show();
        Con_DepartmentAccount con_departmentAccount = new Con_DepartmentAccount(handler);
        con_departmentAccount.getDepartmentUserAccountList(departmentId);
    }
}
