package under.hans.com.flow.Start;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import under.hans.com.flow.Dialogs.MainBudgetDialog;
import under.hans.com.flow.R;

public class StartBudgetFragment extends Fragment{

    EditText etBudgetClick;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_second_budget,container,false);

        etBudgetClick =(EditText)view.findViewById(R.id.etBudgetClick);
        etBudgetClick.setFocusable(false);
        etBudgetClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FragmentManager fm = getFragmentManager();
                    MainBudgetDialog mainBudgetDialog = new MainBudgetDialog();
                    mainBudgetDialog.show(fm, "MainBudgetDialog");
            }
        });

        return view;

    }

}
