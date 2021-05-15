package com.mandeep.firechat.Adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mandeep.firechat.Models.MessageModel;
import com.mandeep.firechat.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    ArrayList<MessageModel> messageModelArrayList;
    int size;
    Context context;

    public MessageAdapter(ArrayList<MessageModel> messageModelArrayList, int size, Context context) {
        this.messageModelArrayList = messageModelArrayList;
        this.size = size;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cus_userchatmessage,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(messageModelArrayList.get(position).getFrom().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;

            holder.textView.setLayoutParams(params);
            holder.textView.setTextSize(14);
            holder.textView.setText(messageModelArrayList.get(position).getMessage());
        }else{
            Notify(messageModelArrayList.get(position).getFrom(),messageModelArrayList.get(position).getMessage());
            holder.textView.setTextSize(14);
            holder.textView.setText(messageModelArrayList.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_userchatmessage);
        }
    }

    private void Notify(String title, String body) {

        /*int notification_id = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Simple Notification")
                .setContentText("This is a example of Simple Notification")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelid = "abc";
            NotificationChannel notificationChannel = new NotificationChannel(channelid, " welcome", 	NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId(channelid);

        }
        notificationManager.notify(notification_id, builder.build());*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelid = "abc";
            NotificationChannel notificationChannel = new NotificationChannel(channelid, " welcome", 	NotificationManager.IMPORTANCE_DEFAULT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message_black_24dp)
                    .setContentText(body);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(2,builder.build());
        }else{

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.ic_message_black_24dp)
                    .setContentText(body);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(3,builder.build());

        }


    }
}
