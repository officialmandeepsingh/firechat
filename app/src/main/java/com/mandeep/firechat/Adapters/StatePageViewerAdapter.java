package com.mandeep.firechat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mandeep.firechat.Fragments.Chat;
import com.mandeep.firechat.Fragments.FriendRequest;
import com.mandeep.firechat.Fragments.Friends;

public class StatePageViewerAdapter extends FragmentStatePagerAdapter {
    Fragment fragment;
    public StatePageViewerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                fragment=new FriendRequest();
                return fragment;
            case 1:
                fragment=new Chat();
                return fragment;
            case 2:
                fragment=new Friends();
                return fragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0 :
                return " Friend Request ";
            case 1 :
                return " Chat ";
            case 2 :
                return " Friends ";
        }
        return null;
    }
}
