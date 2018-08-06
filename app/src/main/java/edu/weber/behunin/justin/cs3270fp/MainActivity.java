package edu.weber.behunin.justin.cs3270fp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        PlanFragment.OnPlanAction,
        SignInFragment.SignedInAction,
        PlanRecyclerAdapter.OnPlanClicked,
        SemesterFragment.OnSemesterAction,
        ConfirmDeletePlanDialog.PlanDeleteConfirmed,
        SemesterRecyclerAdapter.OnSemesterClicked,
        CourseFragment.OnCourseAction,
        CourseRecylerAdapter.OnCourseClicked{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        mDatabase.child(currentUser.getUid()).child(plan.getPlanName()).removeValue();
        doneWithPlan();
    }

    @Override
    public void confirmSemesterDelete(Semester semester, Plan plan) {
        //TODo create confirmation dialog and delete semester if confirmed.
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
        mDatabase.child(currentUser.getUid()).child(plan.getPlanName())
                .setValue(plan);
        ArrayList<Semester> semesters = new ArrayList<>();
        semesters = plan.getSemesterList();
        for(Semester s : semesters){
            Log.d("test", s.getSemesterName());
        }
        Log.d("test", semester.getSemesterName() + " deleted");
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
    public void deletionConfirmed(Plan plan) {
        deletePlan(plan);
    }

    @Override
    public void semesterClickAction(Semester semester, Plan plan) {
        CourseFragment courseFragment = new CourseFragment();
        courseFragment.setPlanValues(semester, plan);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frag1, courseFragment, "fragCourse")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void courseClickedAction(Course course, Semester semester, Plan plan) {
        //TODO create action when course is clicked.
    }
}
