package under.hans.com.flow.BudgetPlan;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass.CategoryClass;
import under.hans.com.flow.Data.SqlContractClass.SpendingsEntryClass;
import under.hans.com.flow.Dialogs.MainBudgetDialog;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.Loaders.WeeksBarChartLoader;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.ChartValueFormatter;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;

/**
 * Created by Hans on 4/17/2018.
 */

public class CategoryBudget extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<BarEntry>>, MainBudgetDialog.MainBudgetDialogListener{

    private static final String TAG = "CategoryBudget";
    //Loader IDs
    private final static int SPENDINGS_ID = 201;
    private final static int CATEGORY_ID = 101;
    private final static int LINECHART_ID = 301;

    TextView tvSubName,tvBudCurrency,tvMonthCurrency,tvMonthSpendings,tvPercentage,tvMonthBudget;
    BarChart weekSpendingsChart;
    Uri categoryUri;
    Context mContext = CategoryBudget.this;
    String strCategory;
    CardView cvBudget;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_budget_layout);

        setupWidgets();
        Intent getIntent = getIntent();

        if(getIntent != null){

            categoryUri = getIntent.getData();
            strCategory = getIntent.getStringExtra("Category");


            getSupportLoaderManager().initLoader(CATEGORY_ID,null,categoryLoader);
            getSupportLoaderManager().initLoader(SPENDINGS_ID,null,spendingsLoader);
            getSupportLoaderManager().initLoader(LINECHART_ID,null,this);


            tvPercentage.setText(DatabaseUtils.getPercentage(mContext,strCategory));

            tvMonthBudget.setText(DatabaseUtils.getCategoryBudget(mContext,categoryUri));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void setupWidgets(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_arrow_white));


        tvSubName = (TextView)findViewById(R.id.tvCategorySubName);
        tvBudCurrency = (TextView)findViewById(R.id.tvBudgetCurrency);
        tvMonthBudget = (TextView)findViewById(R.id.tvMonthlyBudget);
        tvMonthSpendings = (TextView)findViewById(R.id.tvSpent);
        tvMonthCurrency = (TextView)findViewById(R.id.tvCurrency);
        tvPercentage = (TextView)findViewById(R.id.tvPercentage);
        cvBudget = (CardView) findViewById(R.id.cvBudget);

        weekSpendingsChart = (BarChart) findViewById(R.id.weeklyBarChart);
        chartInit();


        cvBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                MainBudgetDialog mainBudgetDialog = new MainBudgetDialog();
                mainBudgetDialog.show(fm,"MainBudgetDialog");
            }
        });

    }
    private void chartInit(){

        final ArrayList<String> xAxisLabel = new ArrayList<String>();
        xAxisLabel.add("Week 1");
        xAxisLabel.add("Week 2");
        xAxisLabel.add("Week 3");
        xAxisLabel.add("Week 4");
        xAxisLabel.add("Week 5");


        weekSpendingsChart.getAxisLeft().setDrawLabels(false);
        weekSpendingsChart.getAxisRight().setDrawLabels(false);
        weekSpendingsChart.getAxisLeft().setDrawGridLines(false);
        weekSpendingsChart.getAxisRight().setDrawGridLines(false);
        weekSpendingsChart.getAxisLeft().setDrawAxisLine(false);
        weekSpendingsChart.getAxisRight().setDrawAxisLine(false);
        weekSpendingsChart.setDrawValueAboveBar(true);
        weekSpendingsChart.getLegend().setEnabled(false);
        weekSpendingsChart.getDescription().setEnabled(false);
        weekSpendingsChart.setPinchZoom(false);
        weekSpendingsChart.setDrawGridBackground(false);


        final XAxis xAxis = weekSpendingsChart.getXAxis();
        weekSpendingsChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xAxisLabel.get((int)value);
            }
        });


    }

    /**----------------------------------------------LOADERS-------------------------------------------------**/
    private LoaderManager.LoaderCallbacks<Cursor> categoryLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            String[] projection = {
                    CategoryClass.COLUMN_CATEGORY_NAME,
                    CategoryClass.COLUMN_CATEGORY_SUBNAME,
                    CategoryClass.COLUMN_CATEGORY_BUDGET};


            return new CursorLoader(mContext,
                    categoryUri,
                    projection,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if(cursor.moveToFirst()){
                int nameIndex  = cursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_NAME);
                int subNameIndex = cursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_SUBNAME);
                int budgetIndex = cursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_BUDGET);

                String name = cursor.getString(nameIndex);
                String subName = cursor.getString(subNameIndex);
                int budget = cursor.getInt(budgetIndex);

                tvSubName.setText(name);
                tvMonthBudget.setText(String.valueOf(budget));
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            tvSubName.setText("");
            tvMonthBudget.setText("");
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> spendingsLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            String thisTimeSec = String.valueOf(DateTimeUtils.getLastMonthMillis());
            String nextTimeSec = String.valueOf(DateTimeUtils.getThisMonthMillis());


            String[] projection = {
                    SpendingsEntryClass.COLUMN_SPENDINGS_NAME,
                    SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,
                    SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};

            String selection = SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY  + "=? AND " +
                    SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
            String[] selectionArgs = {strCategory,thisTimeSec,nextTimeSec};

            //category,
            return new CursorLoader(mContext,
                    SpendingsEntryClass.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            int total = 0;

            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                int amountIndex = cursor.getColumnIndex(SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int amount = cursor.getInt(amountIndex);

                total = total + amount;

            }
            tvMonthSpendings.setText(FormatAlgorithms.getFormattedFunds(total));
        }

        @Override
        public void onLoaderReset(Loader loader) {
            tvMonthSpendings.setText("");
        }
    };

    @Override
    public Loader<List<BarEntry>> onCreateLoader(int i, Bundle bundle) {
        WeeksBarChartLoader barChartLoader = new WeeksBarChartLoader(mContext, strCategory);
        barChartLoader.forceLoad();

        return barChartLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<BarEntry>> loader, List<BarEntry> barEntries) {
        BarDataSet barDataSet = new BarDataSet(barEntries, "Data Set");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(true);

        BarData data = new BarData(barDataSet);
        data.setValueFormatter(new ChartValueFormatter());
        data.setValueTextSize(13f);

        weekSpendingsChart.notifyDataSetChanged();
        weekSpendingsChart.setData(data);
        weekSpendingsChart.invalidate();
        weekSpendingsChart.animateY(500);

    }

    @Override
    public void onLoaderReset(Loader<List<BarEntry>> loader) {
        weekSpendingsChart.invalidate();
    }

    @Override
    public void onDialogPositiveClick(String text) {
        int budgetSet;

        tvMonthBudget.setText(text);
        budgetSet = Integer.parseInt(text);

        DatabaseUtils.updateCategoryBudget(mContext,budgetSet,categoryUri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), BudgetingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
