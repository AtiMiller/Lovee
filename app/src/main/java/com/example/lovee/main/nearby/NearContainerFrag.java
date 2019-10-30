package com.example.lovee.main.nearby;


import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovee.adapters.VPAdapterNear;
import com.example.lovee.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearContainerFrag extends Fragment {

    private ViewPager viewPager;
    private TabLayout favTab;
    private SearchView searchFavFrag;

    public NearContainerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nearby, container, false);

        viewPager = view.findViewById(R.id.view_pager_near);
        viewPager.setAdapter(new VPAdapterNear(getChildFragmentManager()));

        searchFavFrag = view.findViewById(R.id.searchViewNearFrag);
        favTab = view.findViewById(R.id.nearTab);
        favTab.setupWithViewPager(viewPager);

        return view;
    }

}
