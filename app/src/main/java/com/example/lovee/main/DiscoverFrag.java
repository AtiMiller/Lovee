package com.example.lovee.main;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lovee.R;
import com.example.lovee.adapters.CardStackAdapter;
import com.example.lovee.models.ModelAllUser;
import com.example.lovee.models.ModelFavUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFrag extends Fragment implements CardStackListener{

    private ImageView ivUserProfile;
    private TextView tvUserName, tvUserLocation, tvUserAge;
    private FloatingActionButton fabDecline, fabReturn, fabStar, fabHeart;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Query query;

    private CardStackView cardStackView;
    private CardStackLayoutManager stackLayoutManager;

    private String userAddress, userEmail, userAge, userDob, userDescription, userInterested, userLat,
    userLon, userLooking, userPhone, userProfile, userRegTime, userName, userGen, userUid;


    private List<ModelAllUser> usersList;

    CardStackAdapter cardStackAdapter;


    public DiscoverFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        usersList = new ArrayList<>();



        cardStackView = view.findViewById(R.id.card_stack_view);
        stackLayoutManager = new CardStackLayoutManager(getActivity(), this);
        stackLayoutManager.setStackFrom(StackFrom.Bottom);
        stackLayoutManager.setVisibleCount(3);
        stackLayoutManager.setDirections(Direction.HORIZONTAL);
        stackLayoutManager.setSwipeThreshold(0.1f);
        stackLayoutManager.setCanScrollHorizontal(true);
        stackLayoutManager.setCanScrollVertical(false);
        cardStackView.setLayoutManager(stackLayoutManager);
        getAllUsers();

        ivUserProfile = view.findViewById(R.id.ivUserProfile);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserLocation =view.findViewById(R.id.tvUserLocation);
        tvUserAge = view.findViewById(R.id.tvUserAge);
        fabDecline = view.findViewById(R.id.fabDecline);
        fabReturn = view.findViewById(R.id.fabReturn);
        fabStar = view.findViewById(R.id.fabStar);
        fabHeart = view.findViewById(R.id.fabHeart);

        fabReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardStackView.rewind();
            }
        });

        fabDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SwipeAnimationSetting setting = new  SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Normal.duration)
                        .build();
                stackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });

        fabHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting setting = new  SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Slow.duration)
                        .build();
                stackLayoutManager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });



        fabStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                HashMap<String, Object> favMap = new HashMap<>();
                favMap.put("address", userAddress);
                favMap.put("age", userAge);
                favMap.put("dateOfBirth", userDob);
                favMap.put("description", userDescription);
                favMap.put("email", userEmail);
                favMap.put("gender", userGen);
                favMap.put("interestedIn", userInterested);
                favMap.put("lat", userLat);
                favMap.put("lon", userLon);
                favMap.put("phoneNumber", userPhone);
                favMap.put("profilePicture", userProfile);
                favMap.put("regTime", userRegTime);
                favMap.put("userName", userName);
                favMap.put("lookingFor", userLooking);
                favMap.put("uid", userUid);

                reference.child(user.getUid()).child("Favorites").push().setValue(favMap);
            }
        });


        return view;
    }

    private void getAllUsers() {

        query = reference.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    ModelAllUser modelAllUser = ds.child("UserInfo").getValue(ModelAllUser.class);

                    if(!modelAllUser.getEmail().equals(user.getEmail())){
                        usersList.add(modelAllUser);
                        cardStackAdapter = new CardStackAdapter(usersList, getActivity());
                        cardStackView.setAdapter(cardStackAdapter);
                    }
                    else if(modelAllUser.getEmail().equals(user.getEmail())){

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

        userEmail = usersList.get(position).getEmail();
        userAge= usersList.get(position).getAge();
        userAddress = usersList.get(position).getAddress();
        userDob = usersList.get(position).getDateOfBirth();
        userDescription = usersList.get(position).getDescription();
        userInterested = usersList.get(position).getInterestedIn();
        userLat = usersList.get(position).getLat();
        userLon = usersList.get(position).getLon();
        userLooking = usersList.get(position).getLookingFor();
        userPhone = usersList.get(position).getPhoneNumber();
        userProfile = usersList.get(position).getProfilePicture();
        userRegTime = usersList.get(position).getRegistrationTime();
        userName = usersList.get(position).getUserName();
        userGen = usersList.get(position).getGender();
        userUid = usersList.get(position).getUid();

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}


