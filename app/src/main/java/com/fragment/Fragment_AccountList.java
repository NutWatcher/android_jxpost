package com.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Toast;

import com.communicate.Con_Account;
import com.model.Account;
import com.myapplication.Activity_AccountList;
import com.myapplication.Activity_Login;
import com.myapplication.MainActivity;
import com.myapplication.R;
import com.tool.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AccountList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AccountList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AccountList extends Fragment {

    static private int limit = 20;
    private int total = 0;
    private List<Map<String, Object>> dataList = new ArrayList<>();
    private List<Account> dataList_account = new ArrayList<>();
    private SimpleAdapter adapter ;
    private ListView listView ;
    TextView textView ;
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
     * @return A new instance of fragment Fragment_AccountList.
     */
    public static Fragment_AccountList newInstance(String param1, String param2) {
        Fragment_AccountList fragment = new Fragment_AccountList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_AccountList() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (method.equals("notifyDataSetInvalidated")) {
                if (adapter != null) {
                    adapter.notifyDataSetInvalidated();
                }
            }
        }
    };
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
        return inflater.inflate(R.layout.fragment_account_list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }


    /*
    重新加载查询页面
     */
    public void onGetSearchData(Map<String, String> params) {
    }

    public void setViewData(final Bundle data, int total) {
        this.total = total;
        class checkLoginThread extends Thread {
            @Override
            public void run() {
                List<Account> rows = data.getParcelableArrayList("rows");
                for (int i = 0; i < rows.size(); i++) {
                    Account account = rows.get(i);
                    dataList_account.add(account);
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", account.getCustomerName());
                    map.put("money", account.getBalance());
                    map.put("account", account.getExtUserId());
                    dataList.add(map);
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("method", "notifyDataSetInvalidated");
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }
        new checkLoginThread().start();
    }
    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.listView);
        textView = (TextView) getActivity().findViewById(R.id.worning);
        textView.setVisibility(View.GONE);

    }
    private  void initData(){
        mListener.onFragmentAccountGetData(0, limit);
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_accountlist_item,
                new String[]{"name","money","account"}, new int[]{R.id.listname, R.id.listmoney, R.id.account});
        listView.setAdapter(adapter);
    }
    private void initEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("fragment_log", String.valueOf(i));
                Account account = dataList_account.get(i);
                Log.i("account_list", account.getCustomerName());
                Log.i("account_list", "dataList_account length: " + dataList_account.size());
                mListener.onFragmentAccountTouchItem(account);
                //((Activity_AccountList) getActivity()).switchContent();
                //setContentView(R.layout.activity_account_info);
                //isIndexView = false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //AbsListView view 这个view对象就是listview
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        // loadData();
                        //  Log.i("fragment_accountlist","getLastVisiblePosition= "+view.getLastVisiblePosition());
                        //  Log.i("fragment_accountlist","total= "+ total);
                        if (total <= view.getCount()) {
                            return;
                        } else {
                            mListener.onFragmentAccountGetData(view.getCount(), limit);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //lastItem = firstVisibleItem + visibleItemCount - 1 ;
            }
        });
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
        void onFragmentAccountGetData(int start, int limit);

        void onFragmentAccountTouchItem(Account account);
    }

}
