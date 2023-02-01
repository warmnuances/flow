package under.hans.com.flow.Forms;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import under.hans.com.flow.Adapters.CategoryAdapter;
import under.hans.com.flow.AlarmService.AlarmScheduler;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Dialogs.PresetDialog;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.R;
import under.hans.com.flow.models.Categories;

/**
 * Created by Hans on 3/9/2018.
 */

public class AddPresetActivity extends AppCompatActivity
        implements PresetDialog.OnCustomPeriodicSetListener,
        CategoryAdapter.ListItemClickedListener,View.OnClickListener{

    private static final String TAG = "AddPresetActivity";

    private Bundle mBundle;

    //Calendar
    private Calendar c;
    private int mYear, mMonth, mDay;
    private int mHour,mMinute;

    //Reminder
    private RadioGroup radioGroup;

    //Widgets
    private FloatingActionButton btSave;
    private Spinner accSpinner;
    private EditText etName,etAmount;
    private ImageView imgInflowOutflow;
    private RecyclerView rvCategory;
    private TextView tvDate,tvTime,tvNotifier;
    private Switch notificationSwitch;


    //SpinnerList
    private List<String> accountList;
    private List<Categories> categoryDataset = new ArrayList<>();
    CategoryAdapter mAdapter;

    //Variables
    private String flowBool = "outflow";
    private String getCategory;

    private String sDate,eDate,constant;
    private int multi;

    private long currentTime,intervalTime,futureTime;
    private Uri presetUri;

    //Schedule Variables
    private String getFromRepeatEdate,getFromRepeatSdate, getFromRepeatConstant;
    private boolean boolRepeat = false;
    private int getFromRepeatMultiplier;

    private int maxEdate = 2147483647;

    //Database
    private int triggerTime;
    private int multiInt;
    private String strConst;


    //Intent
    private Uri mUri;
    private int Type;
    PresetDialog pdf;
    private int isInflow;

    //Switch
    private int shouldNotify = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_preset_forms_main);

        init();
        setupWidgets();

        mUri = getIntent().getData();


        if(mUri != null){
            Type = getIntent().getIntExtra("Type",0);
            FillDataFromIntent(mUri,Type);
        }
        else{
            tvDate.setText(DateTimeUtils.getCurrentDate());
            tvTime.setText(DateTimeUtils.getCurrentTime());
            notificationSwitch.setChecked(true);

            isInflow = getIntent().getIntExtra("Type",0);

            if(isInflow == 1){
                flowBool = "inflow";
                imgInflowOutflow.setImageDrawable(getResources().getDrawable(R.drawable.plus));
                imgInflowOutflow.setSelected(!imgInflowOutflow.isSelected());
                if(imgInflowOutflow.isSelected()){
                    imgInflowOutflow.setImageResource(R.drawable.animated_minus_to_plus);
                    flowBool = "inflow";
                }else{
                    imgInflowOutflow.setImageResource(R.drawable.animated_plus_to_minus);
                    flowBool = "outflow";

                }
                Drawable drawable = imgInflowOutflow.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }else {
                imgInflowOutflow.setImageDrawable(getResources().getDrawable(R.drawable.minus));
            }

        }

    }

    public void onClickAddItems() {

        String getAmount = etAmount.getText().toString();
        String getName = etName.getText().toString();
        String getDate = tvDate.getText().toString();
        String getTime = tvTime.getText().toString();

        int Amount = FormatAlgorithms.setFormattedFunds(getAmount);
        //int TimeInSec = DateTimeUtils.formatDatetoSec(getDate,getTime);
        int interval;
        int TimeInSec;

        //get Start Date, Interval and End date
        if(radioGroup.getCheckedRadioButtonId() == R.id.btCustom){

            long multiplier = getFromRepeatMultiplier;
            long constant = FormatAlgorithms.parseConstantToInt(getFromRepeatConstant);
            interval = (int)(multiplier * constant);

            //Database
            multiInt = (int)multiplier;
            strConst = getFromRepeatConstant;

            boolRepeat =false;

            intervalTime = DateTimeUtils.calculateInterval(
                    getFromRepeatConstant,getFromRepeatMultiplier,getFromRepeatSdate);

            TimeInSec = DateTimeUtils.convertDateToIntMillis(getFromRepeatSdate);

        }
        else if(radioGroup.getCheckedRadioButtonId() == R.id.btDaily){

            //Database
            multiInt = 1;
            strConst = "Day";
            interval = 86400;
            intervalTime = 86400;
            boolRepeat=true;
            getFromRepeatSdate = DateTimeUtils.getCurrentDate();

            TimeInSec = DateTimeUtils.formatDatetoSec(getDate,getTime);


        }
        else if(radioGroup.getCheckedRadioButtonId() == R.id.btWeekly){

            //Database
            multiInt = 1;
            strConst = "Week";

            interval = 604800;
            intervalTime = 604800;

            boolRepeat = true;
            getFromRepeatSdate = DateTimeUtils.getCurrentDate();
            TimeInSec = DateTimeUtils.formatDatetoSec(getDate,getTime);

        }
        else if(radioGroup.getCheckedRadioButtonId() == R.id.btMonthly){

            //Database
            multiInt = 1;
            strConst = "Month";

            interval = (int)DateTimeUtils.getNextMonthInterval();
            intervalTime = DateTimeUtils.getNextMonthInterval();
            boolRepeat = false;
            getFromRepeatSdate = DateTimeUtils.getCurrentDate();
            TimeInSec = DateTimeUtils.formatDatetoSec(getDate,getTime);

        }
        else {

            Toast.makeText(this, "Reminder is not set!", Toast.LENGTH_SHORT).show();
            return;
        }

        triggerTime = (int)(DateTimeUtils.convertDateToLongMillis(getFromRepeatSdate) + intervalTime);


        if (getFromRepeatSdate.equals(DateTimeUtils.getCurrentDate())) {

            ContentValues cvPreset = new ContentValues();
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT, Amount);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_NAME, getName);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY, getCategory);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_START_DATE, getFromRepeatSdate);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START,
                    TimeInSec);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_END_DATE, getFromRepeatEdate);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_MULTIPLIER,multiInt);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_CONSTANT,strConst);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL, interval);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END,
                    DateTimeUtils.convertDateToIntMillis(getFromRepeatEdate));
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER,triggerTime);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE, flowBool);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY,shouldNotify);

            if(mUri != null){
                getContentResolver().update(mUri,cvPreset,null,null);
            }else {
                presetUri = getContentResolver()
                        .insert(SqlContractClass.PresetClass.CONTENT_URI, cvPreset);
                schedule(presetUri,intervalTime,boolRepeat);
            }




            ContentValues contentValues = new ContentValues();

            if(flowBool == "outflow"){

                contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT, Amount);
                contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME, getName);
                contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY, getCategory);
                contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE, getFromRepeatSdate);
                contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC, DateTimeUtils.convertDateToIntMillis(getFromRepeatSdate));

                if (getAmount.equals("") || getName.equals("") || getCategory.equals("")) {
                    return;
                }

                getContentResolver().insert(SqlContractClass.SpendingsEntryClass.CONTENT_URI, contentValues);

            }

            else if (flowBool == "inflow") {

                contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT, Amount);
                contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME, getName);
                contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY, getCategory);
                contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE, getFromRepeatSdate);
                contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,DateTimeUtils.convertDateToIntMillis(getFromRepeatSdate));

                if (getAmount.equals("") || getName.equals("") || getCategory.equals("")) {
                    return;
                }

                getContentResolver().insert(SqlContractClass.InflowEntryClass.CONTENT_URI,contentValues);
            }

            DatabaseUtils.updateBalance(this, Amount, flowBool);

        }
        else{

            ContentValues cvPreset = new ContentValues();
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT, Amount);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_NAME, getName);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY, getCategory);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_START_DATE, getFromRepeatSdate);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START,
                    DateTimeUtils.convertDateToIntMillis(getFromRepeatSdate));
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_END_DATE, getFromRepeatEdate);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END,
                    DateTimeUtils.convertDateToIntMillis(getFromRepeatEdate));
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE, flowBool);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER,triggerTime);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_MULTIPLIER,multiInt);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_CONSTANT,strConst);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL, interval);
            cvPreset.put(SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY,shouldNotify);

            presetUri = getContentResolver()
                    .insert(SqlContractClass.PresetClass.CONTENT_URI, cvPreset);

        }



    }

    public void schedule(Uri mUri,long interval,boolean boolRepeat){

        if(!boolRepeat){
            currentTime = c.getTimeInMillis();
            futureTime = currentTime + (interval*1000);

            new AlarmScheduler().setAlarm(this,futureTime,mUri);
        }
        else {
            currentTime = c.getTimeInMillis();
            futureTime = currentTime + (interval*1000);
            new AlarmScheduler().setRepeatAlarm(this,futureTime,mUri,interval);
        }

    }

