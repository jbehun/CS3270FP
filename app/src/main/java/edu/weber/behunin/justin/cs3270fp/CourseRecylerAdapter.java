package edu.weber.behunin.justin.cs3270fp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecylerAdapter extends RecyclerView.Adapter<CourseRecylerAdapter.ViewHolder> {

    private final Plan plan;
    private final Semester semester;
    private final List<Course> courseList;
    private final OnCourseClicked mCallback;

    public void addValue(Semester semester) {
        courseList.clear();
        courseList.addAll(semester.getCourses());
        notifyDataSetChanged();
    }

    interface OnCourseClicked {
        void courseClickedAction(Course course, Semester semester, Plan plan);
    }

    public CourseRecylerAdapter(Plan plan, Semester semester, List<Course> courseList, RecyclerView recyclerView) {
        this.plan = plan;
        this.courseList = courseList;
        this.semester = semester;

        try {
            mCallback = (OnCourseClicked) recyclerView.getContext();
        } catch (ClassCastException e) {
            throw new ClassCastException(recyclerView.toString() +
                    " must implement OnCourseClicked interface");
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

        final Course course = courseList.get(position);

        if (course != null) {
            holder.course = course;
            holder.tvLine1.setText(String.format("%s: %s", course.getCourseID(), course.getCourseName()));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.courseClickedAction(course, semester, plan);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvLine1;
        public Course course;
        public View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvLine1 = (TextView) itemView.findViewById(R.id.line1);
        }
    }
}
