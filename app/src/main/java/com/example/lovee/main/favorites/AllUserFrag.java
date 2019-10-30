package com.example.lovee.main.favorites;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovee.R;
//import com.example.lovee.adapters.FavAdapter;
import com.example.lovee.adapters.FavAdapter;
import com.example.lovee.models.ModelFavUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllUserFrag extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    List<ModelFavUser> favUserList;
    FavAdapter favAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Query query;

    public AllUserFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        favUserList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView_allUser);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        favAdapter = new FavAdapter(favUserList, getActivity());
        recyclerView.setAdapter(favAdapter);

        getAllUsers();
        return view;
    }

//    private void getFavData() {
//
//        adapter=new FavAdapter(favUserList, getActivity());
//        recyclerView.setAdapter(adapter);
//    }


//
//    private void getAllUsers() {
//
//        query = reference.orderByKey().equalTo(user.getUid());
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot ds: dataSnapshot.getChildren()) {
//
//                    ModelFavUser favModel = ds.child("Favorites").getValue(ModelFavUser.class);
////
//                    favUserList.add(favModel);
//
//                    FavAdapter favAdapter = new FavAdapter(favUserList, getActivity());
//                    recyclerView.setAdapter(favAdapter); // set the Adapter to RecyclerView
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void getAllUsers() {

        query = reference.orderByKey().equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favUserList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

//                    String key = (String) ds.child("Favorites").getValue();
//                    Toast.makeText(getActivity(), ""+key, Toast.LENGTH_SHORT).show();

                    ModelFavUser favUser = ds.child("Favorites").getValue(ModelFavUser.class);

                        favUserList.add(favUser);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
