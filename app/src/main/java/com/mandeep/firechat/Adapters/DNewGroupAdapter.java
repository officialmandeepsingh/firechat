package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mandeep.firechat.DialogFragments.dlog_newgroup;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DNewGroupAdapter extends RecyclerView.Adapter<DNewGroupAdapter.MyViewHolder> {
    ArrayList<AllUserModel> allUserModels;
    int size;
    Context context;
    dlog_newgroup dlog_newgroup;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    com.mandeep.firechat.DialogFragments.dlog_newgroup.groupParticipantUserId  participantUserIdinterface;
    public DNewGroupAdapter(ArrayList<AllUserModel> allUserModels, int size, Context context, com.mandeep.firechat.DialogFragments.dlog_newgroup dlog_newgroup, com.mandeep.firechat.DialogFragments.dlog_newgroup.groupParticipantUserId participantUserIdinterface) {
        this.allUserModels = allUserModels;
        this.size = size;
        this.context = context;
        this.participantUserIdinterface = participantUserIdinterface;
        this.dlog_newgroup = dlog_newgroup;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dcus_newgroup,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.with(context).load(allUserModels.get(position).getPic_url()).into(holder.imageView);
        holder.textView.setText(allUserModels.get(position).getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(true);
                    participantUserIdinterface.getParticipantID(allUserModels.get(position).getUserid());

                }else if(holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(false);
                    participantUserIdinterface.removeParticipantID(allUserModels.get(position).getUserid());

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;
        CheckBox checkBox;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.dcus_grp_pic);
            textView = itemView.findViewById(R.id.dcus_grp_uname);
            checkBox = itemView.findViewById(R.id.dcus_grp_tick);
        }
    }
}
