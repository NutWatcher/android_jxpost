package com.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.communicate.Con_DepartmentAccount;
import com.model.Account;
import com.model.User;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_DepartmentUserAccountList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_DepartmentUserAccountList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_DepartmentUserAccountList extends Fragment {
    private List<Map<String, String>> dataList = new ArrayList<>();
    private SimpleAdapter adapter;
    private ListView listView;
    TextView departmentInfo_money;
    TextView departmentInfo_name;
    TextView departmentInfo_rank;
    TextView departmentInfo_totleMoney;


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
     * @return A new instance of fragment Fragment_DepartmentUserAccountList.
     */
    public static Fragment_DepartmentUserAccountList newInstance(String param1, String param2) {
        Fragment_DepartmentUserAccountList fragment = new Fragment_DepartmentUserAccountList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_DepartmentUserAccountList() {
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
        return inflater.inflate(R.layout.fragment__department_user_account_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }
    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.lv_department_user_account_listView);
//        departmentInfo_money = (TextView) getActivity().findViewById(R.id.departmentInfo_money);
//        departmentInfo_name = (TextView) getActivity().findViewById(R.id.departmentInfo_name);
//        departmentInfo_rank = (TextView) getActivity().findViewById(R.id.departmentInfo_rank);
//        departmentInfo_totleMoney = (TextView) getActivity().findViewById(R.id.departmentInfo_totleMoney);
    }

    private void initData() {
        mListener.onFragmentGetUserInfoList();
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_department_user_account_item,
                new String[]{"name", "score"}, new int[]{R.id.lv_department_user_account_name, R.id.lv_department_user_account_score});
        listView.setAdapter(adapter);
    }

    public void setViewData(final List<User> data) {
        class checkLoginThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("name", user.getName());
                    map.put("score", user.getScore());
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

    private void initEvent() {
    }

    public void refleshByDepartment(int departmentID) {

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
        void onFragmentInteraction(Uri uri);

        void onFragmentGetUserInfoList();
    }

}
