package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mandeep.firechat.Adapters.FriendReqReceiveAdapter;
import com.mandeep.firechat.Adapters.FriendReqSentAdapter;
import com.mandeep.firechat.Interfaces.InitialFirebaseServices;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendRequest extends Fragment implements InitialFirebaseServices, View.OnClickListener {

    int status = 1;
    private RecyclerView recyclerView;
    FriendReqSentAdapter friendReqSentAdapter;
    FriendReqReceiveAdapter friendReqReceiveAdapter;;
    Context context;
    ArrayList<AllUserModel> allUserModels ;
    CircleImageView imgrec,imgsent;
    TextView txtrec,txtsent;

    public FriendRequest() {
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

        View view=inflater.inflate(R.layout.fragment_friend_request, container, false);
        allUserModels = new ArrayList<>();
        initID(view);


        return view;
    }

    private void initID(View view) {
        recyclerView = view.findViewById(R.id.recy_reqtype);
        imgrec= view.findViewById(R.id.img_reqt_rece);
        imgsent= view.findViewById(R.id.img_reqt_sent);
        txtrec= view.findViewById(R.id.txt_reqt_rece);
        txtsent= view.findViewById(R.id.txt_reqt_sent);
//        mSetUpToolbar(view);


//        mCheckReceiveRequest();
        imgrec.setOnClickListener(this);
        imgsent.setOnClickListener(this);
        txtrec.setOnClickListener(this);
        txtsent.setOnClickListener(this);

    }

    private void mCheckSentRequest() {
        status = 2;
        allUserModels.clear();
        DATABASE_REFERENCE.child("friend_request").child("sent").child(FIREBASE_AUTH.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("sentreq", String.valueOf(dataSnapshot.getChildrenCount()));
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    mFetchUSerDetailsFromDatabase(ds.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void mFetchUSerDetailsFromDatabase(String uidlist) {
        final ArrayList<AllUserModel> modelArrayList = new ArrayList<>();
         DATABASE_REFERENCE.child("users").child(uidlist).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("sentreq","mFetchUSerDetailsFromDatabase  Nmae "+dataSnapshot.child("name").getValue().toString());
                    AllUserModel model ;
                    model = new AllUserModel(dataSnapshot.child("userid").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("pic_url").getValue().toString());
                    Log.d("sentreq"," Model Class Value : "+model.toString());
                    mCallMethodForAddInArrayList(model);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


    }

    private void mCallMethodForAddInArrayList(AllUserModel model) {

        Log.d("sentreq"," mCallMethodForAddInArrayList Model Class Value : "+model.toString());
        allUserModels.add(model);
        Log.d("sentreq"," mCallMethodForAddInArrayList Array list size : "+allUserModels.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(status == 1){
            friendReqReceiveAdapter = new FriendReqReceiveAdapter(allUserModels,allUserModels.size(),context);
            recyclerView.setAdapter(friendReqReceiveAdapter);
//            friendReqSentAdapter = new FriendReqSentAdapter(allUserModels,allUserModels.size(),context)
        }else if(status == 2){
            friendReqSentAdapter = new FriendReqSentAdapter(allUserModels,allUserModels.size(),context);
            recyclerView.setAdapter(friendReqSentAdapter);
        }


    }


    @Override
    public void onClick(View v) {
        if(v == imgrec || v == txtrec){
            Toast.makeText(context, "Received Request", Toast.LENGTH_SHORT).show();
            mCheckReceiveRequest();
        }else if(v == imgsent || v == txtsent){
            Toast.makeText(context, "Sent Request", Toast.LENGTH_SHORT).show();
            mCheckSentRequest();
        }
    }

    private void mCheckReceiveRequest() {
        status = 1;
        allUserModels.clear();
        DATABASE_REFERENCE.child("friend_request").child("receive").child(FIREBASE_AUTH.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("sentreq", String.valueOf(dataSnapshot.getChildrenCount()));
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    mFetchUSerDetailsFromDatabase(ds.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
