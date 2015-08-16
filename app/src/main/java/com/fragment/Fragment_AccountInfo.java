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
import com.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AccountInfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AccountInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AccountInfo extends Fragment {

    TextView tv_account_info_listname;
    TextView tv_account_info_listmoney;
    TextView tv_account_info_account;
    TextView tv_account_info_opentime;
    TextView tv_account_info_type;
    boolean init = false;
    Account account ;

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
     * @return A new instance of fragment Fragment_AccountInfo.
     */
    public static Fragment_AccountInfo newInstance(String param1, String param2) {
        Fragment_AccountInfo fragment = new Fragment_AccountInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_AccountInfo() {
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
        return inflater.inflate(R.layout.fragment_account_info, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        if (account != null) {
            setData(account);
        }
        init = true;
        super.onActivityCreated(savedInstanceState);
    }

    public void setData(Account account) {
        if (tv_account_info_listname == null) {
            this.account = account;
            return;
        }
        Log.i("f_account_info", account.getCustomerName());
        tv_account_info_listname.setText(account.getCustomerName());
        tv_account_info_listmoney.setText(account.getBalance());
        tv_account_info_account.setText(account.getExtUserId());
        tv_account_info_opentime.setText(account.getOpenDt());
        tv_account_info_type.setText(account.getType());
    }

    private void initWidget() {
        tv_account_info_listname = (TextView) getActivity().findViewById(R.id.tv_account_info_listname);
        tv_account_info_listmoney = (TextView) getActivity().findViewById(R.id.tv_account_info_listmoney);
        tv_account_info_account = (TextView) getActivity().findViewById(R.id.tv_account_info_account);
        tv_account_info_opentime = (TextView) getActivity().findViewById(R.id.tv_account_info_opentime);
        tv_account_info_type = (TextView) getActivity().findViewById(R.id.tv_account_info_type);
    }

    private void initData() {
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
        void onFragmentInteraction(Uri uri);
    }

}
