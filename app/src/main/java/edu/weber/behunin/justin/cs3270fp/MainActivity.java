package edu.weber.behunin.justin.cs3270fp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements
        PlanFragment.SignOutAction,
        SignInFragment.SignedInAction,
        PlanRecylcerAdapter.OnPlanClicked{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testFirebase();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            //if user not signed in goto sign fragment
            userSignIn();

        }else{
            loadPlanFragment();
        }

    }

    private void loadPlanFragment() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.frag1, new PlanFragment(), "FragPlan")
                .addToBackStack(null).commit();
    }

    private void userSignIn() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.frag1, new SignInFragment(), "FragSignIn")
                .addToBackStack(null).commit();
    }

    private void testFirebase(){

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Read from the database
       mDatabase.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Course value = child.getValue(Course.class);
                    Log.d("test", "Course: " + value.getCourseID());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("test", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void signout() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        userSignIn();
    }

    @Override
    public void signedIn() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        loadPlanFragment();
    }

    @Override
    public void planClickAction(Plan plan) {
        //TODO add on plan clicked action goto semester fragment
    }
}
