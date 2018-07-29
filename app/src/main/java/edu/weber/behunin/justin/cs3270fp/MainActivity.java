package edu.weber.behunin.justin.cs3270fp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        testFirebase();

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

    private void testFirebase(){


        Course course = new Course("CS3620", "TestCourse", false);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("courses").child(course.getCourseName()).setValue(course);

        course = new Course("CS4250", "Test Course 2", true);

        mDatabase.child("courses").child(course.getCourseName()).setValue(course);

        course = new Course("CS4720", "Test Course 3", true);

        mDatabase.child("courses").child(course.getCourseName()).setValue(course);

        // Read from the database
       mDatabase.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot child = dataSnapshot.child("TestCourse");
                Log.d("test", "" +
                        Objects.requireNonNull(child.getValue(Course.class)).getCourseName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.d("test", "Failed to read value.", error.toException());
            }
        });

        ArrayList<String> courseList = new ArrayList<>();
        courseList.add("CS3100");
        courseList.add("CS2440");
        courseList.add("CS1400");

        Prerequisite prerequisite = new Prerequisite("CS4100",courseList);

        mDatabase.child("prereqs").child("CS4100").setValue(prerequisite);

    }
}
