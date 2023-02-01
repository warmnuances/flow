package under.hans.com.flow.Trends;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import under.hans.com.flow.About.AboutActivity;
import under.hans.com.flow.BudgetPlan.BudgetingActivity;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.Loaders.DayLineChartLoader;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.ViewPagers.TrendsViewPagerState;

/**
 * Created by Hans on 4/23/2018.
 */

public class TrendActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    private static final String TAG = "TrendActivity";
    private ViewPager trendViewPager;
    private FragmentStatePagerAdapter mPagerAdapter;
    private TextView tvSpent,tvSaved,tvSpentCurrency,tvSavedCurrency;
    private TextView tvDateTitle;
    private static final int LOADER_ID = 1020;
    private ImageView imgLeft,imgRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);

        setupWidgets();
        setupViewPager();
        setupNavigationDrawer();

        Bundle bundle = new Bundle();
        bundle.putInt("position",3000);

        getSupportLoaderManager().initLoader(LOADER_ID,bundle,cursorLoader);


    }

    private void setupWidgets(){

        tvDateTitle = (TextView)findViewById(R.id.tvDateTitle);
        tvDateTitle.setText(DateTimeUtils.getMonthYearString(0));
        tvSpent = (TextView)findViewById(R.id.tvSpent);
        tvSaved = (TextView)findViewById(R.id.tvSaved);
        imgLeft = (ImageView) findViewById(R.id.imgLeft);
        imgRight = (ImageView)findViewById(R.id.imgRight);


        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trendViewPager.setCurrentItem(trendViewPager.getCurrentItem()-1);
            }
        });
        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trendViewPager.setCurrentItem(trendViewPager.getCurrentItem()+1);
            }
        });
    }

    private void setupViewPager(){

        trendViewPager = (ViewPager)findViewById(R.id.trendsViewPager);
        mPagerAdapter = new TrendsViewPagerState(getSupportFragmentManager());
        trendViewPager.setAdapter(mPagerAdapter);
        trendViewPager.setCurrentItem(3000);

        trendViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int positionOffSet = position - 3000;
                tvDateTitle.setText(DateTimeUtils.getMonthYearString(positionOffSet));

                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                getSupportLoaderManager().restartLoader(LOADER_ID,bundle,cursorLoader);

            }
        });

    }
    private int getBudget(){
        Cursor mCursor;
        int budget;

        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
        String selection= SqlContractClass.UserSettingsClass._ID + "=?";
        String[] selectionArgs = {"1"};

        mCursor = getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if(mCursor != null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
            budget = mCursor.getInt(budgetIndex);

            return budget;
        }

        return 0;
    }

    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle args) {

            if(args!=null){
                int index = args.getInt("position") - 3000;

                String strStartDate = convertIndexToDate(index);
                String strEndDate = convertIndexToDate(index+1);

                String startQueryMillis = String.valueOf(DateTimeUtils.trendsDateConversion(strStartDate));
                String endQueryMillis = String.valueOf(DateTimeUtils.trendsDateConversion(strEndDate)-1);

                String[] projection = {SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};
                String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC
                        + " BETWEEN ? AND ?";
                String[] selectionArgs = {startQueryMillis,endQueryMillis};

                return new CursorLoader(getBaseContext(),
                        SqlContractClass.SpendingsEntryClass.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,null);



            }else{
                Toast.makeText(TrendActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            int totalAmount = 0;

            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                int amtIndex = cursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int amount  = cursor.getInt(amtIndex);

                totalAmount = amount + totalAmount;

            }
            tvSpent.setText(FormatAlgorithms.getFormattedFunds(totalAmount));

            int saved = (getBudget()*100) - totalAmount;
            if(saved < 0){
                tvSaved.setText(String.valueOf(0));
            }else {
                tvSaved.setText(FormatAlgorithms.getFormattedFunds(saved));
            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            tvSpent.setText("");
        }
    };

    /** ---------------------- Level 2 Setup UI ---------------------------------------**/


    private void setupNavigationDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(TrendActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_preset) {
            Intent intent = new Intent(TrendActivity.this, PresetOverview.class);
            startActivity(intent);


        } else if (id == R.id.nav_budget) {
            Intent intent = new Intent(TrendActivity.this, BudgetingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_trends) {


        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(TrendActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static int getCurrentMonthIndex(){
        Calendar calendar= Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        return month;

    }

    public static String convertIndexToDate(int pos){

        int index = pos+getCurrentMonthIndex();
        Calendar calendar = Calendar.getInstance();
        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");
        calendar.set(Calendar.MONTH,index);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String strDate = df.format(calendar.getTime());

        return strDate;

    }

}
