package edu.weber.behunin.justin.cs3270fp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        PlanFragment.OnPlanAction,
        SignInFragment.SignedInAction,
        PlanRecyclerAdapter.OnPlanClicked,
        SemesterFragment.OnSemesterAction,
        ConfirmDeletePlanDialog.PlanDeleteConfirmed,
        SemesterRecyclerAdapter.OnSemesterClicked,
        CourseFragment.OnCourseAction,
        CourseRecylerAdapter.OnCourseClicked,
        ConfirmDeleteSemesterDialog.OnConfirmedSemesterAction {

    private FirebaseAuth mAuth;
    private ArrayList<Course> courseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(getString(R.string.course_data)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Course course = child.getValue(Course.class);
                    courseList.add(course);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            //if user not signed in goto sign fragment
            userSignIn();

        } else {
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

    @Override
    public void signOut() {
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
        SemesterFragment semesterFragment = new SemesterFragment();
        semesterFragment.setPlan(plan);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frag1, semesterFragment, "fragSemester")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createPlan() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frag1, new PlanDialogFragment(), "fragPlanDialog")
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    @Override
    public void createSemester(Plan plan) {
        SemesterDialogFragment semesterDialog = new SemesterDialogFragment();
        semesterDialog.setPlan(plan);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frag1, semesterDialog, "dialogSemester")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void confirmDeletePlan(Plan plan) {
        ConfirmDeletePlanDialog deletePlanDialog = new ConfirmDeletePlanDialog();
        deletePlanDialog.setCancelable(false);
        deletePlanDialog.setPlan(plan);
        deletePlanDialog.show(getSupportFragmentManager(), "dialogDelete");
    }

    public void deletePlan(Plan plan) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mDatabase.child(currentUser.getUid()).child(plan.getPlanName()).removeValue();
        }
        doneWithPlan();
    }

    @Override
    public void confirmSemesterDelete(Semester semester, Plan plan) {
        ConfirmDeleteSemesterDialog deleteSemesterDialog = new ConfirmDeleteSemesterDialog();
        deleteSemesterDialog.setPlanValues(plan, semester);
        deleteSemesterDialog.setCancelable(false);
        deleteSemesterDialog.show(getSupportFragmentManager(), "dialogDeleteSemester");
    }

    @Override
    public void semesterDeletionConfirmed(Plan plan, Semester semester) {
        deleteSemester(semester, plan);
    }

    @Override
    public void doneWithSemester(Plan plan) {
        SemesterFragment semesterFragment = new SemesterFragment();
        semesterFragment.setPlan(plan);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frag1, semesterFragment, "fragSemester")
                .addToBackStack(null)
                .commit();

    }

    private void deleteSemester(Semester semester, Plan plan) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        plan.deleteSemester(semester);
        if (currentUser != null) {
            mDatabase.child(currentUser.getUid()).child(plan.getPlanName())
                    .setValue(plan);
        }
        doneWithSemester(plan);
    }

    @Override
    public void doneWithPlan() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frag1, new PlanFragment(), "fragPlanDialog")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void planDeletionConfirmed(Plan plan) {
        deletePlan(plan);
    }

    @Override
    public void semesterClickAction(Semester semester, Plan plan) {
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setPlanValues(semester, plan, courseList);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frag1, courseFragment, "fragCourse")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void courseClickedAction(Course course, Semester semester, Plan plan) {
        CourseDialogFragment courseDialogFragment = new CourseDialogFragment();
        courseDialogFragment.setValues(plan, semester, course);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.frag1, courseDialogFragment, "dialogSCourse")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}
