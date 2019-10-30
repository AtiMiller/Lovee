package com.example.lovee.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lovee.main.nearby.AllNearbyFrag;
import com.example.lovee.main.nearby.InCityFrag;
import com.example.lovee.main.nearby.NewNearbyFrag;

public class VPAdapterNear extends FragmentPagerAdapter {

    public VPAdapterNear(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllNearbyFrag();
            case 1:
                return new InCityFrag();
            case 2:
                return new NewNearbyFrag();

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

        String tabTitles[] = new String[]{"All", "In City", "New"};

        return tabTitles[position];
    }
}
