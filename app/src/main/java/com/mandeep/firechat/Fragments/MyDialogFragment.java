package com.mandeep.firechat.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mandeep.firechat.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialogFragment extends androidx.fragment.app.DialogFragment implements View.OnClickListener {
    MaterialEditText  editText;
    Button button;
    public MyDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.custm_otp, container, false);
        initID(view);
        return view;
    }

    private void initID(View view) {
        editText=view.findViewById(R.id.ed_cusotp);
        button=view.findViewById(R.id.btn_otp_submit);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==button){
            String otp="";
            otp=editText.getText().toString().trim();
            Intent intent=new Intent();
            intent.putExtra("otp",otp);
            Fragment fragment=getTargetFragment();
            getDialog().dismiss();
            fragment.onActivityResult(101, Activity.RESULT_OK,intent);
        }
    }
}
