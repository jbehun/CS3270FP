package edu.weber.behunin.justin.cs3270fp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private CourseRecylerAdapter adapter;
    private TextView txtCourseName;
    private Plan plan;
    private Semester semester;
    private OnCourseAction mCallback;
    private FirebaseAuth mAuth;
    private Spinner spinner;
    private Button button;
    final ArrayList<Course> courses = new ArrayList<>();

    interface OnCourseAction {
        void confirmSemesterDelete(Semester semester, Plan plan);

        void doneWithSemester(Plan plan);

        ;
    }

    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCourseAction) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnCourseAction interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_course, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        if (semester != null) {
            toolbar.setTitle(semester.getSemesterName());
        } else {
            toolbar.setTitle(R.string.edit_semester);
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        txtCourseName = root.findViewById(R.id.txtCourse);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvCourseLIst);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        button = (Button) root.findViewById(R.id.button);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(getString(R.string.course_data)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courses.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Course course = child.getValue(Course.class);
                    courses.add(course);
                    Log.d("test", "Course: " + course.getCourseID());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.actionDelete:
                mCallback.confirmSemesterDelete(semester, plan);
                return true;

            case R.id.actionDone:
                mCallback.doneWithSemester(plan);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDatatebase = FirebaseDatabase.getInstance().getReference();

        Context context = getContext();
        adapter = new CourseRecylerAdapter(plan, semester, new ArrayList<Course>(), recyclerView);

        int columnCount = 1;

        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        mDatatebase.child(currentUser.getUid()).child(plan.getPlanName())
                .addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plan plan = dataSnapshot.getValue(Plan.class);
                if (plan != null) {
                    Semester s = plan.getSemester(semester);
                    adapter.addValue(s);
                    Log.d("test", plan.getPlanName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //TODO add functionality to add course to the database.

    }

    public void setPlanValues(Semester semester, Plan plan) {
        this.semester = semester;
        this.plan = plan;
    }

}
