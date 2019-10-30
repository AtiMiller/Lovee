package com.example.lovee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.lovee.main.DiscoverFrag;
import com.example.lovee.main.MessagesFrag;
import com.example.lovee.main.favorites.FavContainerFrag;
import com.example.lovee.main.nearby.NearContainerFrag;
import com.example.lovee.main.profile.MyProfileFrag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linLayFavorites, linLayNearby, linLayMessages, linLayMyProfile;
    FloatingActionButton fab;
    private Toolbar toolbar;
    private View decorView;

    private String mUserName;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        //Toolbar
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        Fragment startFrag = new DiscoverFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, startFrag);
        ft.commit();

        linLayFavorites = findViewById(R.id.linLayFavorites);
        linLayNearby = findViewById(R.id.linLayNearby);
        linLayMessages = findViewById(R.id.linLayMessages);
        linLayMyProfile = findViewById(R.id.linLayMyProfile);
        fab = findViewById(R.id.discoverFab);

        linLayFavorites.setOnClickListener(this);
        linLayNearby.setOnClickListener(this);
        linLayMessages.setOnClickListener(this);
        linLayMyProfile.setOnClickListener(this);
        fab.setOnClickListener(this);

        getInfo();
        hideBottomNavigation ();

    }

    private void getInfo() {
        query = reference.orderByKey().equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    mUserName = "" + ds.child("UserInfo/userName").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSignOut:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                finish();
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linLayFavorites:
                showFavFragContainer();
                break;
            case R.id.linLayNearby:
                showNearFragContainer();
                break;
            case R.id.linLayMessages:
                showMessageFrag();
                break;
            case R.id.linLayMyProfile:
                showProfileFrag();
                break;
            case R.id.discoverFab:
                showDisFrag();
                break;

        }
    }

    private void showFavFragContainer(){

        toolbar.setTitle("Favorites");
        Fragment FavFrag = new FavContainerFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, FavFrag);
        ft.commit();
    }

    private void showNearFragContainer(){

        toolbar.setTitle("Nearby");
        Fragment NearFrag = new NearContainerFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, NearFrag);
        ft.commit();
    }

    private void showDisFrag(){

        toolbar.setTitle("Discover");
        Fragment DisFrag = new DiscoverFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, DisFrag);
        ft.commit();
    }

    private void showMessageFrag(){

        toolbar.setTitle("Messages");
        Fragment MesFrag = new MessagesFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, MesFrag);
        ft.commit();
    }

    private void showProfileFrag(){

        toolbar.setTitle(mUserName);
        Fragment ProfFrag = new MyProfileFrag();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragContainer, ProfFrag);
        ft.commit();
    }

    public void hideBottomNavigation (){
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if (i == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    public int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    }
}
