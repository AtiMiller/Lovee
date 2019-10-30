package com.example.lovee.main.profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovee.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFrag extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Query query;

    private ImageView ivMyPhotos;
    private FloatingActionButton fabEditProfile, fabAddPhotos;
    private TextView tvMyProfileDoB, tvMyProfileLoc, tvMyProfileDes, tvLookingFor,tvInterestedIn,
            tvPhoneNum;



    public MyProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");


        ivMyPhotos = view.findViewById(R.id.ivMyPhotos);
        fabEditProfile = view.findViewById(R.id.fabEditProfile);
        fabAddPhotos = view.findViewById(R.id.fabAddPhotos);
        tvMyProfileDoB = view.findViewById(R.id.tvMyProfileDoB);
        tvMyProfileLoc = view.findViewById(R.id. tvMyProfileLoc);
        tvMyProfileDes =view.findViewById(R.id.tvMyProfileDes);
        tvLookingFor =view.findViewById(R.id.tvLookingFor);
        tvInterestedIn =view.findViewById(R.id.tvInterestedIn);
        tvPhoneNum =view.findViewById(R.id.tvPhoneNum);

        downloadingInfo();

        return view;
    }

    private void downloadingInfo(){

        query = reference.orderByKey().equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String userName = "" + ds.child("UserInfo/userName").getValue();
                    String phoneNumber = "" + ds.child("UserInfo/phoneNumber").getValue();
                    String address = "" + ds.child("UserInfo/address").getValue();
                    String dob = "" + ds.child("UserInfo/dateOfBirth").getValue();
                    String looking = "" + ds.child("UserInfo/lookingFor").getValue();
                    String interested = "" + ds.child("UserInfo/interestedIn").getValue();
                    String profile = "" + ds.child("UserInfo/profilePicture").getValue();
                    String description = "" + ds.child("UserInfo/description").getValue();

                    tvMyProfileDoB.setText(dob);
                    tvMyProfileLoc.setText(address);
                    tvInterestedIn.setText(interested);
                    tvLookingFor.setText(looking);
                    tvPhoneNum.setText(phoneNumber);
                    tvMyProfileDes.setText(description);

                    try {
                        Picasso.get().load(profile).into(ivMyPhotos);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.app_logo).into(ivMyPhotos);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
