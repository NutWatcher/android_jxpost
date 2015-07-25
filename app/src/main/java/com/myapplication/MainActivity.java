package com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.adapter.Adapter_GradView;


public class MainActivity extends Activity {
    private GridView gridView;
    private String[] titles = new String[]
            { "协储", "设置", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
    private int[] images = new int[]{
            R.drawable.xiechu, R.drawable.shezhi, R.drawable.xiechu,
            R.drawable.xiechu, R.drawable.xiechu, R.drawable.xiechu,
            R.drawable.xiechu, R.drawable.xiechu, R.drawable.xiechu
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new Adapter_GradView(titles, images, this));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (id == 0){
                    Intent intent = new Intent(MainActivity.this, Activity_AccountList.class);
                    startActivityForResult(intent, 1);
                }
                else
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
