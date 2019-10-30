package com.example.lovee.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFrag extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private long currentTime;
    private EditText etEmailRegister, etPasswordRegister, etPhoneNumber,
            etFullName, etRePasswordRegister;
    private Spinner spinnerCountries;
    private Bundle bundle;

    public RegisterFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //FirebaseInit
        mAuth = FirebaseAuth.getInstance();

        //FirebaseDatabaseInit
        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("Users");

        //FindViewByIdsInit
        TextView tvSignIn = view.findViewById(R.id.tvSignIn);
        etFullName = view.findViewById(R.id.etFullName);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etEmailRegister = view.findViewById(R.id.etEmailRegister);
        etPasswordRegister = view.findViewById(R.id.etPasswordRegister);
        etRePasswordRegister = view.findViewById(R.id.etRePasswordRegister);
        Button btnSignUp = view.findViewById(R.id.btnSignUp);

        //Spinner Setup
        spinnerCountries = view.findViewById(R.id.spinnerCountries);
        spinnerCountries.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner, CountryData.countryNames));

        //ClickListeners

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new LogInFrag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.frag_container, frag);
                ft.commit();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Calling this method verifies the password strenght
//                 ,checking if the fields are edited or not, creates an account with
//                firebase auth and adding data to the database.
                fieldAndPasswordChecker();

//                Fragment frag6 = new CreateProfileFrag();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.setCustomAnimations(R.anim.enter_from_right_frag, R.anim.exit_to_left_frag);
//                ft.replace(R.id.frag_container, frag6);
//                ft.commit();


            }
        });



        return view;
    }


    private void fieldAndPasswordChecker(){

        String password1 = etPasswordRegister.getText().toString();
        String password2 = etRePasswordRegister.getText().toString();
        final String email = etEmailRegister.getText().toString().trim();
        String password = etPasswordRegister.getText().toString().trim();
        Pattern upperCase = Pattern.compile("[A-Z]");
        Pattern lowerCase = Pattern.compile("[a-z]");
        Pattern digitCase = Pattern.compile("[0-9]");

        if (etFullName.getText().toString().isEmpty() || etPhoneNumber.getText().toString().isEmpty()
                || etEmailRegister.getText().toString().isEmpty() || etPasswordRegister.getText().toString().isEmpty() ||
                etRePasswordRegister.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Please enter all fields!", Toast.LENGTH_SHORT).show();
        } else if (etPhoneNumber.getText().toString().isEmpty() ||
                etPhoneNumber.getText().toString().length() < 10 ) {
            etPhoneNumber.setError("Valid number is required");
            etPhoneNumber.requestFocus();
        } else if (!(password1.length() > 6)) {
            Toast.makeText(getActivity(), "Your password is to short.", Toast.LENGTH_SHORT).show();
        } else if(!lowerCase.matcher(password1).find()){
            Toast.makeText(getActivity(), "Your password need at least one lowercase letter.", Toast.LENGTH_SHORT).show();
        } else if (!upperCase.matcher(password1).find()){
            Toast.makeText(getActivity(), "Your password need at least one capital letter.", Toast.LENGTH_SHORT).show();
        } else if (!digitCase.matcher(password1).find()){
            Toast.makeText(getActivity(), "Your password need at least one number", Toast.LENGTH_SHORT).show();
        } else if (!(password1.equals(password2))) {
            Toast.makeText(getActivity(), "Passwords are not the same.", Toast.LENGTH_SHORT).show();
        } else if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(getActivity(), "The email is not right.", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty() && password1.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter all fields!", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                currentTime = Calendar.getInstance().getTimeInMillis();

                                //Adding data to the database
                                String name = etFullName.getText().toString();
                                String number = etPhoneNumber.getText().toString().trim();
                                String code = CountryData.countryAreaCodes[spinnerCountries.getSelectedItemPosition()];
                                String phoneNumber = "+" + code + number;
                                String mUid = user.getUid();
                                String registrationTime = "" + currentTime;


                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    hashMap.put("userName", name);
                                    hashMap.put("phoneNumber", phoneNumber);
                                    hashMap.put("email", email);
                                    hashMap.put("registrationTime", registrationTime);

                                    mDatabaseRef = database.getReference("Users");
                                            mDatabaseRef.child(mUid).child("UserInfo").setValue(hashMap);

                                //Sending data through fragments
                                bundle = new Bundle();
                                bundle.putString("phoneNumber", phoneNumber);

                                //Fragment Navigation
                                Fragment frag6 = new CreateProfileFrag();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.setCustomAnimations(R.anim.enter_from_right_frag, R.anim.exit_to_left_frag);
                                ft.replace(R.id.frag_container, frag6);
                                ft.commit();

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }
    }
    


}