//    private List<String> getAccountList(){
//        List<String> listOfAccounts = new ArrayList<String>();
//        listOfAccounts.add("Debit Card");
//        listOfAccounts.add("Cash");
//
//        return listOfAccounts;
//    }

    /**--------------------------------Level 3 UI --------------------------------------**/
    private void setupWidgets(){

        //Preset
        radioGroup = (RadioGroup)findViewById(R.id.rgReminder);

        //Widgets
        etAmount = (EditText)findViewById(R.id.etAmount);
        etName = (EditText)findViewById(R.id.etName);
        imgInflowOutflow = (ImageView)findViewById(R.id.inflowOutflow);
        btSave = (FloatingActionButton)findViewById(R.id.btSave);
        rvCategory = (RecyclerView)findViewById(R.id.categoryRecyclerview);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvNotifier = (TextView)findViewById(R.id.tvNotifier);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

//        accSpinner = (Spinner)findViewById(R.id.accSpinner);
//        accountList = new ArrayList<>();
//        accountList = getAccountList();
//
//        ArrayAdapter<String> accListAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, accountList);
//
//        accListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        accSpinner.setAdapter(accListAdapter);


        imgInflowOutflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgInflowOutflow.setSelected(!imgInflowOutflow.isSelected());
                if(imgInflowOutflow.isSelected()){
                    imgInflowOutflow.setImageResource(R.drawable.animated_minus_to_plus);
                    flowBool = "inflow";
                }else{
                    imgInflowOutflow.setImageResource(R.drawable.animated_plus_to_minus);
                    flowBool = "outflow";
                }
                Drawable drawable = imgInflowOutflow.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if(checkedId == R.id.btCustom){
                    pdf = new PresetDialog();
                    pdf.show(getSupportFragmentManager(), "PresetDialog");
                }
                else if(checkedId == R.id.btDaily){
                    tvNotifier.setText("Scheduled Daily");
                }
                else if(checkedId == R.id.btWeekly){
                    tvNotifier.setText("Scheduled Weekly");
                }
                else if(checkedId == R.id.btMonthly){
                    tvNotifier.setText("Scheduled Monthly");
                }
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfItemsHasInput();
            }
        });

        categoryDataset = DatabaseUtils.getCategoryDataset(this);
        mAdapter = new CategoryAdapter(this, categoryDataset,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(layoutManager);

        rvCategory.setAdapter(mAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_white));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        notificationSwitch = (Switch)findViewById(R.id.notificationSwitch);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked == true){
                    shouldNotify = 1;
                }else {
                    shouldNotify = 0;
                }
            }
        });
    }

    private void checkIfItemsHasInput(){
        String getAmount = etAmount.getText().toString();
        String getName = etName.getText().toString();

        if(getAmount.equals("")||getName.equals("")||getCategory == null||
                radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(this,"Oops, you forgot to enter something", Toast.LENGTH_SHORT).show();

        }else {
            onClickAddItems();
            if(flowBool.equals("inflow")){
                Intent intent = new Intent(AddPresetActivity.this, PresetOverview.class);
                intent.putExtra("page",1);
                startActivity(intent);
            }else{
                Intent intent = new Intent(AddPresetActivity.this, PresetOverview.class);
                startActivity(intent);
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(mUri !=null){
            getMenuInflater().inflate(R.menu.menu_add_preset, menu);
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                getContentResolver().delete(mUri,null,null);
                Intent intent = new Intent(AddPresetActivity.this,PresetOverview.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void OnCustomPeriodicSet(Bundle bundle) {

        if(bundle!=null){
            getFromRepeatSdate = bundle.getString(getResources().getString(R.string.start_date));
            getFromRepeatEdate = bundle.getString(getResources().getString(R.string.end_date));
            getFromRepeatConstant = bundle.getString(getResources().getString(R.string.constant));
            getFromRepeatMultiplier = bundle.getInt(getResources().getString(R.string.multiplier));

            setNotifierText(getFromRepeatMultiplier,getFromRepeatConstant);
        }


    }

    @Override
    public void OnCancelPressed() {
        radioGroup.check(-1);
        pdf.dismiss();
        radioGroup.clearCheck();
    }

    @Override
    public void onListItemClickedListener(String itemCategory) {
        getCategory = itemCategory;
    }

    @Override
    public void onClick(View v) {

        if(v == tvDate ){

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            tvDate.setText(DateTimeUtils.getFormattedDate(year,month,dayOfMonth));
                            mYear = year;
                            mMonth = month;
                            mDay = dayOfMonth;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

            etAmount.clearFocus();
            etName.clearFocus();

        }
        else if(v == tvTime){

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            tvTime.setText(DateTimeUtils.getFormattedTime(hourOfDay,minute));
                            mHour = hourOfDay;
                            mMinute = minute;
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

            etAmount.clearFocus();
            etName.clearFocus();
        }
    }
    private void init(){
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    private void setNotifierText(int multiplier, String constant){
        String date = tvDate.getText().toString();
        String strScheduled = "Scheduled on " + date
                + " every " + String.valueOf(multiplier)
                + " " + constant;
        tvNotifier.setText(strScheduled);
    }

    // TODO: 11/05/2018 Set type,account Type, category;
    private void FillDataFromIntent(Uri mUri,int typeBool){
        int amount = 0,timeEnd = 0,interval = 0,notify;
        long time = 0;
        String name,category,type;



        String[] projection = {
                SqlContractClass.PresetClass.COLUMN_PRESET_NAME,
                SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT,
                SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START,
                SqlContractClass.PresetClass.COLUMN_PRESET_ACCOUNT,
                SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY,
                SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END,
                SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL,
                SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY,
                SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE};

        Cursor mCursor = getContentResolver().query(mUri,
                projection,
                null,
                null,
                null);

        if(mCursor!=null && mCursor.moveToFirst()){
            int amtIndex  = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT);
            int nameIndex  = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_NAME);
            int timeIndex  = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START);
            int categoryIndex  = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY);
            int timeEndIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END);
            int intervalIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL);
            int notifyIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY);
            int typeIndex  = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE);


            amount = mCursor.getInt(amtIndex);
            name = mCursor.getString(nameIndex);
            time = mCursor.getInt(timeIndex);
            category = mCursor.getString(categoryIndex);
            timeEnd  = mCursor.getInt(timeEndIndex);
            interval = mCursor.getInt(intervalIndex);
            notify = mCursor.getInt(notifyIndex);
            type = mCursor.getString(typeIndex);


            setCategoryOnIntent(category);
            tvTime.setText(DateTimeUtils.formatTimetoString(time*1000));
            tvDate.setText(DateTimeUtils.formatDatetoString(time));
            etAmount.setText(FormatAlgorithms.getFormattedFunds(amount));
            etName.setText(name);

            if(timeEnd == 0){
                int days = ((int)interval/86400);

                if(days == 1){
                    radioGroup.check(R.id.btDaily);
                }else if(days == 7){
                    radioGroup.check(R.id.btWeekly);
                }else {
                    radioGroup.check(R.id.btMonthly);
                }

            }else {
                radioGroup.check(R.id.btCustom);
            }

            if(notify == 1){
                notificationSwitch.setChecked(true);
            }else {
                notificationSwitch.setChecked(false);
            }


            flowBool = type;

            if(type.equals("inflow")){
                imgInflowOutflow.setImageDrawable(getResources().getDrawable(R.drawable.plus));

                    imgInflowOutflow.setSelected(imgInflowOutflow.isSelected());
                    if(imgInflowOutflow.isSelected()){
                        imgInflowOutflow.setImageResource(R.drawable.animated_plus_to_minus);
                        flowBool = "outflow";

                    }else{
                        imgInflowOutflow.setImageResource(R.drawable.animated_minus_to_plus);
                        flowBool = "inflow";
                    }
                    Drawable drawable = imgInflowOutflow.getDrawable();
                    if (drawable instanceof Animatable) {
                        ((Animatable) drawable).start();
                    }
            }else if(type.equals("outflow")){
                imgInflowOutflow.setImageDrawable(getResources().getDrawable(R.drawable.minus));
                imgInflowOutflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgInflowOutflow.setSelected(!imgInflowOutflow.isSelected());
                        if(imgInflowOutflow.isSelected()){
                            imgInflowOutflow.setImageResource(R.drawable.animated_minus_to_plus);
                            flowBool = "inflow";
                        }else{
                            imgInflowOutflow.setImageResource(R.drawable.animated_plus_to_minus);
                            flowBool = "outflow";
                        }
                        Drawable drawable = imgInflowOutflow.getDrawable();
                        if (drawable instanceof Animatable) {
                            ((Animatable) drawable).start();
                        }
                    }
                });
            }


        }
        mCursor.close();

    }
    private void setCategoryOnIntent(final String category){

        final int pos = DatabaseUtils.getCategoryPosition(this, category);

        rvCategory.scrollToPosition(pos);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    onListItemClickedListener(category);
                    rvCategory.findViewHolderForAdapterPosition(pos).itemView.performClick();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },5);
    }

}
