package com.mandeep.firechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mandeep.firechat.Fragments.AllUsers;
import com.mandeep.firechat.Fragments.Authentication;
import com.mandeep.firechat.Fragments.Dashboard;
import com.mandeep.firechat.Fragments.Splash;
import com.mandeep.firechat.Fragments.settings;

public class MainActivity extends AppCompatActivity  {
    FragmentManager fragmentManager,manager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("chechstatus","MainActivity");
        fragmentManager=getSupportFragmentManager();
        manager = getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragment=new Authentication();
        fragmentTransaction.add(R.id.myframe,fragment,"splash");
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.mainmenulist, menu);
            return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main_all_btn){
            Log.d("mytool"," All USers ");
            Toast.makeText(this, " All USers ", Toast.LENGTH_SHORT).show();
//            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            fragment = new AllUsers();
            transaction.replace(R.id.myframe,fragment);
            transaction.addToBackStack("All Users");
            transaction.commit();
            return true;
        }else if(id == R.id.main_logout_btn){
            Log.d("mytool"," Logout ");
            mUserSignOut();
            return true;
        }else if(id == R.id.main_setting_btn){
            Log.d("mytool"," Setting ");
            fragment = new settings();
//            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.myframe,fragment);
            transaction.addToBackStack("Setting");
            transaction.commit();
            return true;
        }else if(id == R.id.main_home){
            fragment = new Dashboard();
//            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.myframe,fragment);
            transaction.addToBackStack("Home");
            transaction.commit();
            return true;
        }
        return false;
    }

    private void mUserSignOut() {
        FirebaseAuth.getInstance().signOut();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment = new Authentication();
        transaction.replace(R.id.myframe,fragment);
//        fragmentTransaction.addToBackStack("Authentication");
        manager.popBackStack();
        transaction.commit();
    }
}
