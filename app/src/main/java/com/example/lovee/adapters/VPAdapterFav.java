package com.example.lovee.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lovee.main.favorites.AllUserFrag;
import com.example.lovee.main.favorites.NewUserFrag;
import com.example.lovee.main.favorites.SpotlightFrag;


public class VPAdapterFav extends FragmentPagerAdapter {

    public VPAdapterFav(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AllUserFrag();
            case 1:
                return new SpotlightFrag();
            case 2:
                return new NewUserFrag();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String[] tabTitles = new String[]{"All", "Spotlight", "New"};

        return tabTitles[position];
    }
}
