package com.example.lovee.login;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.lovee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyFrag extends Fragment {

    private Bundle bundle;
    private String phoneNumber;
    private String verificationId;
    private FirebaseAuth mAuth;
    private EditText etVerificationCode;
    private Toolbar toolbarVerify;
    private Button btnSubmit;
    private TextView tvResend;

    public VerifyFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify, container, false);

        //FindViewByIdsInit
        etVerificationCode = view.findViewById(R.id.etVerificationCode);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        tvResend = view.findViewById(R.id.tvResend);
        toolbarVerify = view.findViewById(R.id.toolbar_verify);

        //Toolbar
        toolbarV(toolbarVerify);
        mAuth = FirebaseAuth.getInstance();

        //Getting the phone number from previous fragment's EditText.
        bundle = getArguments();
        if(bundle != null){
            phoneNumber = bundle.getString("phoneNumber");
            sendVerificationCode(phoneNumber);
            Toast.makeText(getActivity(), "" + phoneNumber, Toast.LENGTH_SHORT).show();
        }

        //Called when the user clicks on the Resend Text
        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
            }
        });

        //Called when the user clicks on the Submit Button.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etVerificationCode.getText().toString().trim();

                if(code.length()<6 ){
                    etVerificationCode.setError("Verification code is to short");
                    etVerificationCode.requestFocus();
                }else {
                    verifyCode(code);
                }
            }
        });

        return view;
    }

    private Toolbar toolbarV (Toolbar toolbarVerify){
        getActivity().setActionBar(toolbarVerify);
        toolbarVerify.setTitle("");
        toolbarVerify.setNavigationIcon(R.drawable.back_arrow_icon_black);
        toolbarVerify.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftwareKeyboard(false);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                Fragment fragment = new RegisterFrag();
//                fragmentManager.beginTransaction()
//                        .setCustomAnimations(R.anim.enter_from_left_frag, R.anim.exit_to_right_frag)
//                        .replace(R.id.logInFrag, fragment)
//                        .commit();
            }
        });
        return toolbarVerify;
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredetial(credential);
    }

    private void signInWithCredetial(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            Fragment fragment = new CreateProfileFrag();
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            fragmentManager.beginTransaction()
//                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                    .replace(R.id.logInFrag, fragment)
//                                    .addToBackStack(null)
//                                    .commit();
                        }else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){
        Toast.makeText(getActivity(), "Sending verification number", Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(getActivity(), "On code Sent", Toast.LENGTH_SHORT).show();
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(getActivity(), "On verification completed", Toast.LENGTH_SHORT).show();
            if(code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    private void showSoftwareKeyboard(boolean showKeyboard){
        final FragmentActivity activity = getActivity();
        final InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), showKeyboard ? InputMethodManager.SHOW_FORCED : InputMethodManager.HIDE_NOT_ALWAYS);
    }



}
