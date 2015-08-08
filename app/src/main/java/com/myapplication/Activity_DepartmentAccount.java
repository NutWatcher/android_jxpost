package com.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_DepartmentAccount;
import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;
import com.fragment.Fragment_DepartmentAccountList;
import com.fragment.Fragment_DepartmentUserAccountList;
import com.model.Department;
import com.model.User;
import com.tool.LoadingDialog;

import java.util.List;


public class Activity_DepartmentAccount extends FragmentActivity implements
        Fragment_DepartmentAccountList.OnFragmentInteractionListener {

    Context mContext;
    private LoadingDialog dialog;
    TextView title_text;
    ImageButton imageButton_Back;
    FragmentManager fragmentManager;
    Fragment_DepartmentAccountList fragment_departmentAccountList;

    private boolean isIndexView;
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
            if (method.equals("department_list")) {
                List<Department> rows = data.getParcelableArrayList("rows");
                int total = 0;
                fragment_departmentAccountList.setViewData(rows);
            }
        }
    };

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
        dialog = new LoadingDialog(this);
        title_text = (TextView) findViewById(R.id.title_text);
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        fragment_departmentAccountList = new Fragment_DepartmentAccountList();
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_departmentAccountList);
        fragmentTransaction.commit();
    }

    private void initData() {
        mContext = this;
        title_text.setText("网店余额");
    }

    private void initEvent() {
        imageButton_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTitleBackButtonClick();
            }
        });
    }
    private void onTitleBackButtonClick() {
        dialog.dismiss();
        Activity_DepartmentAccount.this.setResult(RESULT_OK);
        Activity_DepartmentAccount.this.finish();
    }

    public void onBackPressed() {
        onTitleBackButtonClick();
        // super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnFragment_GetDepartmentAccountLIst() {
        Con_DepartmentAccount con_departmentAccount = new Con_DepartmentAccount(handler);
        dialog.show();
        con_departmentAccount.getDepartmentAccountList();
    }
}
