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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication.Activity_AccountList;
import com.myapplication.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Login extends Fragment {

    Button btnSubmit;
    EditText et_loginFragment_name;
    EditText et_loginFragment_password;

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
     * @return A new instance of fragment Fragment_Login.
     */
    public static Fragment_Login newInstance(String param1, String param2) {
        Fragment_Login fragment = new Fragment_Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Fragment_Login() {
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
        return inflater.inflate(R.layout.fragment_login, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        initWidget();
        initData();
        initEvent();
        super.onActivityCreated(savedInstanceState);
    }

    private void initWidget() {
        btnSubmit = (Button) getActivity().findViewById(R.id.btn_loginFragment_login);
        et_loginFragment_name = (EditText) getActivity().findViewById(R.id.et_loginFragment_name);
        et_loginFragment_password = (EditText) getActivity().findViewById(R.id.et_loginFragment_password);
    }

    private void initData() {

    }

    private void initEvent() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(et_loginFragment_name.getText());
                String password = String.valueOf(et_loginFragment_password.getText());
                if (password.equals("") || name.equals("")) {
                    Toast.makeText(getActivity(), "密码用户名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("password", password);
                if (mListener != null) {
                    mListener.onSubmit(bundle);
                }
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
        void onSubmit(Bundle bundle);
    }

}
