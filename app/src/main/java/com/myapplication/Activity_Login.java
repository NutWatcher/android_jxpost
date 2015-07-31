package com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.fragment.Fragment_AccountInfo;
import com.fragment.Fragment_AccountList;
import com.fragment.Fragment_AccountSearch;


public class Activity_Login extends Activity {
    private boolean isIndexView ;
    ImageButton imageButton_Back ;
    ImageButton imageButton_Search ;
    FragmentManager fragmentManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
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
}
