package under.hans.com.flow.Forms;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import under.hans.com.flow.Adapters.CategoryAdapter;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.models.Categories;

/**
 * Created by Hans on 4/15/2018.
 */

public class AddItemsForms extends AppCompatActivity
        implements View.OnClickListener,CategoryAdapter.ListItemClickedListener {

    private static final String TAG = "AddItemsForms";

    //Calendar
    private Calendar c;
    private int mYear, mMonth, mDay;
    private int mHour,mMinute;

    //Widgets
    private Button btSave,btLabelsText;
    private Spinner accSpinner;
    private EditText etName,etAmount;
    private ImageView imgDropDown;
    private TextView tvDate,tvTime;
    private RecyclerView rvCategory;

    //Spendings or Inflow
    private ImageView imgInflowOutflow;
    private String flowBool = "outflow";

    //SpinnerList
    private List<String> accountList;

    //Variables
    private String getCategory;
    private List<Categories> categoryDataset = new ArrayList<>();
    CategoryAdapter mAdapter;

    private Uri mUri;
    private int Type;

    private Boolean hasRecords = false;
    private int oldAmount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spendings_form);

        mUri = getIntent().getData();


        init();
        setupWidgets();

        tvDate.setText(DateTimeUtils.getCurrentDate());
        tvTime.setText(DateTimeUtils.getCurrentTime());

        imgInflowOutflow.setImageDrawable(getResources().getDrawable(R.drawable.minus));

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfItemsHasInput();

            }
        });

        if(mUri != null){
            Type = getIntent().getIntExtra("Type",0);
            FillDataFromIntent(mUri,Type);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                deleteItem();
                Intent intent = new Intent(AddItemsForms.this,MainActivity.class);
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

    private void deleteItem(){
        Log.d(TAG, "deleteItem: flowBool = " + flowBool);

        int amount = FormatAlgorithms.setFormattedFunds(etAmount.getText().toString());

            DatabaseUtils.updateBalanceOnDelete(this,amount,flowBool);

    }

    // TODO: 11/05/2018 Set type,account Type, category; 
    private void FillDataFromIntent(Uri mUri, int type){
        int amount = 0,time= 0;
        String name = "",account= "",category= "";

        hasRecords = true;
        if(type == 1){
            String[] projection = {
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC};

           Cursor mCursor = getContentResolver().query(mUri,
                    projection,
                    null,
                    null,
                    null);


            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
                int amtIndex  = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int nameIndex  = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME);
                int timeIndex  = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC);
                int accIndex  = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT);
                int categoryIndex  = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY);

                amount = mCursor.getInt(amtIndex);
                name = mCursor.getString(nameIndex);
                time = mCursor.getInt(timeIndex);
                account = mCursor.getString(accIndex);
                category = mCursor.getString(categoryIndex);

                flowBool = "outflow";

            }


        }
        else if(type == 2) {
            String[] projection = {
                    SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME,
                    SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT,
                    SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,
                    SqlContractClass.InflowEntryClass.COLUMN_INFLOW_ACCOUNT,
                    SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY};

            Cursor mCursor = getContentResolver().query(mUri,
                    projection,
                    null,
                    null,
                    null);


            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                int amtIndex = mCursor.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT);
                int nameIndex = mCursor.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME);
                int timeIndex = mCursor.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC);
                int accIndex = mCursor.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_ACCOUNT);
                int categoryIndex = mCursor.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY);

                amount = mCursor.getInt(amtIndex);
                name = mCursor.getString(nameIndex);
                time = mCursor.getInt(timeIndex);
                account = mCursor.getString(accIndex);
                category = mCursor.getString(categoryIndex);

                flowBool = "inflow";
            }
        }
        else {
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

        setCategoryOnIntent(category);
        oldAmount = amount;
        etAmount.setText(FormatAlgorithms.getFormattedFunds(amount));
        etName.setText(name);
        tvDate.setText(DateTimeUtils.convertMillisToDate(time));
    }


    public void onClickAddItems(){

        String getAmount = etAmount.getText().toString();
        String getName = etName.getText().toString();
        String getDate = tvDate.getText().toString();
        String getTime = tvTime.getText().toString();
        String getNotes = "";

        Uri getUri;


        int TimeInSec = DateTimeUtils.formatDatetoSec(getDate,getTime);
        int newAmount = FormatAlgorithms.setFormattedFunds(getAmount);

        ContentValues contentValues = new ContentValues();


        if(flowBool == "outflow"){



            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT, newAmount);
            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME, getName);
            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY, getCategory);
            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE, getDate);
            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,TimeInSec);
            contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NOTES, getNotes);

            if (getAmount.equals("") || getName.equals("") || getCategory.equals("")) {
                return;
            }

            if(hasRecords){
                getUri = mUri;
                getContentResolver().update(getUri,contentValues,null,null);
            }else{
                getUri = SqlContractClass.SpendingsEntryClass.CONTENT_URI;
                getContentResolver().insert(getUri,contentValues);
            }


        } else if(flowBool == "inflow"){

            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT, newAmount);
            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME, getName);
            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY, getCategory);
            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE, getDate);
            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,TimeInSec);
            contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NOTES, getNotes);

            if (getAmount.equals("") || getName.equals("") || getCategory.equals("")) {
                return;
            }

            if(hasRecords){
                getUri = mUri;
                getContentResolver().update(getUri,contentValues,null,null);
            }else{
                getUri = SqlContractClass.InflowEntryClass.CONTENT_URI;
                getContentResolver().insert(getUri,contentValues);
            }

        }

        DatabaseUtils.updateBalance(this,oldAmount,flowBool,newAmount);

    }

    private void checkIfItemsHasInput(){

        String getAmount = etAmount.getText().toString();
        String getName = etName.getText().toString();

        if(getAmount.equals("")||getName.equals("")||getCategory == null){
            Toast.makeText(this,"Oops, you forgot to enter something", Toast.LENGTH_SHORT).show();

        }else {
            onClickAddItems();
            Intent intent = new Intent(AddItemsForms.this, MainActivity.class);
            startActivity(intent);
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
    /**--------------------------------Level 2 Setup Widgets --------------------------------------**/

    private void setupWidgets(){

        //Widgets
        etAmount = (EditText)findViewById(R.id.etAmount);

        etName = (EditText)findViewById(R.id.etName);
        rvCategory = (RecyclerView)findViewById(R.id.categoryRecyclerview);

        tvDate = (TextView)findViewById(R.id.tvDate);
        tvTime = (TextView)findViewById(R.id.tvTime);

        btSave = (Button)findViewById(R.id.btSave);


        imgInflowOutflow = (ImageView)findViewById(R.id.inflowOutflow);

        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);

//        accountList = new ArrayList<>();
//        accountList = getAccountList();
//
//
//        accSpinner = (Spinner)findViewById(R.id.accSpinner);
//        ArrayAdapter<String> accListAdapter = new ArrayAdapter<String>(this,
//                R.layout.spinner_item, accountList);
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

        categoryDataset = DatabaseUtils.getCategoryDataset(this);
        mAdapter = new CategoryAdapter(this, categoryDataset,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(layoutManager);

        rvCategory.setAdapter(mAdapter);

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

    @Override
    public void onListItemClickedListener(String itemCategory) {
        getCategory = itemCategory;
    }

    private void setCategoryOnIntent(final String category){

        final int pos = DatabaseUtils.getCategoryPosition(this, category);

        rvCategory.scrollToPosition(pos);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    onListItemClickedListener(category);
                    rvCategory.findViewHolderForAdapterPosition(pos).itemView.performClick();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },5);
    }
}
