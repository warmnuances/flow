package under.hans.com.flow.Dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Forms.AddPresetActivity;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DateTimeUtils;

/**
 * Created by Hans on 4/15/2018.
 */

public class PresetDialog extends DialogFragment implements View.OnClickListener {


    /** ------------------------ Fragment Interface ---------------------------------------**/

    OnCustomPeriodicSetListener mCallback;

    public interface OnCustomPeriodicSetListener{
        void OnCustomPeriodicSet(Bundle bundle);
        void OnCancelPressed();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (PresetDialog.OnCustomPeriodicSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /** ----------------------------- Fragment -----------------------------------------**/
    private static final String TAG = "PresetDialogFragment";

    private TextView tvStartDate,tvEndDate;
    private EditText etMultiplier;
    private Spinner constantSpinner;
    private Button btSetPeriodic,btCancel;

    //Calendar
    private Calendar c;
    private int mYear, mMonth, mDay;
    private int mHour,mMinute;

    //Variables
    private List<String> constantList;
    //Variables
    private String strStartDate,strEndDate,strConstant;
    private int multiplier;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptView = inflater.inflate(R.layout.dialog_preset,null);

        init();
        setupWidgets(promptView);

        builder.setView(promptView);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0){
                    onCancelPressed();
                    return true;
                }
                return false;
            }
        });

        return builder.create();
    }

    private void setupWidgets(View view){

        constantList = new ArrayList<>();
        constantList = getConstants();

        tvStartDate = (TextView)view.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView)view.findViewById(R.id.tvEndDate);
        etMultiplier = (EditText) view.findViewById(R.id.etMultiplier);
        constantSpinner = (Spinner)view.findViewById(R.id.constSpinner);
        btSetPeriodic = (Button)view.findViewById(R.id.btSetPeriodic);
        btCancel = (Button)view.findViewById(R.id.btCancel);


        tvEndDate.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);

        ArrayAdapter<String> constListAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, constantList);
        constListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        constantSpinner.setAdapter(constListAdapter);

        etMultiplier.setText("1");

        btSetPeriodic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPeriodicIntervalSet(view);
                PresetDialog.this.getDialog().dismiss();

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });

        int millis = ((int)System.currentTimeMillis()/1000)+86400;
        tvStartDate.setText(DateTimeUtils.getCurrentDate());
        tvEndDate.setText(DateTimeUtils.convertMillisToDate(millis));

        constantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                int constantInterval = 0,startMillis,endMillis;
                int multiplier = Integer.parseInt(etMultiplier.getText().toString());

                strStartDate = tvStartDate.getText().toString();

                startMillis = DateTimeUtils.convertDateToIntMillis(strStartDate);

                if(pos == 0){
                    constantInterval = 86400* multiplier;
                }else if(pos == 1){
                    constantInterval = 604800* multiplier;
                }else if(pos == 2){
                    String startDate = strStartDate;
                    constantInterval = ((DateTimeUtils.getMonthInterval(startDate,multiplier))*86400);
                }

                endMillis = startMillis + constantInterval;
                tvEndDate.setText(DateTimeUtils.convertMillisToDate(endMillis));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etMultiplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String input = etMultiplier.getText().toString();

                if(!input.equals("")){
                    int multiplier = Integer.parseInt(input);
                    int pos = constantSpinner.getSelectedItemPosition();
                    int constantInterval = 0,startMillis,endMillis;

                    strStartDate = tvStartDate.getText().toString();

                    startMillis = DateTimeUtils.convertDateToIntMillis(strStartDate);

                    if(pos == 0){
                        constantInterval = 86400 * multiplier;
                    }else if(pos == 1){
                        constantInterval = 604800 * multiplier;
                    }else if(pos == 2){
                        String startDate = strStartDate;
                        constantInterval = ((DateTimeUtils.getMonthInterval(startDate,multiplier))*86400);
                    }

                    endMillis = startMillis + constantInterval;
                    tvEndDate.setText(DateTimeUtils.convertMillisToDate(endMillis));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    private void onPeriodicIntervalSet(View v){

        strStartDate = tvStartDate.getText().toString();
        strEndDate = tvEndDate.getText().toString();
        strConstant = constantSpinner.getSelectedItem().toString();
        multiplier = Integer.parseInt(etMultiplier.getText().toString());

        Bundle bundle  = new Bundle();
        bundle.putString(getResources().getString(R.string.start_date), strStartDate);
        bundle.putString(getResources().getString(R.string.end_date), strEndDate);
        bundle.putString(getResources().getString(R.string.constant), strConstant);
        bundle.putInt(getResources().getString(R.string.multiplier), multiplier);

        mCallback.OnCustomPeriodicSet(bundle);

    }

    private void onCancelPressed(){

        PresetDialog.this.getDialog().cancel();
        PresetDialog.this.getDialog().dismiss();
        mCallback.OnCancelPressed();
    }

    @Override
    public void onClick(View view) {
        if(view == tvStartDate ){
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String startDate = DateTimeUtils.getFormattedDate(year,month,dayOfMonth);

                            tvStartDate.setText(startDate);
                            mYear = year;
                            mMonth = month;
                            mDay = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if(view == tvEndDate){
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            tvEndDate.setText(DateTimeUtils.getFormattedDate(year,month,dayOfMonth));
                            mYear = year;
                            mMonth = month;
                            mDay = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    private List<String> getConstants(){
        List<String> listOfConstants = new ArrayList<String>();
        listOfConstants.add(getResources().getString(R.string.constant_day));
        listOfConstants.add(getResources().getString(R.string.constant_week));
        listOfConstants.add(getResources().getString(R.string.constant_month));

        return listOfConstants;
    }

    /**--------------------------------Level 1 initialisation --------------------------------------**/
    private void init(){
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }
}
