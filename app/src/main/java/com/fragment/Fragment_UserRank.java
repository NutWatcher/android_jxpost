package com.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.model.User;
import com.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_UserRank.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_UserRank#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_UserRank extends Fragment {

    ToggleButton toggleButton;
    ListView listView ;
    TextView foot_textView;
    private List<Map<String, Object>> dataList = new ArrayList<>();
    private List<User> dataList_user = new ArrayList<>();
    private SimpleAdapter adapter ;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String method = data.getString("method");
            if (method.equals("notifyDataSetInvalidated")) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };
    public static Fragment_UserRank newInstance(String param1, String param2) {
        Fragment_UserRank fragment = new Fragment_UserRank();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_UserRank() {
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
        return inflater.inflate(R.layout.fragment_user_rank, null);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        initData();
        initEvent();
    }


    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.lv_user_rank_listview);
        toggleButton = (ToggleButton) getActivity().findViewById(R.id.lv_user_rank_toggleButton);
       // listView.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.foot_view_clear_account, null));
        foot_textView = (TextView) getActivity().findViewById(R.id.foot_textView);
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_user_rank_item,
                new String[]{"name","department","score"}, new int[]{R.id.tv_item_username, R.id.tv_item_department, R.id.tv_item_score});
        listView.setAdapter(adapter);

    }
    private  void initData(){
        toggleButton.setChecked(true);
        mListener.onFragment_UserRank_getData(true);
    }
    private void initEvent() {
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mListener.onFragment_UserRank_getData(false);
                } else {
                    mListener.onFragment_UserRank_getData(true);
                }
            }
        });

    }
    public void setViewData(final Bundle data, int total) {
        class setViewDataThread extends Thread {
            @Override
            public void run() {
                dataList.clear();
                List<User> rows = data.getParcelableArrayList("rows");
                for (int i = 0; i < rows.size(); i++) {
                    User user = rows.get(i);
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", user.getName());
                    map.put("department",user.getDepartment());
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
        new setViewDataThread().start();
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
        void onFragment_UserRank_getData(boolean isTop);
    }

}
