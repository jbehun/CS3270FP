package edu.weber.behunin.justin.cs3270fp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseRecylerAdapter adapter;
    private Plan plan;
    private Semester semester;
    private OnCourseAction mCallback;
    private Spinner spinner;
    private Button button;
    private ArrayList<Course> courses = new ArrayList<>();

    interface OnCourseAction {
        void confirmSemesterDelete(Semester semester, Plan plan);

        void doneWithSemester(Plan plan);

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_course, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        if (semester != null) {
            toolbar.setTitle(semester.getSemesterName());
        } else {
            toolbar.setTitle(R.string.edit_semester);
        }

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) root.findViewById(R.id.rvCourseLIst);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        button = (Button) root.findViewById(R.id.button);

        return root;
    }

    private void addCourse(Course course) {
        courses.add(course);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.fragment_menu, menu);
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        Context context = getContext();

        ArrayAdapter<Course> arrayAdapter = null;
        if (context != null) {
            arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, courses);
        }
        if (arrayAdapter != null) {
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        spinner.setAdapter(arrayAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new CourseRecylerAdapter(plan, semester, new ArrayList<Course>(), recyclerView);


        int columnCount = 1;

        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        if (currentUser != null) {
            mDatabase.child(currentUser.getUid()).child(plan.getPlanName())
                    .addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Plan plan = dataSnapshot.getValue(Plan.class);
                            if (plan != null) {
                                Semester s = plan.getSemester(semester);
                                adapter.addValue(s);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = (Course) spinner.getSelectedItem();
                if (!semester.getCourses().contains(course)) {
                    Semester temp = semester;
                    temp.addCourse(course);
                    plan.deleteSemester(semester);
                    plan.addSemester(temp);
                    if (currentUser != null) {
                        mDatabase.child(currentUser.getUid()).child(plan.getPlanName()).setValue(plan);
                    }
                } else {
                    Toast toast = Toast.makeText(getContext(), R.string.course_already_added, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });
    }

    public void setPlanValues(Semester semester, Plan plan, ArrayList<Course> courseList) {
        this.semester = semester;
        this.plan = plan;
        courses = courseList;
    }

}
