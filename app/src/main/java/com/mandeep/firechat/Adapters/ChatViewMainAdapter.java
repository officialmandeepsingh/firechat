package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.firechat.Fragments.UserChatFragment;
import com.mandeep.firechat.Models.MainChatViewModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewMainAdapter extends RecyclerView.Adapter<ChatViewMainAdapter.MyViewHolder> {
    ArrayList<MainChatViewModel> uidArrayList;
    int size=0;
    Context context;


    public ChatViewMainAdapter(ArrayList<MainChatViewModel> uidArrayList, int size, Context context) {
        this.uidArrayList = uidArrayList;
        this.size = size;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cus_userchats,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtname.setText(uidArrayList.get(position).getName());
        holder.txtmess.setText(uidArrayList.get(position).getMessage());
        holder.txtstatus.setText(uidArrayList.get(position).getAccstatus());
        holder.txtdate.setText(uidArrayList.get(position).getTime());
        Picasso.with(context).load(uidArrayList.get(position).getPic_url()).into(holder.imageView);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " Clicked On : "+uidArrayList.get(position).getUid(), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("uid",uidArrayList.get(position).getUid().toString());
                Fragment newFragment = new UserChatFragment();
                newFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.myframe, newFragment).addToBackStack("user chat").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView txtname,txtmess,txtdate,txtstatus;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.cuschat_userpic);
            txtname = itemView.findViewById(R.id.cuschat_username);
            txtmess = itemView.findViewById(R.id.cuschat_usermsg);
            txtdate = itemView.findViewById(R.id.cuschat_usertime);
            txtstatus = itemView.findViewById(R.id.cuschat_useronline);
        }
    }
}
