package com.example.lovee.login;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lovee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreenFrag extends Fragment {

    public View view;

    public SplashScreenFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_splash_screen, container, false);

        FrameLayout splashFrame = view.findViewById(R.id.splashFrame);

        splashFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag2 = new LogInFrag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right_frag, R.anim.exit_to_left_frag);
                ft.replace(R.id.frag_container, frag2);
                ft.remove(SplashScreenFrag.this);
                ft.commit();
            }
        });

        return view;
    }


}
