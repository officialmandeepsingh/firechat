package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mandeep.firechat.Interfaces.InitialFirebaseServices;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendReqSentAdapter extends RecyclerView.Adapter<FriendReqSentAdapter.MyViewHolder> implements InitialFirebaseServices {
    int size = 0;
    ArrayList<AllUserModel> usersArrayList;
    Context context;

    public FriendReqSentAdapter(ArrayList<AllUserModel> usersArrayList, int size, Context context) {
        this.usersArrayList = usersArrayList;
        this.size = size;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View customv = layoutInflater.inflate(R.layout.cus_friendreq_sent, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(customv);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Log.d("sentreq","List User Name : "+usersArrayList.get(position).getName());
        holder.textView.setText(usersArrayList.get(position).getName());
        Picasso.with(context).load(usersArrayList.get(position).getPic_url()).into(holder.imageView);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("btnaction","Remove");
                DATABASE_REFERENCE.child("friend_request").child("receive").child(usersArrayList.get(position).getUserid()).child(FIREBASE_AUTH.getCurrentUser().getUid()).removeValue();
                DATABASE_REFERENCE.child("friend_request").child("sent").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(usersArrayList.get(position).getUserid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("sentreq","Delete");
                        notifyItemRemoved(position);

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button button;
        TextView textView;
        CircleImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_freq_s_img);
            textView = itemView.findViewById(R.id.txt_freq_s_name);
            button = itemView.findViewById(R.id.btn_freq_s_dlt);
        }
    }
}
