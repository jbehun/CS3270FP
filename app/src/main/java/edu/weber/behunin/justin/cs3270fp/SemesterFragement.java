package edu.weber.behunin.justin.cs3270fp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SemesterFragement extends Fragment {

    View root;

    public SemesterFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_semseter, container, false);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.your_plans);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        //TODO create a recycler adapter for this fragment
        //recyclerView = (RecyclerView) root.findViewById(R.id.rvSemesterLIst);

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabSemester);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return root;
    }

}
