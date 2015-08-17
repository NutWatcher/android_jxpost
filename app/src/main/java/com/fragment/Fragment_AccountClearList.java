package com.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
    // TODO: Rename and change types and number of parameters
    public static Fragment_AccountClearList newInstance(String param1, String param2) {
        Fragment_AccountClearList fragment = new Fragment_AccountClearList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        return inflater.inflate(R.layout.fragment_account_list, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    private void initWidget() {
        listView = (ListView) getActivity().findViewById(R.id.listView);
        textView = (TextView) getActivity().findViewById(R.id.worning);
        textView.setVisibility(View.GONE);

    }

    private void initData() {
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
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), dataList, R.layout.listview_accountlist_item,
                new String[]{"customerName", "balance", "extUserId"}, new int[]{R.id.listname, R.id.listmoney, R.id.account});
        listView.setAdapter(adapter);
    }

    private void initEvent() {

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
