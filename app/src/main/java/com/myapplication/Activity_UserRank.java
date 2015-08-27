package com.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.communicate.Con_User;
import com.fragment.Fragment_UserRank;
import com.model.User;
import com.tool.LoadingDialog;

import java.util.List;


public class Activity_UserRank extends FragmentActivity implements
        Fragment_UserRank.OnFragmentInteractionListener {
    Context mContext;
    LoadingDialog loadingDialog;
    TextView title_text;
    ImageButton imageButton_Back;
    FragmentManager fragmentManager;
    Fragment_UserRank fragment_userRank ;


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
            if (method.equals("user_rank_list")) {
                List<User> rows = data.getParcelableArrayList("rows");
                int total = data.getInt("total");
                fragment_userRank.setViewData(data, total);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity__user_rank);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_account);

        initWidget();
        initData();
        initEvent();
    }
    private void initWidget() {
        loadingDialog = new LoadingDialog(this);
        title_text = (TextView) findViewById(R.id.title_text);
        imageButton_Back = (ImageButton) findViewById(R.id.title_imageButton_back);
        fragment_userRank = new Fragment_UserRank() ;
        fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, fragment_userRank);
        fragmentTransaction.commit();
    }

    private void initData() {
        mContext = this;
        title_text.setText("排行榜");
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
        loadingDialog.dismiss();
        Activity_UserRank.this.setResult(RESULT_OK);
        Activity_UserRank.this.finish();
    }

    public void onBackPressed() {
        onTitleBackButtonClick();
        // super.onBackPressed();
        System.out.println("按下了back键   onBackPressed()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__user_rank, menu);
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
    public void onFragment_UserRank_getData(boolean isTop) {
        loadingDialog.show();
        Con_User con_user = new Con_User(handler);
        if(isTop){
            con_user.getUserRank(true);
        }
        else{
            con_user.getUserRank(false);
        }
    }
}
