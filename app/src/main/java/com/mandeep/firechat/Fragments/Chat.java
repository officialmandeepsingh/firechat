package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.ChatViewMainAdapter;
import com.mandeep.firechat.Adapters.GroupViewsMainAdapter;
import com.mandeep.firechat.Models.MainChatViewModel;
import com.mandeep.firechat.Models.NewGroupModel;
import com.mandeep.firechat.Models.NewUser;
import com.mandeep.firechat.R;
import com.mandeep.firechat.DialogFragments.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment implements View.OnClickListener {
    Context context;
    RecyclerView recyclerView;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    ChatViewMainAdapter chatViewMainAdapter;
    MainChatViewModel mainChatViewModel;
    ArrayList<MainChatViewModel> chatViewModelArrayList;
    String frienduid = "";
    int totalkeyfound=0;
    ArrayList<String> totalmemberinhroupslist;
    TextView txtNewMessage,txtNewGroup;

    public Chat() {
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
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        initID(view);
        return view;
    }

    private void initID(View view) {
        chatViewModelArrayList = new ArrayList<>();
        totalmemberinhroupslist = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        recyclerView = view.findViewById(R.id.recy_mainchat_chats);
        txtNewMessage = view.findViewById(R.id.txt_chatmain_newmess);
        txtNewGroup = view.findViewById(R.id.txt_chatmain_newgroup);

        txtNewMessage.setOnClickListener(this);
        txtNewGroup.setOnClickListener(this);

        setGroupId();
//        getDeafaultPicURL();
//        mlLoadPrevChats();
        mLoadGroups();
        Log.d("mainchat"," End");
    }

    private void setGroupId() {
        databaseReference.child("group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren() ) {
                    Toast.makeText(context, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds:snapshot.getChildren()){
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("groupid",snapshot.getKey());

                        databaseReference.child("group").child(snapshot.getKey()).child(ds.getKey()).updateChildren(hashMap);
                        Toast.makeText(context, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDeafaultPicURL() {
        storageReference.child("groupicon").child("group.png").getDownloadUrl().addOnSuccessListener(getActivity(), new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("mypicurl",uri.toString());

            }
        });
    }

    private void mLoadGroups() {
        databaseReference.child("groupdetails").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("mygroup"," You are Member of "+dataSnapshot.getChildrenCount()+" Groups");
                for (DataSnapshot s: dataSnapshot.getChildren()){

                    totalmemberinhroupslist.add(s.getKey());
                }
                mFetchGroupsDetails(totalmemberinhroupslist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mFetchGroupsDetails(ArrayList<String> totalmemberinhroupslist) {
        final int size = totalmemberinhroupslist.size();
        final ArrayList<NewGroupModel> newGroupModelArrayList = new ArrayList<>();
//        Log.d("groupdetails",totalmemberinhroupslist.get(0).toString());
        for (String i:totalmemberinhroupslist) {
            databaseReference.child("group").child(i).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s:dataSnapshot.getChildren()) {
                        NewGroupModel groupModel = s.getValue(NewGroupModel.class);
                        newGroupModelArrayList.add(groupModel);
                    }
                    if(size == newGroupModelArrayList.size()){
                        GroupViewsMainAdapter groupViewsMainAdapter = new GroupViewsMainAdapter(newGroupModelArrayList,size,context);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(groupViewsMainAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void mlLoadPrevChats() {
        databaseReference.child("message").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("mainchat","Loop Start ");
                totalkeyfound = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Log.d("mainchat",ds.getKey());
                    frienduid = ds.getKey();
                    mFetchuserDetails(frienduid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("arraysize","7 : "+chatViewModelArrayList.size());
    }

    private void mFetchuserDetails(final String frienduid) {
        Log.d("mainchat","mFetchuserDetails : "+frienduid);

        databaseReference.child("users").child(frienduid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mainChatViewModel = new MainChatViewModel(dataSnapshot.child("pic_url").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("accstatus").getValue().toString(),dataSnapshot.getKey());
                mFetchUserMessageDetails(frienduid,mainChatViewModel);
                Log.d("arraysize","9 : "+chatViewModelArrayList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mFetchUserMessageDetails(final String frienduid, final MainChatViewModel mainChatViewModel) {
        Query query =databaseReference.child("message").child(firebaseUser.getUid()).child(frienduid).limitToLast(1);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("mainchat"," Query : "+dataSnapshot.getKey());
                mainChatViewModel.setMessage(dataSnapshot.child("message").getValue().toString());
                mainChatViewModel.setTime(dataSnapshot.child("time").getValue().toString());
                Log.d("mainchat"," Model Class  : "+mainChatViewModel.toString());
                chatViewModelArrayList.add(mainChatViewModel);
                if(totalkeyfound == chatViewModelArrayList.size()){
                    chatViewMainAdapter = new ChatViewMainAdapter(chatViewModelArrayList,chatViewModelArrayList.size(),context);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(chatViewMainAdapter);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("mainchat"," Query : "+dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        
        if(v == txtNewGroup){
            Toast.makeText(context, "New Group", Toast.LENGTH_SHORT).show();
            dlog_newgroup newgroup = new dlog_newgroup();
            newgroup.show(getFragmentManager(),"New Group");

        }else if(v == txtNewMessage){
            Toast.makeText(context, "New Message", Toast.LENGTH_SHORT).show();
            dlog_NewMessage newMessage = new dlog_NewMessage();
            newMessage.show(getFragmentManager(),"New Message");
        }
        
    }
}
