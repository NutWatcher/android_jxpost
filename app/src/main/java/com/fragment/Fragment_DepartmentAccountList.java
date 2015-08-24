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

import com.model.Department;
import com.model.User;
import com.myapplication.Activity_AccountList;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_DepartmentAccountList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_DepartmentAccountList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_DepartmentAccountList extends Fragment {

    private List<Map<String, String>> dataList = new ArrayList<>();
    private SimpleAdapter adapter;
    private ListView listView;

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
     * @return A new instance of fragment Fragment_DepartmentAccountList.
     */
    public static Fragment_DepartmentAccountList newInstance(String param1, String param2) {
        Fragment_DepartmentAccountList fragment = new Fragment_DepartmentAccountList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_DepartmentAccountList() {
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
        return inflater.inflate(R.layout.fragment__department_account_list, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        initData();
        initEvent();
    }

    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.account_listView);
    }

    private void initData() {
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_departmentaccount_item,
                new String[]{"name", "balance"}, new int[]{R.id.lv_department_name, R.id.lv_department_finish});
        listView.setAdapter(adapter);
        mListener.OnFragment_GetDepartmentAccountLIst();
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> map = dataList.get(i);
//                Log.i("select", "name: " + map.get("name") + " rank: " + map.get("rank"));
                Log.i("fragment_Department", "departmentId: "+ map.get("departmentId"));
                mListener.OnFragment_DepartmentAccountLIst_ListItemClick(Integer.parseInt(map.get("departmentId")));
            }
        });
    }

    public void setViewData(final List<Department> data) {
        class checkLoginThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < data.size(); i++) {
                    Department department = data.get(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("name", department.getdepartmentName());
                    map.put("balance", department.getfinish());
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
        void OnFragment_GetDepartmentAccountLIst();
        void OnFragment_DepartmentAccountLIst_ListItemClick(int departmentId);
    }

}
