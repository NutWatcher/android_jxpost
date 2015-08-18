package com.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.db.DB_Account;
import com.model.Account;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AccountClearList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AccountClearList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AccountClearList extends Fragment {

    static private int limit = 20;
    private int total = 0;
    private List<Map<String, Object>> dataList = new ArrayList<>();
    private List<Account> dataList_account = new ArrayList<>();
    private SimpleAdapter adapter;
    private ListView listView;
    private ImageButton imageButton;
    TextView textView;
    LinearLayout loadingLayout;
    int userId;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_AccountClearList.
     */
    public static Fragment_AccountClearList newInstance(String param1, String param2) {
        Fragment_AccountClearList fragment = new Fragment_AccountClearList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (method.equals("clearAccountNotifyDataSetInvalidated")) {
                if (adapter != null) {
                    adapter.notifyDataSetInvalidated();
                }
            }
        }
    };
    public Fragment_AccountClearList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_clear_list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.clear_listView);
        imageButton = (ImageButton) getActivity().findViewById(R.id.ib_clear_list_close);
    }

    private void initData() {
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_accountlist_item,
                new String[]{"customerName", "balance", "extUserId"}, new int[]{R.id.listname, R.id.listmoney, R.id.account});
        listView.setAdapter(adapter);
        adapter.notifyDataSetInvalidated();
        getData();
    }

    private void getData() {
        dataList.clear();
        class getClearAccountDataThread extends Thread {
            @Override
            public void run() {
                DB_Account dbHelper = new DB_Account(getActivity());
                SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
                Cursor cursor = sqliteDatabase.query("Clear_Account", new String[]{"extUserId",
                        "customerName", "balance"}, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String extUserId = cursor.getString(cursor.getColumnIndex("extUserId"));
                    String customerName = cursor.getString(cursor.getColumnIndex("customerName"));
                    String balance = cursor.getString(cursor.getColumnIndex("balance"));
                    Account account = new Account();
                    Map<String, Object> map = new HashMap<>();
                    map.put("extUserId", extUserId);
                    map.put("customerName", customerName);
                    map.put("balance", balance);
                    dataList.add(map);
                }
                cursor.close();

                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("method", "clearAccountNotifyDataSetInvalidated");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new getClearAccountDataThread().start();
    }

    private void initEvent() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTableClearAccount_Clear();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
            Log.i("fragment_account_clear", "show");
            //相当于Fragment的onResume
        } else {
            Log.i("fragment_account_clear", "hide");
            //相当于Fragment的onPause
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        public void onTableClearAccount_Clear();
    }

}
