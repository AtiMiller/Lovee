package com.example.lovee.main.favorites;


import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovee.adapters.VPAdapterFav;
import com.example.lovee.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavContainerFrag extends Fragment {

    private ViewPager viewPager;
    private TabLayout favTab;
    private SearchView searchFavFrag;

    public FavContainerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        viewPager = view.findViewById(R.id.view_pager_fav);
        viewPager.setAdapter(new VPAdapterFav(getChildFragmentManager()));

        searchFavFrag = view.findViewById(R.id.searchFavFrag);
        favTab = view.findViewById(R.id.favTab);
        favTab.setupWithViewPager(viewPager);


        return view;
    }

}
