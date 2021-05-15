package com.mandeep.firechat.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.MessageAdapter;
import com.mandeep.firechat.Models.MessageModel;
import com.mandeep.firechat.Models.NewGroupModel;
import com.mandeep.firechat.Models.NewUser;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment implements View.OnClickListener {
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
    String groupuid = "";
    MessageModel sentmessmodel,recmessmodel,fetchmessage;
    SimpleDateFormat dateFormat;
    Date date;
    int size= 0;

    public GroupChatFragment() {
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
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        initID(view);
        mGetUidAndFetchData();
        return view;
    }

    private void mGetUidAndFetchData() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Bundle bundle = getArguments();
        Log.d("chatwindow"," Group UID : "+ bundle.getString("uid"));
        groupuid = bundle.getString("uid");
        mFetchGroupDetails(groupuid);
        mLoadPrevMessages(groupuid);

    }

    private void mFetchGroupDetails(final String groupuid) {
        Toast.makeText(context, " Group ID : "+groupuid, Toast.LENGTH_SHORT).show();
        databaseReference.child("group").child(groupuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    NewGroupModel groupModel = snapshot.getValue(NewGroupModel.class);
                    Toast.makeText(context, "Group Name : "+groupModel.getGroupname(), Toast.LENGTH_SHORT).show();
                    txtusername.setText(groupModel.getGroupname());
                    Picasso.with(context).load(groupModel.getGroupicon()).into(imguserpic);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initID(View view) {
        messageModelArrayList = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm a");
        recyclerView = view.findViewById(R.id.recy_userchat_mess);
        edsentmessage = view.findViewById(R.id.chatsend_textmsg);
        imgsend = view.findViewById(R.id.chatsend_sendmsg);
        imgemoji = view.findViewById(R.id.chatsend_emoji);
        imguserpic = view.findViewById(R.id.chatop_userpic);
        txtusername = view.findViewById(R.id.chatop_username);
        txtlastseen = view.findViewById(R.id.chatop_userstatus);
        imgsend.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == imgsend){
            Toast.makeText(context, "Message : "+edsentmessage.getText().toString(), Toast.LENGTH_SHORT).show();
            sentMessage();
        }
    }

    private void sentMessage() {

        String key = databaseReference.push().getKey();
        Toast.makeText(context, "Key  : " + key, Toast.LENGTH_SHORT).show();
        String message = edsentmessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            date = new Date();
            sentmessmodel = new MessageModel(message, "false", "text", dateFormat.format(date), firebaseUser.getUid(), "sent");
            databaseReference.child("groupmessage").child(groupuid).child(key).setValue(sentmessmodel);
            edsentmessage.setText(null);
            mLoadPrevMessages(groupuid);
        }
    }

    private void mLoadPrevMessages(String groupuid) {
        databaseReference.child("groupmessage").child(groupuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                size = (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    Toast.makeText(context, "Prev Message : "+messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                    messageModelArrayList.add(messageModel);
                    Toast.makeText(context, messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if(size == messageModelArrayList.size()){
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
}
