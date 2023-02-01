package under.hans.com.flow.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import under.hans.com.flow.BudgetPlan.BudgetingActivity;
import under.hans.com.flow.R;

/**
 * Created by Hans on 4/17/2018.
 */

public class MainBudgetDialog extends DialogFragment {
    private static final String TAG = "MainBudgetDialog";

    MainBudgetDialogListener mListener;
    String budget;

    EditText etSetBudget;

    public interface MainBudgetDialogListener{
        void onDialogPositiveClick(String text);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (MainBudgetDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement MainBudgetDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptView = inflater.inflate(R.layout.dialog_set_budget,null);


        etSetBudget = (EditText) promptView.findViewById(R.id.etSetBudget);

        builder.setView(promptView)
            .setPositiveButton(R.string.set_budget, new DialogInterface.OnClickListener() {
              @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  budget = etSetBudget.getText().toString();
                  mListener.onDialogPositiveClick(budget);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainBudgetDialog.this.getDialog().cancel();
                }
            });

        return builder.create();
    }
}
