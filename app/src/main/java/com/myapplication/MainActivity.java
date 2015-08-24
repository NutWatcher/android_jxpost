package com.myapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
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
import com.communicate.Con_Account;
import com.communicate.Con_User;
import com.db.DB_Account;
import com.model.Account;
import com.model.User;
import com.service.AccountService;
import com.tool.IndexDialog;
import com.tool.LoadingDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private Intent Intent_AccountService;
    private Context mContext;
    private LoadingDialog dialog;
    private IndexDialog indexDialog;
    private int userId = 0;
    private GridView gridView;
    private TextView tv_main_name;
    private TextView tv_main_department;
    private TextView tv_main_average_money;
    private TextView tv_main_expect_money;
    private TextView tv_main_need_money;
    private MsgReceiver msgReceiver;
    private NotificationManager mNotifyMgr;
    // 声明Notification（通知）对象
    private Notification notification;
    // 消息的唯一标示id
    public int mNotificationId = 1;
    private DB_Account dbHelper;

    private String[] titles = new String[]
            {"个人协储", "部门信息", "网点余额", "设置", "荣誉榜", "努力榜", "pic6", "pic7", "pic8", "pic9"};
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
        setClock();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("main_actovoty", "onResume");
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
                        // indexDialog.show();
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
                case "user_clear_account":
                    int total = data.getInt("total");
                    Log.i("main_acitivy", "total:" + total);
                    if (total != 0) {
                        saveClearAccount(data);
                        startAppInfo();
                    }
                    break;
            }
        }
    };
    private void initWidget() {
        dialog = new LoadingDialog(this);
        indexDialog = new IndexDialog(this);
        tv_main_name = (TextView) findViewById(R.id.tv_main_name);
        tv_main_department = (TextView) findViewById(R.id.tv_main_department);
        tv_main_average_money = (TextView) findViewById(R.id.tv_main_average_money);
        tv_main_expect_money = (TextView) findViewById(R.id.tv_main_expect_money);
        tv_main_need_money = (TextView) findViewById(R.id.tv_main_need_money);
        gridView = (GridView)findViewById(R.id.gridView);
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ELITOR_CLOCK");
        registerReceiver(msgReceiver, intentFilter);
        dbHelper = new DB_Account(this);
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
                    intent.putExtra("fragment", "1");
                    startActivityForResult(intent, 1);
                } else if (id == 1) {
                    Intent intent = new Intent(MainActivity.this, Activity_Department.class);

                    startActivityForResult(intent, 1);
                } else if (id == 2) {
                    Intent intent = new Intent(MainActivity.this, Activity_Org.class);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

    @Override
    protected void onDestroy() {
        stopService(Intent_AccountService);
        super.onDestroy();
    }

    private void saveClearAccount(final Bundle data) {
        class saveClearAccountThread extends Thread {
            @Override
            public void run() {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues cValue;
                List<Account> rows = data.getParcelableArrayList("rows");
                for (int i = 0; i < rows.size(); i++) {
                    cValue = new ContentValues();
                    Account account = rows.get(i);
                    Map<String, Object> map = new HashMap<>();
                    cValue.put("extUserId", account.getExtUserId());
                    cValue.put("customerName", account.getCustomerName());
                    cValue.put("balance", account.getBalance());
                    db.insert("Clear_Account", null, cValue);
                }
                db.close();
            }
        }
        new saveClearAccountThread().start();
    }

    private void startAppInfo() {

        Intent resultIntent = new Intent(MainActivity.this, Activity_AccountList.class);
        resultIntent.putExtra("fragment", "ClearAccount");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                MainActivity.this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.post_logo)
                .setTicker("嘉兴邮政提示!")
                .setContentTitle("嘉兴邮政提示")
                .setContentText("您最近有账户销户！！")
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(resultPendingIntent).build();
        notification = builder.build();
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId++, notification);
    }
    private void setClock() {
        Intent intent = new Intent("ELITOR_CLOCK");
        intent.putExtra("msg", "你该打酱油了");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5 * 100000, pi);
        getClearAccount();
    }
    private void getClearAccount() {
        if (userId == 0) {
            //setClock();
            return;
        }
        Log.i("main_activty", "getClearAccount");
        Con_Account con_account = new Con_Account(handler);
        con_account.getClearAccountList(userId);
    }

    public class MsgReceiver extends BroadcastReceiver {
        private int i = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到进度，更新UI
            Log.i("broadcast", "come!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            getClearAccount();
        }
    }
}
