package com.example.lovee.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovee.MainActivity;
import com.example.lovee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFrag extends Fragment {

    private FirebaseAuth mAuth;
    private TextInputEditText etEmailSignIn, etPasswordSignIn;
    private Button btnSignIn;
    private TextView tvCreateNewAccount, tvForgotPassword;
    private CheckBox checkRemember;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    public View view;

    public LogInFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        //Called when the LogIn activity started and checks if the network is avabile.
        isNetworkAvabile();

        
        //FirebaseInit
        mAuth = FirebaseAuth.getInstance();

        //FindViewByIds
        etEmailSignIn = view.findViewById(R.id.etEmailSignIn);
        etPasswordSignIn = view.findViewById(R.id.etPasswordSignIn);
        btnSignIn = view.findViewById(R.id.btnSignIn);
        tvCreateNewAccount = view.findViewById(R.id.tvCreateNewAccount);
        tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        checkRemember = view.findViewById(R.id.checkRemember);

        //Called after the user pressed the Enter
        enter(etPasswordSignIn);

        //Called when button signIn is clicked
        signIn(btnSignIn);

        // Called when the Remember Me checkbox is checked
        rememberMe(checkRemember);

        //Called when the user clicked on the Create Account text
        createAccount(tvCreateNewAccount);

        //Forgot Password messsage invisible
        tvForgotPassword.setVisibility(View.GONE);

        return view;
    }

    private void isNetworkAvabile(){
        connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


    }

    private EditText enter (final EditText etPasswordSignIn){



        etPasswordSignIn.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String email = etEmailSignIn.getText().toString().trim();
                    String password = etPasswordSignIn.getText().toString().trim();

                    if (networkInfo == null) {
                        Toast.makeText(getActivity(), "Please, check your network connection", Toast.LENGTH_SHORT).show();
                    }else if (email.isEmpty() && password.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter your email and password.", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getActivity(), "Please check your email and password.",
                                                    Toast.LENGTH_SHORT).show();
                                            forgotPassword();
                                        }

                                        // ...
                                    }
                                });
                    }
                    return true;
                }
                return false;
            }
        });
        return etPasswordSignIn;
    }

    private Button signIn(Button btnSignIn){

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = etEmailSignIn.getText().toString().trim();
                    String password = etPasswordSignIn.getText().toString().trim();

                    if (networkInfo == null) {
                        Toast.makeText(getActivity(), "Please, check your network connection", Toast.LENGTH_SHORT).show();
                    }else if (email.isEmpty() && password.isEmpty()) {
                        Toast.makeText(getActivity(), "Please enter your email and password.", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getActivity(), "Please check your email and password.",
                                                    Toast.LENGTH_SHORT).show();
                                            forgotPassword();
                                        }

                                        // ...
                                    }
                                });
                    }
                }
            });
        return btnSignIn;
        
    }

    private CheckBox rememberMe(CheckBox checkRemember){

        SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

        if(checkbox.equals("true") && mAuth.getCurrentUser() != null){
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        checkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });
        return checkRemember;
    }

    private TextView createAccount (TextView tvCreateNewAccount){
        tvCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag3 = new RegisterFrag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.enter_from_right_frag, R.anim.exit_to_left_frag);
                ft.replace(R.id.frag_container, frag3);
                ft.addToBackStack("A");
                ft.commit();
            }
        });

        return tvCreateNewAccount;
    }

    private void forgotPassword(){

        tvForgotPassword.setVisibility(View.VISIBLE);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag4 = new ForgotFrag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.frag_container, frag4);
                ft.addToBackStack("A");
                ft.commit();
            }
        });
    }

}
