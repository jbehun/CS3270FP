package edu.weber.behunin.justin.cs3270fp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CourseDialogFragment extends DialogFragment {

    Plan plan;
    Semester semester;
    Course course;
    TextView txtCourseID, txtCourseName, txtCoursePreReq;
    View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.course_dialog_fragment, container, false);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.course);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }


        setHasOptionsMenu(true);

        txtCourseID = root.findViewById(R.id.txtCourseID);
        txtCourseName = root.findViewById(R.id.txtCourseName);
        txtCoursePreReq = root.findViewById(R.id.txtCoursePreReq);

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.course_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        switch (item.getItemId()) {
            case R.id.actionCourseDelete:

                semester.deleteCourse(course);
                Semester temp = semester;
                plan.deleteSemester(semester);
                plan.addSemester(temp);
                if (currentUser != null) {
                    mDatabase.child(currentUser.getUid()).child(plan.getPlanName()).setValue(plan);
                }
                dismiss();

                return true;

            case android.R.id.home:

                dismiss();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        txtCourseID.setText(course.getCourseID());
        txtCourseName.setText(course.getCourseName());
    }

    public void setValues(Plan plan, Semester semester, Course course) {
        this.plan = plan;
        this.semester = semester;
        this.course = course;
    }
}
