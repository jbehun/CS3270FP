package edu.weber.behunin.justin.cs3270fp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlanRecylcerAdapter extends RecyclerView.Adapter<PlanRecylcerAdapter.ViewHolder> {

    private final List<Plan> planList;
    private OnPlanClicked mCallback;

    public void addPlans(ArrayList<Plan> plans) {
        planList.clear();
        planList.addAll(plans);
        notifyDataSetChanged();

    }

    interface OnPlanClicked {
        void planClickAction(Plan plan);
    }

    public PlanRecylcerAdapter(List<Plan> planList, RecyclerView recyclerView) {
        this.planList = planList;

        try{
            mCallback = (OnPlanClicked) recyclerView.getContext();
        }catch (ClassCastException e){
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
        final Plan plan = planList.get(position);
        if (plan != null) {
            holder.plan = plan;
            holder.tvLine1.setText(plan.getPlanName());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.planClickAction(plan);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLine1;
        public Plan plan;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvLine1 = (TextView) itemView.findViewById(R.id.line1);
        }
    }
}
