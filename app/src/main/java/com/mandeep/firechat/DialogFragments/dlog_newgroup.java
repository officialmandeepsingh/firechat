package com.mandeep.firechat.DialogFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mandeep.firechat.Adapters.DNewGroupAdapter;
import com.mandeep.firechat.Adapters.DNewMessageAdapter;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.Models.GroupParticipant;
import com.mandeep.firechat.Models.NewGroupModel;
import com.mandeep.firechat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class dlog_newgroup extends DialogFragment implements View.OnClickListener {

    RecyclerView recyclerView;
    Button button;
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
    DNewGroupAdapter groupAdapter;
    ArrayList<String> participantlist;


    public static ArrayList<AllUserModel> groupparticipant;
    public dlog_newgroup() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dlog_newgroup, container, false);
        initID(view);
        return view;
    }



    private void initID(View view) {

        recyclerView = view.findViewById(R.id.dialog_newgroup_recycler);
        groupparticipant = new ArrayList<>();
        participantlist = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = firebaseStorage.getReference();
        allUserModels = new ArrayList<>();
        button = view.findViewById(R.id.dialog_newgroup_btn);
        button.setOnClickListener(this);
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
            groupAdapter = new DNewGroupAdapter(allUserModels,allUserModels.size(),context,this,participantUserIdinterface);
            recyclerView.setAdapter(groupAdapter);


        }


    }

    groupParticipantUserId participantUserIdinterface = new groupParticipantUserId() {

        @Override
        public void getParticipantID(String id) {

            Log.d("mycheck"," getParticipantID User id  : "+id);
            participantlist.add(id);
            Log.d("mycheck"," After Adding Size is : "+participantlist.size());
        }

        @Override
        public void removeParticipantID(String id) {
            Log.d("mycheck"," removeParticipantID User id  : "+id);
            participantlist.remove(id);
            Log.d("mycheck"," After Removing Size is : "+participantlist.size());
        }
    };

    @Override
    public void onClick(View v) {

        if(v == button){


       /* Log.d("mycheck"," Button Clicked on Dialgog Fragment");
        Log.d("mycheck"," participant list : "+participantlist.size());
        final ArrayList<String> adminlist= new ArrayList<>();
        final ArrayList<String> participantarraylist= new ArrayList<>();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        Date date =  new Date();

        adminlist.add(auth.getCurrentUser().getUid());
        participantarraylist.addAll(participantlist);
        GroupParticipant groupParticipant = new GroupParticipant(adminlist,participantarraylist);
        final NewGroupModel newGroupModel = new NewGroupModel("new Group ",auth.getCurrentUser().getUid(),dateFormat.format(date),groupParticipant);

        final HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("addedby",auth.getCurrentUser().getUid());
        hashMap.put("addedon",dateFormat.format(date));


        final String key = databaseReference.push().getKey();
        databaseReference.child("group").child(key).child(auth.getCurrentUser().getUid()).setValue(newGroupModel).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    for (String s: participantarraylist){
                        databaseReference.child("groupdetails").child(s).child(key).setValue(hashMap);
                    }
                    for(String s : adminlist){
                        databaseReference.child("groupdetails").child(s).child(key).setValue(hashMap);
                    }
                }
            }
        });*/
            MyDataStore dataStore = new MyDataStore();
            dataStore.execute();
            this.dismiss();
        }

    }



    public interface groupParticipantUserId{

        void getParticipantID(String id);
        void removeParticipantID(String id);

    }

    class MyDataStore extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("mycheck"," Button Clicked on Dialgog Fragment");
            Log.d("mycheck"," participant list : "+participantlist.size());
            final ArrayList<String> adminlist= new ArrayList<>();
            final ArrayList<String> participantarraylist= new ArrayList<>();
            SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
            Date date =  new Date();

            adminlist.add(auth.getCurrentUser().getUid());
            participantarraylist.addAll(participantlist);
            GroupParticipant groupParticipant = new GroupParticipant(adminlist,participantarraylist);
            final NewGroupModel newGroupModel = new NewGroupModel("new Group ",auth.getCurrentUser().getUid(),dateFormat.format(date),groupParticipant,"https://firebasestorage.googleapis.com/v0/b/firechat-b6830.appspot.com/o/groupicon%2Fgroup.png?alt=media&token=046d0305-e743-43e6-aa44-49deb5b68a6e","");

            final HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("addedby",auth.getCurrentUser().getUid());
            hashMap.put("addedon",dateFormat.format(date));


            final String key = databaseReference.push().getKey();

            databaseReference.child("group").child(key).child(auth.getCurrentUser().getUid()).setValue(newGroupModel).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        for (String s: participantarraylist){

                            databaseReference.child("groupdetails").child(s).child(key).setValue(hashMap);
                        }
                        for(String s : adminlist){
                            databaseReference.child("groupdetails").child(s).child(key).setValue(hashMap);
                        }

                    }
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Creating Group");
            progressDialog.setMessage(" we are creating a Group For You");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

        }
    }
}
