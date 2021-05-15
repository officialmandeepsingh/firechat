package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mandeep.firechat.Interfaces.InitialFirebaseServices;
import com.mandeep.firechat.Models.NewUser;
import com.mandeep.firechat.Models.SentFriendRequest;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegUserProfile extends Fragment implements InitialFirebaseServices, View.OnClickListener {
    CircleImageView imageView;
    TextView txtname,txtstatus;
    Button button,btndlt;
    NewUser newUser;
    Context context;
    String frienduid="";

    public RegUserProfile() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_user_profile, container, false);

        InitID(view);
        return view;
    }

    private void InitID(View view) {
        imageView = view.findViewById(R.id.img_regu_profilepic);
        txtname = view.findViewById(R.id.txt_regu_name);
        txtstatus = view.findViewById(R.id.txt_regu_status);
        button = view.findViewById(R.id.btn_regu_sentreq);
        btndlt = view.findViewById(R.id.btn_regu_dltreq);
        newUser = new NewUser();
        button.setOnClickListener(this);
        btndlt.setOnClickListener(this);
        mCheckGetBungle();

    }



    private void mCheckGetBungle() {
        Bundle bundle=getArguments();
        Log.d("myposition"," Received bundle : "+bundle.getString("uid"));
        frienduid = bundle.getString("uid");
        DATABASE_REFERENCE.child("users").child(bundle.getString("uid")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               newUser =dataSnapshot.getValue(NewUser.class);
                Log.d("myposition",newUser.getName());

                txtstatus.setText(newUser.getStatus());
                txtname.setText(newUser.getName());
                Picasso.with(context).load(newUser.getPic_url()).into(imageView);

                //DATABASE_REFERENCE.child("friend_request").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(frienduid).setValue(sentFriendRequest);
                DATABASE_REFERENCE.child("friend_request").child("sent").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(frienduid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            button.setVisibility(View.INVISIBLE);
                            btndlt.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == button){
            button.setVisibility(View.INVISIBLE);
            btndlt.setVisibility(View.VISIBLE);
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd - MM- yyyy ");
            Date date = new Date();
            SentFriendRequest sentFriendRequest = new SentFriendRequest(frienduid,FIREBASE_AUTH.getCurrentUser().getUid(),simpleDateFormat.format(date));
//            SentFriendRequest receivedFriendRequest =new SentFriendRequest(frienduid,FIREBASE_AUTH.getCurrentUser().getUid(),simpleDateFormat.format(date));
            Log.d("myauthcheck"," Reg USer PROFILE SENT REQUEST : "+sentFriendRequest.toString());
            DATABASE_REFERENCE.child("friend_request").child("sent").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(frienduid).setValue(sentFriendRequest);
            DATABASE_REFERENCE.child("friend_request").child("receive").child(frienduid).child(FIREBASE_AUTH.getCurrentUser().getUid()).setValue(sentFriendRequest);

        }else if(v == btndlt){
            button.setVisibility(View.VISIBLE);
            btndlt.setVisibility(View.INVISIBLE);
            DATABASE_REFERENCE.child("friend_request").child("sent").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(frienduid).removeValue().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        DATABASE_REFERENCE.child("friend_request").child("receive").child(frienduid).child(FIREBASE_AUTH.getCurrentUser().getUid()).removeValue();

                    }
                }
            });
        }
    }
}
