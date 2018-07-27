package edu.weber.behunin.justin.cs3270fp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragement extends Fragment {

    FirebaseAuth mAuth;
    View root;
    TextInputEditText txtEmail, txtPassword;
    Button btnSignIn, btnCreate;

    public SignInFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_sign_in_fragement, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        txtEmail = root.findViewById(R.id.inputEmail);
        txtPassword = root.findViewById(R.id.inputPassword);
        btnSignIn = root.findViewById(R.id.buttonSignIn);
        btnCreate = root.findViewById(R.id.buttonCreate);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (validInput()) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //if succesfull go to plan fragment
                                Log.d("test", "user successfully created");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d("test", user.getEmail());
                            } else {
                                //if sign in fails, display a message to user
                                Log.d("test", "Create user failed " + task.getException());
                                Toast.makeText(getActivity(), "Creation Failed", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });

        } else {
            Toast toast = Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void signInUser() {

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (validInput()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sign in successful go to plan fragment.
                                Log.d("test", "Sign in successful");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getActivity(), "Welcome " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("test", "Sign in failed " + task.getException());
                                Toast.makeText(getActivity(), "Unable to sign in",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast toast = Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean validInput() {

        //todo add input validation

        return true;
    }
}
