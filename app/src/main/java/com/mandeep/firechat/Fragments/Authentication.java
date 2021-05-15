package com.mandeep.firechat.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mandeep.firechat.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class Authentication extends Fragment implements View.OnClickListener {
    private MaterialEditText edmail,edpassword;
    private Button blogin,breg;
    private TextView txtpassrecovery;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Context context;
    ProgressDialog progressDialog;

    public Authentication() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_authentication, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        initID(view);
        mLoadSavedCredential();
        return view;
    }

    private void mLoadSavedCredential() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Credential",Context.MODE_PRIVATE);
        //editor.putString("email",edmail.getText().toString());
        //        editor.putString("password",edpassword.getText().toString());
        edmail.setText(sharedPreferences.getString("email",""));
        edpassword.setText(sharedPreferences.getString("password",""));
    }

    private void initID(View view) {

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("users");
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        edmail=view.findViewById(R.id.ed_auth_email);
        edpassword=view.findViewById(R.id.ed_auth_password);
        txtpassrecovery=view.findViewById(R.id.txt_auth_passrecovey);
        blogin=view.findViewById(R.id.btn_auth_login);
        breg=view.findViewById(R.id.btn_auth_regsiter);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Authentication & Authorization");
        progressDialog.setTitle(" Please Wait ");

        blogin.setOnClickListener(this);
        breg.setOnClickListener(this);
        /*if(auth != null){
            fragment=new Dashboard();
            fragmentTransaction.replace(R.id.myframe,fragment);
            fragmentTransaction.commit();
        }*/
    }


    @Override
    public void onClick(View v) {
        if(v==breg){
            fragment=new RegisterNewUser();
            fragmentTransaction.replace(R.id.myframe,fragment,"RegisterNewUser");
            fragmentTransaction.addToBackStack("Register");
            fragmentTransaction.commit();
        }else if(v==blogin){
            progressDialog.show();
            mSaveCredential();
            auth.signInWithEmailAndPassword(edmail.getText().toString(),edpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mGetDeviceToken();
                        progressDialog.dismiss();

                            /*progressDialog.dismiss();
                            fragment=new UserDetails();
                            fragmentTransaction.replace(R.id.myframe,fragment);
                            fragmentTransaction.commit();*/




                        /*
                        * Temporly Block This Block For Testing Purpose

                        */

                        if(auth.getCurrentUser().isEmailVerified()){
                            reference.child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("myauth","accstatus : "+dataSnapshot.child("accstatus").getValue());
                                    // Remove Not
                                    if (dataSnapshot.child("accstatus").getValue().toString().equalsIgnoreCase("complete")){
                                        progressDialog.dismiss();
                                        fragment=new Dashboard();
                                        fragmentTransaction.replace(R.id.myframe,fragment);
                                        fragmentTransaction.commit();

//                                       startActivity(new Intent(getActivity(), Dashboard.class));

                                    }else{
                                        progressDialog.dismiss();
                                        fragment=new UserDetails();
                                        fragmentTransaction.replace(R.id.myframe,fragment);
                                        fragmentTransaction.commit();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            progressDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(context)
                                    .setTitle("Verification Warning")
                                    .setMessage(" Verify Your Email Account ")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builder.show();

                        }
                    }
                }
            });
        }

    }

    private void mGetDeviceToken() {
        FirebaseMessaging.getInstance().subscribeToTopic(auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("mytoken","Device Token");


                }
            }
        });
    }

    private void mSaveCredential() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Credential",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("email",edmail.getText().toString());
        editor.putString("password",edpassword.getText().toString());
        editor.commit();
    }
}
