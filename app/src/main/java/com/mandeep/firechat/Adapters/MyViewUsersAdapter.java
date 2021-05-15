package com.mandeep.firechat.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mandeep.firechat.Fragments.AllUsers;
import com.mandeep.firechat.Fragments.RegUserProfile;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewUsersAdapter extends RecyclerView.Adapter<MyViewUsersAdapter.ViewHolder> {
    ArrayList<AllUserModel> finalmap;
    Context context;
    int size=0;
    View view ;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public MyViewUsersAdapter(ArrayList<AllUserModel> finalmap, int size, Context context) {
        this.finalmap = finalmap;
        this.size = size;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View customv = layoutInflater.inflate(R.layout.cus_viewallusers, parent, false);
        ViewHolder myViewHolder=new ViewHolder(customv);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(finalmap.get(position).getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Log.d("myauthcheck"," All Users FBind Value UID : "+finalmap.get(position).getUserid());
                Bundle bundle = new Bundle();
                bundle.putCharSequence("uid",finalmap.get(position).getUserid().toString());
                Fragment newFragment = new RegUserProfile();
                newFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.myframe, newFragment).addToBackStack(null).commit();

            }
        });
        Picasso.with(context).load(finalmap.get(position).getPic_url()).into(holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        CircleImageView circleImageView;
        TextView textView;
        View view ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView ;
            circleImageView = itemView.findViewById(R.id.img_viewreg_userpic);
            textView = itemView.findViewById(R.id.txt_viewreg_username);
        }


    }
}
