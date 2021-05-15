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

import com.mandeep.firechat.Interfaces.InitialFirebaseServices;
import com.mandeep.firechat.Models.AddFriendModel;
import com.mandeep.firechat.Models.AllUserModel;
import com.mandeep.firechat.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendReqReceiveAdapter extends RecyclerView.Adapter<FriendReqReceiveAdapter.MyViewHolder> implements InitialFirebaseServices {

    ArrayList<AllUserModel> allUserModels;
    int size;
    Context context;

    public FriendReqReceiveAdapter(ArrayList<AllUserModel> allUserModels, int size, Context context) {
        this.allUserModels = allUserModels;
        this.size = size;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cus_friendreq_receive,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Log.d("sentreq","List User Name : "+allUserModels.get(position).getName());
        holder.textView.setText(allUserModels.get(position).getName());
        Picasso.with(context).load(allUserModels.get(position).getPic_url()).into(holder.imageView);

        holder.btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
                Date date = new Date();
                AddFriendModel friendModel = new AddFriendModel(allUserModels.get(position).getUserid(),dateFormat.format(date));
                Log.d("addfriend",friendModel.getDate());
                // add Friends
                DATABASE_REFERENCE.child("friend").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(allUserModels.get(position).getUserid()).setValue(friendModel);

                //DATABASE_REFERENCE.child("friend").child(allUserModels.get(position).getUserid()).child(FIREBASE_USER.getUid()).setValue(friendModel);
                // Remove request Receive Current USer


                DATABASE_REFERENCE.child("friend_request").child("receive").child(FIREBASE_AUTH.getCurrentUser().getUid()).child(allUserModels.get(position).getUserid()).removeValue();
                // Remove request send by User B to me
                DATABASE_REFERENCE.child("friend_request").child("sent").child(allUserModels.get(position).getUserid()).child(FIREBASE_AUTH.getCurrentUser().getUid()).removeValue();

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button btnaccept,btndecline;
        TextView textView;
        CircleImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnaccept = itemView.findViewById(R.id.btn_freq_r_acpt);
            btndecline = itemView.findViewById(R.id.btn_freq_r_decline);
            textView = itemView.findViewById(R.id.txt_freq_r_name);
            imageView = itemView.findViewById(R.id.img_freq_r_img);
        }
    }
}
