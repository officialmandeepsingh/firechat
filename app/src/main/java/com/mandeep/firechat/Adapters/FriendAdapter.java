package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.firechat.Fragments.RegUserProfile;
import com.mandeep.firechat.Fragments.UserChatFragment;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewholder> {
    ArrayList<AllUserModel> allUserModels;
    int size;
    Context context;

    public FriendAdapter(ArrayList<AllUserModel> allUserModels, int size, Context context) {
        this.allUserModels = allUserModels;
        this.size = size;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cus_friendall,parent,false);
        MyViewholder myViewholder = new MyViewholder(view);

        return myViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, final int position) {
        holder.txtname.setText(allUserModels.get(position).getName());
        Picasso.with(context).load(allUserModels.get(position).getPic_url()).into(holder.imageView);

        holder.imgcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " imgcall "+allUserModels.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " imgmess "+allUserModels.get(position).getName(), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Log.d("chatwindow","  Value UID : "+allUserModels.get(position).getUserid());
                Bundle bundle = new Bundle();
                bundle.putCharSequence("uid",allUserModels.get(position).getUserid().toString());
                Fragment newFragment = new UserChatFragment();
                newFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.myframe, newFragment).addToBackStack("user chat").commit();
            }
        });

        holder.imgnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " imgnotify "+allUserModels.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgredeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " imgredeye "+allUserModels.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, " imgprofile "+allUserModels.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView txtname,txtstatus;
        ImageView imgmess,imgnotify,imgcall,imgprofile,imgredeye;
        View view;
        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.img_cusfrnd_dp);
            imgcall = itemView.findViewById(R.id.img_cusfrnd_call);
            imgmess = itemView.findViewById(R.id.img_cusfrnd_mess);
            imgnotify = itemView.findViewById(R.id.img_cusfrnd_notice);
            imgredeye = itemView.findViewById(R.id.img_cusfrnd_onlinerem);
            imgprofile = itemView.findViewById(R.id.img_cusfrnd_profile);
            txtname = itemView.findViewById(R.id.txt_cusfrnd_name);
            txtstatus = itemView.findViewById(R.id.txt_cusfrnd_status);
        }
    }
}
