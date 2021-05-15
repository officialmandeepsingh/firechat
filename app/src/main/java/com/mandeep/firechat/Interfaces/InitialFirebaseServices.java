package com.mandeep.firechat.Interfaces;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public interface InitialFirebaseServices {
    FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    DatabaseReference DATABASE_REFERENCE = FIREBASE_DATABASE.getReference();
    FirebaseStorage FIREBASE_STORAGE = FirebaseStorage.getInstance();
    StorageReference STORAGE_REFERENCE = FIREBASE_STORAGE.getReference();
    FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    FirebaseUser FIREBASE_USER = FIREBASE_AUTH.getCurrentUser();
}
