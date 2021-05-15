package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.firechat.Fragments.GroupChatFragment;
import com.mandeep.firechat.Fragments.UserChatFragment;
import com.mandeep.firechat.Models.NewGroupModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupViewsMainAdapter extends RecyclerView.Adapter<GroupViewsMainAdapter.MyViewHolder> {
    Context context;
    int size = 0;
    ArrayList<NewGroupModel> newGroupModelArrayList;

    public GroupViewsMainAdapter(ArrayList<NewGroupModel> newGroupModelArrayList, int size, Context context) {
        this.newGroupModelArrayList = newGroupModelArrayList;
        this.size = size;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cus_group_main,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.txtname.setText(newGroupModelArrayList.get(position).getGroupname());
        Picasso.with(context).load(newGroupModelArrayList.get(position).getGroupicon()).into(holder.imageView);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("uid",newGroupModelArrayList.get(position).getGroupid());
                Fragment newFragment = new GroupChatFragment();
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
        TextView txtmsg,txtname,txtdate,txtstatus;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.gimg_main_grpicon);
            txtdate = itemView.findViewById(R.id.gtxt_main_grpdate);
            txtmsg = itemView.findViewById(R.id.gtxt_main_grpmess);
            txtname = itemView.findViewById(R.id.gtxt_main_grpname);
            txtstatus = itemView.findViewById(R.id.gtxt_main_grpmemOnline);
        }
    }
}
