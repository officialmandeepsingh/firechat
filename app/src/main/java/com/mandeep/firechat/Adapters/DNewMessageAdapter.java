package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.firechat.DialogFragments.dlog_NewMessage;
import com.mandeep.firechat.Fragments.UserChatFragment;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DNewMessageAdapter extends RecyclerView.Adapter<DNewMessageAdapter.MyViewHolder> {
    Context context;
    ArrayList<AllUserModel> allUserModels;
    int size;
    dlog_NewMessage dlog_newMessage;

    public DNewMessageAdapter(Context context, ArrayList<AllUserModel> allUserModels, int size, dlog_NewMessage dlog_newMessage) {
        this.context = context;
        this.allUserModels = allUserModels;
        this.size = size;
        this.dlog_newMessage = dlog_newMessage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dcus_newmessage,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Picasso.with(context).load(allUserModels.get(position).getPic_url()).into(holder.imageView);
        holder.textView.setText(allUserModels.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlog_newMessage.dismiss();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putCharSequence("uid",allUserModels.get(position).getUserid().toString());
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
        TextView textView;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dcus_mess_pic);
            textView = itemView.findViewById(R.id.dcus_mess_uname);
            button = itemView.findViewById(R.id.dcus_mess_btn);
        }
    }
}
