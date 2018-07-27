package edu.weber.behunin.justin.cs3270fp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //todo add sign out as a menu option once signed in
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            //if user not signed in goto sign fragment
            userSignIn();

        }else{
            Toast toast = Toast.makeText(this, currentUser.getEmail(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void userSignIn() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.frag1, new SignInFragement(), "FragSignIn")
                .addToBackStack(null).commit();
    }
}
