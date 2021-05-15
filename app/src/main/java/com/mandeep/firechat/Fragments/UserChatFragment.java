package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.MessageAdapter;
import com.mandeep.firechat.Models.MessageModel;
import com.mandeep.firechat.Models.NewUser;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserChatFragment extends Fragment implements View.OnClickListener {
    private CircleImageView imguserpic,imgemoji,imgsend;
    private TextView txtusername,txtlastseen;
    private EditText edsentmessage;
    private RecyclerView recyclerView;
    private Context context;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    ArrayList<MessageModel> messageModelArrayList;
    MessageAdapter messageAdapter;

    int totalmessages = 0;
    String Frienduid = "";
    MessageModel sentmessmodel,recmessmodel,fetchmessage;
    SimpleDateFormat dateFormat;
    Date date;
    public UserChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_chat, container, false);
        mGetUidAndFetchData();
        mLoadPrevMessages(Frienduid);
        initID(view);
        return view;
    }

    private void mLoadPrevMessages(String frienduid) {
        messageModelArrayList = new ArrayList<>();
        Log.d("mychat"," mLoadPrevMessages Friend ID : "+frienduid);
        databaseReference.child("message").child(firebaseUser.getUid()).child(frienduid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("mychat"," Total Messages Between : "+dataSnapshot.getChildrenCount());
                totalmessages = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    Log.d("mychat"," Message Keys : "+ds.getKey());
                    Log.d("mychat"," Message  : "+ds.child("message").getValue().toString());

                    fetchmessage = new MessageModel(ds.child("message").getValue().toString(),ds.child("seen").getValue().toString(),ds.child("type").getValue().toString(),ds.child("time").getValue().toString(),ds.child("from").getValue().toString(),ds.child("set").getValue().toString());

                    messageModelArrayList.add(fetchmessage);

                }
                if(totalmessages == messageModelArrayList.size()){
                    messageAdapter = new MessageAdapter(messageModelArrayList,messageModelArrayList.size(),context);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.smoothScrollToPosition(messageModelArrayList.size());
                    recyclerView.setAdapter(messageAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void initID(View view) {

        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        recyclerView = view.findViewById(R.id.recy_userchat_mess);
        edsentmessage = view.findViewById(R.id.chatsend_textmsg);
        imgsend = view.findViewById(R.id.chatsend_sendmsg);
        imgemoji = view.findViewById(R.id.chatsend_emoji);
        imguserpic = view.findViewById(R.id.chatop_userpic);
        txtusername = view.findViewById(R.id.chatop_username);
        txtlastseen = view.findViewById(R.id.chatop_userstatus);


        imgsend.setOnClickListener(this);
        mFetchUserDetails(Frienduid);


    }

    private void mGetUidAndFetchData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle bundle = getArguments();
        Log.d("chatwindow"," Friend UID : "+ bundle.getString("uid"));
        Frienduid = bundle.getString("uid");

    }

    private void mFetchUserDetails(String frienduid) {
        databaseReference.child("users").child(frienduid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NewUser newUser = dataSnapshot.getValue(NewUser.class);
                Log.d("chatwindow"," Friend UID : "+newUser.getName());
                txtusername.setText(newUser.getName());
                txtlastseen.setText(newUser.getStatus());
                Picasso.with(context).load(newUser.getPic_url()).into(imguserpic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == imgsend){
            mMessageSent();
        }
    }

    private void mMessageSent() {
        String key = databaseReference.push().getKey();
        Toast.makeText(context, key, Toast.LENGTH_SHORT).show();
        String message = edsentmessage.getText().toString();
        if(!TextUtils.isEmpty(message)){
            date = new Date();
         /*   Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",dateFormat.format(date));
            messageMap.put("from",firebaseUser.getUid());*/

            sentmessmodel = new MessageModel(message,"false","text",dateFormat.format(date),firebaseUser.getUid(),"sent");
            recmessmodel = new MessageModel(message,"false","text",dateFormat.format(date),firebaseUser.getUid(),"receive");


        databaseReference.child("message").child(firebaseUser.getUid()).child(Frienduid).child(key).setValue(sentmessmodel);
        databaseReference.child("message").child(Frienduid).child(firebaseUser.getUid()).child(key).setValue(recmessmodel);

        mLoadPrevMessages(Frienduid);
        }
    }
}
