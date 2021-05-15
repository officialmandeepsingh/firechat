package com.mandeep.firechat.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
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
public class settings extends Fragment implements View.OnClickListener {
    CircleImageView imageView;
    MaterialEditText editText;
    Button button;
    Context context;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FragmentManager fragmentManager;
    public settings() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        onFragmentAttach();
    }

    private void onFragmentAttach() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        initID(view);
        return view;
    }

    private void initID(View v) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        button = v.findViewById(R.id.btn_sett_submit);
        editText = v.findViewById(R.id.ed_sett_status);
        imageView = v.findViewById(R.id.imgv_sett_userpic);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        storageReference = firebaseStorage.getReference().child("profile");

        fragmentManager = getFragmentManager();
        loadPicture();
        loadStatus();

        imageView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    private void loadStatus() {
        databaseReference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("mydata",dataSnapshot.child("status").getValue().toString());
                editText.setText(dataSnapshot.child("status").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPicture() {
        storageReference.child(auth.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(getActivity(), new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).noPlaceholder().centerCrop().fit()
                        .into(imageView);
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
            imageView.setImageBitmap(imageBitmap);

        }else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Picasso.with(context).load(data.getData()).fit().into(imageView);

        }else if(requestCode==1001 && resultCode == Activity.RESULT_OK){
            mSelectImage();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == button){
            mSaveUserPicture();
        }else if( v == imageView){
            mCheckUserPermission();
        }
    }

    private void mSaveUserPicture() {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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
                                        Log.d("mydata",link);
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
        hashMap.put("status",editText.getText().toString());
        hashMap.put("pic_url",picurl);
        databaseReference.child(auth.getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, " Profile Updated ", Toast.LENGTH_SHORT).show();
                    if(fragmentManager != null){
                        Log.d("myuserid","Manager have");
                    }else{
                        Log.d("myuserid","Manager Not  have");
                    }
                }
            }
        });
    }
}
