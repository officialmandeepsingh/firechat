package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.MyViewUsersAdapter;
import com.mandeep.firechat.Interfaces.InitialFirebaseServices;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsers extends Fragment {
    Context context;
    ArrayList<AllUserModel> allUserModels;
    MyViewUsersAdapter myViewUsersAdapter;
    RecyclerView recyclerView;
    AllUserModel userModel;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser user;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public AllUsers() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
//        mGetAllFRomDatabase();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        initID(view);
//        Log.d("mydata"," onCreateView Map : "+map.toString());
        return view;
    }

    private void initID(View view) {
        recyclerView = view.findViewById(R.id.myalluserrecycler);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = firebaseStorage.getReference();

        allUserModels = new ArrayList<>();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mGetAllFRomDatabase();
    }


    private void mGetAllFRomDatabase() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    if(postSnapshot.getKey().equalsIgnoreCase(user.getUid())){
                        continue;
                    }
                   Log.d("myauthcheck",postSnapshot.child("name").getValue().toString()+" "+postSnapshot.child("pic_url").getValue().toString());
                   userModel=new AllUserModel(postSnapshot.getKey(),postSnapshot.child("name").getValue().toString(),postSnapshot.child("pic_url").getValue().toString());
                   allUserModels.add(userModel);

                }

                Log.d("listsize"," All Users  Fianl List size : "+allUserModels.size());

                myViewUsersAdapter = new MyViewUsersAdapter(allUserModels,allUserModels.size(),context);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(myViewUsersAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
