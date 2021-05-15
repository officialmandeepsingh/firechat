package com.mandeep.firechat.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mandeep.firechat.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetails extends Fragment implements View.OnClickListener {
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Context context;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private MaterialEditText edstatus;
    private CircleImageView circleImageView;
    private Button button;


    public UserDetails() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user_details, container, false);
        initID(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("User Profile");
        return view;
    }

    private void initID(View view) {

        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("users");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference("profile");
        edstatus=view.findViewById(R.id.ed_userd_status);
        button=view.findViewById(R.id.btn_userd_next);
        circleImageView=view.findViewById(R.id.imgv_userd_profilepic);

        button.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        mLoadDefaultDisplayPicture();

    }

    private void mLoadDefaultDisplayPicture() {
        storageReference.child("nopic.jpg").getDownloadUrl().addOnSuccessListener(getActivity(), new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).noPlaceholder().centerCrop().fit()
                        .into(circleImageView);

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==circleImageView){
            mCheckUserPermission();
//            mSelectImage();
        }else if(v==button){
            mSaveUserPicture();
        }
    }

    private void mSaveUserPicture() {

        Bitmap bitmap = ((BitmapDrawable) circleImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        final StorageReference storageRef = storageReference.child(auth.getCurrentUser().getUid()+".jpg");
        byte[] data=stream.toByteArray();
        UploadTask uploadTask=storageRef.putBytes(data);

        uploadTask.addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            storageRef.getDownloadUrl().addOnCompleteListener(getActivity(), new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        String link = task.getResult().toString();
                                         Log.d("mypic",link);
                                         mSaveStatusInDatabase(link);
                                    }
                                }
                            });

                        }
                    }
                });


            }
        });

    }

    private void mSaveStatusInDatabase(String picurl) {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",edstatus.getText().toString());
        hashMap.put("accstatus","complete");
        hashMap.put("pic_url",picurl);
        hashMap.put("isverified","yes");
        hashMap.put("userid",auth.getCurrentUser().getUid());
        reference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, " Profile Updated ", Toast.LENGTH_SHORT).show();
                     Fragment fragment = new Dashboard();
                    FragmentManager fragmentManager=getFragmentManager();
                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.myframe,fragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void mCheckUserPermission() {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},1001);
        }else{
            mSelectImage();
        }
    }

    // MEthod For Dialog
    private void mSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
             builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);

                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            startActivityForResult(intent, 1);

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });


        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode== Activity.RESULT_OK){

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            circleImageView.setImageBitmap(imageBitmap);

        }else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Picasso.with(context).load(data.getData()).fit().into(circleImageView);

        }else if(requestCode==1001 && resultCode == Activity.RESULT_OK){
            mSelectImage();
        }
    }
}
