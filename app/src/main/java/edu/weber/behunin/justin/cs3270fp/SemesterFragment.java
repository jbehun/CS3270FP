package edu.weber.behunin.justin.cs3270fp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
public class SemesterFragment extends Fragment {

    private View root;
    private TextView txtSemester;
    private RecyclerView recyclerView;
    private Plan plan;
    private OnSemesterAction mCallbackk;
    private FirebaseAuth mAuth;
    private SemesterRecyclerAdapter adapter;

    interface OnSemesterAction{
        void createSemester(Plan plan);
        void confirmDeletePlan(Plan plan);
        void doneWithPlan();
    }


    public SemesterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_semseter, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        if(plan != null) {
            toolbar.setTitle(plan.getPlanName());
        }else {
            toolbar.setTitle(R.string.edit_plan);
        }

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        txtSemester = root.findViewById(R.id.txtSemester);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvSemesterLIst);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabSemester);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbackk.createSemester(plan);
            }
        });
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            mCallbackk = (OnSemesterAction)getActivity() ;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() +
            " must implement OnSemeterAction interface");
        }
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
                mCallbackk.confirmDeletePlan(plan);
                return true;
            case R.id.actionDone:
                mCallbackk.doneWithPlan();
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

        txtSemester.setText(R.string.semester_list);

        Context context = getContext();
        adapter = new SemesterRecyclerAdapter(plan, new ArrayList<Semester>(), recyclerView);

        int columnCount = 1;
        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child(currentUser.getUid()).child(plan.getPlanName())
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Plan plan = dataSnapshot.getValue(Plan.class);
                        if (plan != null) {
                            adapter.addSemesters(plan);
                            Log.d("test", plan.getPlanName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    public void setPlan(Plan plan){
        this.plan = plan;
    }

}
