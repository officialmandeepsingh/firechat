package com.mandeep.firechat.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mandeep.firechat.Models.NewUser;
import com.mandeep.firechat.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterNewUser extends Fragment implements View.OnClickListener {
    private MaterialEditText edmail,edpassword,edname,edcontact;
    private Button breg;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private DialogFragment dialogFragment;
    private String sendotp="",scontact="",sname="",semail="",spassword="";
    private Context context;

    public RegisterNewUser() {
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
        View view=inflater.inflate(R.layout.fragment_register_new_user, container, false);
        initID(view);
        return view;
    }

    private void initID(View view) {
        fragmentManager=getFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();

        edmail=view.findViewById(R.id.ed_reg_emailid);
        edcontact=view.findViewById(R.id.ed_reg_phnnum);
        edname=view.findViewById(R.id.ed_reg_name);
        edpassword=view.findViewById(R.id.ed_reg_password);
        breg=view.findViewById(R.id.btn_reg_register);
        breg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==breg){
            mGetEnteredData();
//            mSendOTP();
//            MyDialogFragment myDialog=new MyDialogFragment();
//            FragmentManager fm = getFragmentManager();
//            myDialog.setTargetFragment(RegisterNewUser.this,101);
//            myDialog.show(fm, "fragment_edit_name");
        }

    }

    private void mGetEnteredData() {
        scontact=edcontact.getText().toString();
        sname=edname.getText().toString();
        spassword=edpassword.getText().toString();
        semail=edmail.getText().toString();
        Log.d("myotp"," Entered Data Entries : "+scontact+" "+sname+" "+spassword+" "+semail);
        mRegisteredUSer();
    }

    private void mRegisteredUSer() {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference().child("users");
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("myauth"," User ID : "+auth.getCurrentUser().getUid());

                    mDataStoreInDatabase(reference,auth);

                }
            }
        });
    }

    private void mDataStoreInDatabase(DatabaseReference reference, final FirebaseAuth auth) {

        NewUser newUser=new NewUser(sname,semail,scontact,"","","","Hey I\'m Using Chat Mates","no","pending","");

        reference.child(auth.getCurrentUser().getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mSetUserName(sname,auth);
                }
            }
        });


    }

    private void mSetUserName(String sname, final FirebaseAuth auth) {
        UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
                .setDisplayName(sname)
                .build();

        auth.getCurrentUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mSendEmailVerification(auth);
                }
            }
        });
    }

    private void mSendEmailVerification(FirebaseAuth auth) {

        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(context)
                            .setTitle("Email Verification")
                            .setMessage("Verification Email Sent to Your Email")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    fragment=new Authentication();
                                    fragmentTransaction.replace(R.id.myframe,fragment);
                                    fragmentTransaction.commit();
                                }
                            });
                    builder.show();
                }
            }
        });

    }

    private void mSendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" +  scontact,
                30,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            sendotp=phoneAuthCredential.getSmsCode().toString();
            Log.d("myotp","Sent otp : "+phoneAuthCredential.getSmsCode().toString());
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.d("myotp","Sent otp Failed : "+e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d("myotp","Sent otp : "+s.toString());
            sendotp=s;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode== Activity.RESULT_OK){

            Log.d("myotp","OTP : "+data.getStringExtra("otp"));

        }
    }
}
