package com.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.model.Account;
import com.model.User;
import com.myapplication.Activity_AccountList;
import com.myapplication.R;

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
 * {@link Fragment_LoginSelect.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_LoginSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_LoginSelect extends Fragment {

    ListView lv_fragment_login_userlist;
    private List<Map<String, String>> dataList = new ArrayList<>();
    private SimpleAdapter adapter;
    TextView textView;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Fragment_LoginSelect newInstance(String param1, String param2) {
        Fragment_LoginSelect fragment = new Fragment_LoginSelect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Fragment_LoginSelect() {
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
        return inflater.inflate(R.layout.fragment_loginselect, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    private void initWidget() {
        lv_fragment_login_userlist = (ListView) getActivity().findViewById(R.id.lv_fragment_login_userlist);
    }

    private void initData() {
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_userlist_item,
                new String[]{"name", "userId", "department"}, new int[]{R.id.listname, R.id.listuserid, R.id.listdepartment});
        lv_fragment_login_userlist.setAdapter(adapter);
        Log.i("loginselect", String.valueOf(dataList.size()));
    }

    private void initEvent() {
        lv_fragment_login_userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> map = dataList.get(i);
                mListener.onFragmentUserSelect(Integer.parseInt(map.get("userId")));
            }
        });
    }

    public void setListData(JSONArray data) {
        dataList.clear();
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonObject = (JSONObject) data.opt(i);
            User user = new User();
            try {
                user.setLogUserData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Map<String, String> map = new HashMap<>();
            map.put("name", user.getName());
            map.put("department", user.getDepartment());
            map.put("userId", user.getUserId());
            dataList.add(map);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            // Log.i("loginselect", "adapter");
        }
    }
    public void setListData(List<User> data) {
        dataList.clear();
        // Log.i("loginselect", String.valueOf(dataList.size()));
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", data.get(i).getName());
            map.put("department", data.get(i).getDepartment());
            map.put("userId", data.get(i).getUserId());
            dataList.add(map);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            // Log.i("loginselect", "adapter");
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
        void onFragmentUserSelect(int userId);
    }

}
