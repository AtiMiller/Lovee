package com.example.lovee.main.favorites;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovee.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUserFrag extends Fragment {

    ArrayList<Integer> personImages = new ArrayList<>();
    List<String> personName;

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    public NewUserFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_new_users, container, false);

        personName = Arrays.asList("Person1", "Person2", "Person3", "Person4");
        personImages.add(R.drawable.online_circle);
        personImages.add(R.drawable.person2);
        personImages.add(R.drawable.person3);
        personImages.add(R.drawable.person4);
        recyclerView = view.findViewById(R.id.recyclerView_newUser);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

//        FavAdapter favAdapter = new FavAdapter(personName, personImages, getActivity());
//        recyclerView.setAdapter(favAdapter); // set the Adapter to RecyclerView

        return view;
    }

}
