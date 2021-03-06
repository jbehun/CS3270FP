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
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    private OnPlanAction mCallback;
    private TextView welcomeTxt;
    private RecyclerView recyclerView;
    private PlanRecyclerAdapter adapter;

    interface OnPlanAction {
        void signOut();

        void createPlan();
    }

    public PlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_plan, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_plans);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        welcomeTxt = (TextView) root.findViewById(R.id.txtPlanWelcome);

        recyclerView = (RecyclerView) root.findViewById(R.id.rvPlanList);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.createPlan();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnPlanAction) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnPlanAction interface");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.signout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_signOut:
                mCallback.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String[] getUser = Objects.requireNonNull(currentUser.getEmail()).split("@");
            welcomeTxt.setText(String.format("%s %s",
                    getString(R.string.welcome_title),
                    getUser[0].substring(0, 1).toUpperCase()
                            + getUser[0].substring(1)));
        } else {
            welcomeTxt.setText(R.string.user_name_not_found);
        }

        Context context = getContext();
        adapter = new PlanRecyclerAdapter(new ArrayList<Plan>(), recyclerView);

        int columnCount = 1;
        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            mDatabase.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {

                private ArrayList<Plan> plans = new ArrayList<>();

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    plans.clear();

                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Plan plan = child.getValue(Plan.class);
                        plans.add(plan);
                    }

                    adapter.addPlans(plans);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
