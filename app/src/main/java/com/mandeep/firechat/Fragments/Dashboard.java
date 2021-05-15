package com.mandeep.firechat.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.StatePageViewerAdapter;
import com.mandeep.firechat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {
 ViewPager viewPager;
 TabLayout tabLayout;
 FirebaseAuth auth;
 FirebaseUser user;
 FirebaseStorage storage;
 FirebaseDatabase database;
 DatabaseReference reference;
 StorageReference storageReference;
 FragmentManager fragmentManager;
 FragmentTransaction fragmentTransaction;
 Fragment fragment;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        initID(view);
//        mToolbarSetup(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatePageViewerAdapter viewerAdapter = new StatePageViewerAdapter(getFragmentManager());
        viewPager.setAdapter(viewerAdapter);
    }

    private void initID(View v) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = database.getReference();

        viewPager = v.findViewById(R.id.mypageviewer);
        tabLayout = v.findViewById(R.id.mytablayout);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(auth.getCurrentUser().getDisplayName());
        StatePageViewerAdapter viewerAdapter = new StatePageViewerAdapter(getFragmentManager());
        viewPager.setAdapter(viewerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    /*private void mToolbarSetup(View view) {
        Toolbar toolbar=view.findViewById(R.id.mytoolbar);
        toolbar.setTitle(user.getDisplayName());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.main_all_btn){
                    Log.d("mytool"," All USers ");
                    fragment = new AllUsers();
                    fragmentTransaction.replace(R.id.myframe,fragment);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    return true;
                }else if(id == R.id.main_logout_btn){
                    Log.d("mytool"," Logout ");
                    mUserSignOut();
                    return true;
                }else if(id == R.id.main_setting_btn){
                    Log.d("mytool"," Setting ");
                    fragment = new settings();
                    fragmentTransaction.replace(R.id.myframe,fragment);
                    fragmentTransaction.addToBackStack("settings");
                    fragmentTransaction.commit();
                    return true;
                }
                return false;
            }
        });
    }*/

    private void mUserSignOut() {
        auth.signOut();
        fragment = new Authentication();
        fragmentTransaction.replace(R.id.myframe,fragment);
//        fragmentTransaction.addToBackStack("Authentication");
        fragmentTransaction.commit();
    }

}
