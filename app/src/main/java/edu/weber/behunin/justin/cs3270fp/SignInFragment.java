package edu.weber.behunin.justin.cs3270fp;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FirebaseAuth mAuth;
    private View root;
    private TextInputEditText txtEmail, txtPassword;
    private Button btnSignIn, btnCreate;
    private TextView txtError;
    private SignedInAction mCallback;

    interface SignedInAction {
        void signedIn();
    }

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sign_in, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SignedInAction) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement SignedInActionInterface");

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        txtEmail = root.findViewById(R.id.inputEmail);
        txtPassword = root.findViewById(R.id.inputPassword);
        btnSignIn = root.findViewById(R.id.buttonSignIn);
        btnCreate = root.findViewById(R.id.buttonCreate);
        txtError = root.findViewById(R.id.txtError);

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
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //if succesfull go to plan fragment
                                mCallback.signedIn();
                            } else {
                                //if sign in fails, display a message to user
                                Log.d("test", "Create user failed " + task.getException());
                                txtError.setText(R.string.create_user_failed);
                            }
                        }
                    });

        } else {
            txtError.setText(R.string.invalid_input);
        }
    }

    private void signInUser() {

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (validInput()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Sign in successful go to plan fragment.
                                mCallback.signedIn();
                            } else {
                                Log.d("test", "Sign in failed " + task.getException());
                                txtError.setText(R.string.failed_signin);
                            }
                        }
                    });
        } else {
            txtError.setText(R.string.invalid_input);
        }
    }

    private boolean validInput() {

        //todo add input validation

        return true;
    }
}
