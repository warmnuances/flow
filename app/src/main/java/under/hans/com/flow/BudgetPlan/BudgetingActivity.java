package under.hans.com.flow.BudgetPlan;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.About.AboutActivity;
import under.hans.com.flow.Adapters.CategoryAdapter;
import under.hans.com.flow.Adapters.CategoryBudgetAdapter;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Dialogs.MainBudgetDialog;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.Loaders.BudgetPieLoader;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;
import under.hans.com.flow.Trends.TrendActivity;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.models.Categories;

/**
 * Created by Hans on 4/1/2018.
 */

public class BudgetingActivity extends AppCompatActivity
        implements CategoryBudgetAdapter.ListItemClickedListener,
        MainBudgetDialog.MainBudgetDialogListener,
        LoaderManager.LoaderCallbacks<List<PieEntry>>,
        NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "BudgetingActivity";
    private static final int PIECHART_ID = 101;
    private Context mContext = BudgetingActivity.this;

    TextView tvMonthlyBudget;
    PieChart pieChartBudget;
    RecyclerView categoryRecyclerView;
    CategoryBudgetAdapter mAdapter;

    String getCategory;

    private List<Categories> categoryDataset = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting_plan);

        setupNavigationDrawer();

        categoryDataset = DatabaseUtils.getCategoryDataset(getApplicationContext());

        tvMonthlyBudget = (TextView)findViewById(R.id.tvMonthlyBudget);
        pieChartBudget = (PieChart) findViewById(R.id.pieChartBudget);
        categoryRecyclerView = (RecyclerView)findViewById(R.id.categoryRecyclerview);

        PieChartInit();

        tvMonthlyBudget.setText(DatabaseUtils.getUserBudget(mContext));

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        mAdapter = new CategoryBudgetAdapter(this, categoryDataset,this);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(layoutManager);

        categoryRecyclerView.setAdapter(mAdapter);


        tvMonthlyBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptBudgetDialog();
            }
        });

        getSupportLoaderManager().initLoader(PIECHART_ID,null,this);


    }

    private void promptBudgetDialog(){
        FragmentManager fm = getSupportFragmentManager();
        MainBudgetDialog mainBudgetDialog = new MainBudgetDialog();
        mainBudgetDialog.show(fm, "MainBudgetDialog");
    }
    @Override
    public void onListItemClickedListener(String itemCategory) {

        String[] projection = {SqlContractClass.CategoryClass._ID};
        String selection = SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME + " =?";
        String[] selectionArgs = {itemCategory};

        Cursor cursor = getContentResolver().query(
                SqlContractClass.CategoryClass.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        null);

        if(cursor.moveToFirst()){
            Uri categoryUri = ContentUris.withAppendedId(SqlContractClass.CategoryClass.CONTENT_URI,
                    cursor.getInt(cursor.getColumnIndex(SqlContractClass.CategoryClass._ID)));

            getCategory = itemCategory;
            Intent intent = new Intent(BudgetingActivity.this, CategoryBudget.class);
            intent.setData(categoryUri);
            intent.putExtra("Category",getCategory);
                    startActivity(intent);
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }
    public void PieChartInit(){
        pieChartBudget.setRotationEnabled(false);
        pieChartBudget.getLegend().setEnabled(false);
        pieChartBudget.getDescription().setEnabled(false);
        pieChartBudget.setTouchEnabled(false);
        pieChartBudget.setDrawHoleEnabled(false);
        //pieChartBudget.setNoDataText("Please enter budget in categories");
    }

    @Override
    public void onDialogPositiveClick(String text) {
        int budgetSet;

        tvMonthlyBudget.setText(text);
        budgetSet = Integer.parseInt(text);



        DatabaseUtils.updateUserBudget(this,budgetSet);

    }


    @Override
    public Loader<List<PieEntry>> onCreateLoader(int i, Bundle bundle) {

        BudgetPieLoader budgetPieLoader = new BudgetPieLoader(mContext);
        budgetPieLoader.forceLoad();

        return budgetPieLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<PieEntry>> loader, List<PieEntry> pieEntries) {

        PieDataSet dataSet = new PieDataSet(pieEntries, "Categories");
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(getResources().getColor(R.color.colorGreyText));

        if(pieEntries.size() == 0){
            pieChartBudget.setNoDataText("Please enter budget in categories");
        }else {
            pieChartBudget.setData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PieEntry>> loader) {
        pieChartBudget.clear();
    }

    /** ---------------------- Level 2 Setup UI ---------------------------------------**/


    private void setupNavigationDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /** ---------------------- Level 1 Setup up for Navigation and toolbar-----------------------**/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budgeting_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_set_budget) {
            promptBudgetDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(BudgetingActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_preset) {

            Intent intent = new Intent(BudgetingActivity.this, PresetOverview.class);
            startActivity(intent);

        } else if (id == R.id.nav_budget) {


        } else if (id == R.id.nav_trends) {

            Intent intent = new Intent(BudgetingActivity.this, TrendActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(BudgetingActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
