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

import com.myapplication.Activity_AccountList;
import com.myapplication.R;

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
    private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
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
        View view = inflater.inflate(R.layout.fragment_loginselect, null);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    private void initWidget() {
        lv_fragment_login_userlist = (ListView) getActivity().findViewById(R.id.lv_fragment_login_userlist);
        textView = (TextView) getActivity().findViewById(R.id.worning);
        textView.setVisibility(View.GONE);

    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "战三");
            map.put("money", "800004.56");
            map.put("account", "54684984896849698498");
            dataList.add(map);
        }
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_accountlist_item,
                new String[]{"name", "money", "account"}, new int[]{R.id.listname, R.id.listmoney, R.id.account});
        lv_fragment_login_userlist.setAdapter(adapter);
    }

    private void initEvent() {
        lv_fragment_login_userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedPosition = i;
                Map<String, String> map = dataList.get(i);
                Log.i("select", "userid: " + map.get("userId") + " name: " + map.get("name"));
                Log.i("fragment_log", String.valueOf(selectedPosition));
                mListener.onFragmentUserSelect(Integer.parseInt(map.get("userId")));
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
        // TODO: Update argument type and name
        public void onFragmentUserSelect(int userId);
    }

}
