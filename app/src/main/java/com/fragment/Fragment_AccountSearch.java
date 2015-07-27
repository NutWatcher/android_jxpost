package com.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_AccountSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_AccountSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_AccountSearch extends Fragment {

    Button buttonGo;
    Button buttonCancle;
    EditText editTextName;
    EditText editTextMoney;
    CheckBox checkBox_HuoQi;
    CheckBox checkBox_DingQi;
    CheckBox checkBox_QingHu;
    Map<String, String> params = new HashMap<String, String>();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Fragment_AccountSearch newInstance(String param1, String param2) {
        Fragment_AccountSearch fragment = new Fragment_AccountSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_AccountSearch() {
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
        View view = inflater.inflate(R.layout.fragment_account_search, null);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);

    }

    private void initWidget() {

        buttonGo = (Button) getActivity().findViewById(R.id.button);
        buttonCancle = (Button) getActivity().findViewById(R.id.button2);
        editTextName = (EditText) getActivity().findViewById(R.id.editText);
        editTextMoney = (EditText) getActivity().findViewById(R.id.editText2);
        checkBox_HuoQi = (CheckBox) getActivity().findViewById(R.id.checkBox1);
        ;
        checkBox_DingQi = (CheckBox) getActivity().findViewById(R.id.checkBox2);
        ;
        checkBox_QingHu = (CheckBox) getActivity().findViewById(R.id.checkBox3);
        ;
    }

    private void initData() {
    }

    private void reSetForm() {
        editTextName.setText("");
        editTextMoney.setText("");
        checkBox_HuoQi.setChecked(false);
        checkBox_DingQi.setChecked(false);
        checkBox_QingHu.setChecked(false);
    }

    private void upParams() {
        mListener.onFragmentSearchInteraction(params);
    }

    private void initEvent() {
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upParams();
            }
        });
        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reSetForm();
                upParams();
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
        public void onFragmentSearchInteraction(Map<String, String> params);
    }
}
