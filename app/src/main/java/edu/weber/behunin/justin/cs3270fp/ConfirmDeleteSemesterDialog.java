package edu.weber.behunin.justin.cs3270fp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ConfirmDeleteSemesterDialog extends DialogFragment {

    private Plan plan;
    private Semester semester;
    OnConfirmedSemesterAction mCallback;

    interface OnConfirmedSemesterAction {
        void semesterDeletionConfirmed(Plan plan, Semester semester);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.delete_semester)
                .setMessage(R.string.delete_semester_question)
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.semesterDeletionConfirmed(plan, semester);
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
            mCallback = (OnConfirmedSemesterAction) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnConfirmedSemesterAction");
        }
    }

    public void setPlanValues(Plan plan, Semester semester) {

        this.plan = plan;
        this.semester = semester;

    }
}
