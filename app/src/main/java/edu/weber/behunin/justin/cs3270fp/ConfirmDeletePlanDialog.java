package edu.weber.behunin.justin.cs3270fp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ConfirmDeletePlanDialog extends DialogFragment {

    private PlanDeleteConfirmed mCallBack;
    private Plan plan;

    interface PlanDeleteConfirmed {
        void planDeletionConfirmed(Plan plan);
    }

    public ConfirmDeletePlanDialog() {
        //required empty constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_plan)
                .setMessage(R.string.delete_plan_message)
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallBack.planDeletionConfirmed(plan);
                        dismiss();
                    }
                }).setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing user cancelled.
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (PlanDeleteConfirmed) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement PlanDeleteConfirmedInterface");
        }
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

}
