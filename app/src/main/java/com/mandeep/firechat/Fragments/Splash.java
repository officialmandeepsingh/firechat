package com.mandeep.firechat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mandeep.firechat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Splash extends Fragment {

    public Splash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_splash, container, false);
        try {

            mAfterSplash();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }



    private void mAfterSplash() throws InterruptedException {
        Log.d("chechstatus","mAfterSplash");
        Thread.sleep(5000);
        Log.d("chechstatus","After Thread");

        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment fragment=new Authentication();
        fragmentTransaction.replace(R.id.myframe,fragment,"Authentication");
//        fragmentTransaction.addToBackStack("Authentication");
        fragmentTransaction.commit();
    }

}
