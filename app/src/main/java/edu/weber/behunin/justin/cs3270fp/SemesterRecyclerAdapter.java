package edu.weber.behunin.justin.cs3270fp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SemesterRecyclerAdapter extends RecyclerView.Adapter<SemesterRecyclerAdapter.ViewHolder> {

    private final List<Semester> semesterList;
    private final OnSemesterClicked mCallback;
    private final Plan plan;

    public void addSemesters(Plan plan) {
        if (plan.getSemesterList() != null) {
            semesterList.clear();
            semesterList.addAll(plan.getSemesterList());
            notifyDataSetChanged();
        }
    }

    interface OnSemesterClicked {
        void semesterClickAction(Semester semester, Plan plan);
    }

    public SemesterRecyclerAdapter(Plan plan, List<Semester> semesterList, RecyclerView recyclerView) {
        this.semesterList = semesterList;
        this.plan = plan;

        try {
            mCallback = (OnSemesterClicked) recyclerView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(recyclerView.toString() +
                    " must implement OnPlanClicked interface");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Semester semester = semesterList.get(position);
        if (semester != null) {
            holder.semester = semester;
            holder.tvLine1.setText(semester.getSemesterName());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.semesterClickAction(semester, plan);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLine1;
        public Semester semester;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvLine1 = (TextView) itemView.findViewById(R.id.line1);
        }
    }


}
