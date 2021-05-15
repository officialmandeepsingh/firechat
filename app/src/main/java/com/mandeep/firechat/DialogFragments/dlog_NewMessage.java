package com.mandeep.firechat.DialogFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.DNewMessageAdapter;
import com.mandeep.firechat.Adapters.FriendAdapter;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class dlog_NewMessage extends DialogFragment {
    RecyclerView recyclerView;
    DNewMessageAdapter messageAdapter;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser user;
    int totalchildren;
    Context context;
    ArrayList<AllUserModel> allUserModels ;
    public dlog_NewMessage() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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
        View view = inflater.inflate(R.layout.fragment_dlog__new_message, container, false);
        initID(view);

        return view;
    }

    private void initID(View view) {
        recyclerView = view.findViewById(R.id.dialognewmess_recycler);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = firebaseStorage.getReference();
        allUserModels = new ArrayList<>();
        mCheckMyFriend();
    }

    private void mCheckMyFriend() {
        databaseReference.child("friend").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalchildren = (int) dataSnapshot.getChildrenCount();
                Log.d("myfriends", String.valueOf(totalchildren));
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Log.d("myfriendkey",dataSnapshot1.getKey());
                    mFetchDataFromDatabase(dataSnapshot1.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mFetchDataFromDatabase(String key) {
        final ArrayList<AllUserModel> modelArrayList = new ArrayList<>();
        databaseReference.child("users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("myfriends","mFetchUSerDetailsFromDatabase  Nmae "+dataSnapshot.child("name").getValue().toString());
                AllUserModel model ;
                model = new AllUserModel(dataSnapshot.child("userid").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("pic_url").getValue().toString());
                Log.d("myfriends"," Model Class Value : "+model.toString());
                mCallMethodForAddInArrayList(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void mCallMethodForAddInArrayList(AllUserModel model) {

        Log.d("myfriends"," mCallMethodForAddInArrayList Model Class Value : "+model.toString());
        allUserModels.add(model);
        Log.d("myfriends"," mCallMethodForAddInArrayList Array list size : "+allUserModels.size());
        if(allUserModels.size() == totalchildren){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            messageAdapter = new DNewMessageAdapter(context, allUserModels, allUserModels.size(),this);
            recyclerView.setAdapter(messageAdapter);
        }

    }
}
