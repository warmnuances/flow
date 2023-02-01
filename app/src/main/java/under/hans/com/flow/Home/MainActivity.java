package under.hans.com.flow.Home;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import under.hans.com.flow.About.AboutActivity;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Dialogs.MainBudgetDialog;
import under.hans.com.flow.Start.StartActivity;
import under.hans.com.flow.Trends.TrendActivity;
import under.hans.com.flow.ViewPagers.MainViewPagerAdapter;
import under.hans.com.flow.BudgetPlan.BudgetingActivity;
import under.hans.com.flow.BuildConfig;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;

import under.hans.com.flow.Data.SqlContractClass.CategoryClass;

import under.hans.com.flow.Utils.DatabaseUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainBudgetDialog.MainBudgetDialogListener{

    private static final String TAG = "MainActivity";

    Toolbar toolbar;
    ViewPager mViewPager;

    private boolean isFirstRun = false;
    public MainViewPagerAdapter adapter;

    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirstRun();
        setupNavigationDrawer();
        setupViewPager();

    }


    /** ---------------------- Level 3: Check first run---------------------------------------**/

    private void checkFirstRun(){

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        //Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        //Get Saved Version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        //Case 1: Normal Run
        if(currentVersionCode == savedVersionCode){
            boolean boolInput = CheckIfBudgetHasInput();
            Log.d(TAG, "checkFirstRun: boolinput == " + boolInput);
            if(!boolInput){
                Intent intent = new Intent(MainActivity.this, StartActivity.class);
                intent.putExtra("page",1);
                startActivity(intent);
            }
        }
        //Case 2: First run
        else if(savedVersionCode == DOESNT_EXIST){
            isFirstRun = true;
            DatabaseUtils.addNewUser(getApplicationContext());
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            init();

            //FakeData.insertFakeData(getApplicationContext());

        }
        //Case 3 : Upgrade
        else if (currentVersionCode > savedVersionCode){

        }

        prefs.edit().putInt(PREF_VERSION_CODE_KEY,currentVersionCode).apply();
    }

    private boolean CheckIfBudgetHasInput(){

        int budget=0;

        Uri userUri = ContentUris.withAppendedId(SqlContractClass.UserSettingsClass.CONTENT_URI,
                1);

        Cursor mCursor = getContentResolver().query(userUri,null,null,null,null);

//        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
//        String selection = SqlContractClass.UserSettingsClass._ID + "=?";
//        String[] selectionArgs = {"1"};
//
//        Cursor mCursor = getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null);

        if(mCursor != null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
            budget = mCursor.getInt(budgetIndex);

            if(budget == 0){
                return false;
            }
            else {
                return true;
            }
        }
        Log.d(TAG, "CheckIfBudgetHasInput: userUri = " + userUri);
        Log.d(TAG, "CheckIfBudgetHasInput: mCursor size = " + mCursor.getCount());
        Log.d(TAG, "CheckIfBudgetHasInput: Budget == " + budget);
        return false;
    }

    public void init(){

        Context context = getApplicationContext();
        List<ContentValues> categoriesList = DatabaseUtils.getCategoriesList(context);

        try {

            for (ContentValues cv: categoriesList){
                Log.d(TAG, "init: getContentValues: " + cv.get(CategoryClass.COLUMN_CATEGORY_NAME));
                getContentResolver().insert(CategoryClass.CONTENT_URI, cv);
            }
        }catch (SQLiteException e){
            Log.e(TAG, "init: failed to insert to sqlite database" );
        }

    }


    /** ---------------------- Level 2 Setup UI ---------------------------------------**/

    private void setupViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.vpPager);
        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

    }

    private void setupNavigationDrawer(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_front);
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
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_set_budget){
            promptBudgetDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptBudgetDialog(){

        MainBudgetDialog fragmentBudgetDialog = new MainBudgetDialog();
        fragmentBudgetDialog.show(getSupportFragmentManager(), "MainBudgetDialog");


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        } else if (id == R.id.nav_preset) {

            Intent intent = new Intent(MainActivity.this, PresetOverview.class);
            startActivity(intent);

        } else if (id == R.id.nav_budget) {

            Intent intent = new Intent(MainActivity.this, BudgetingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_trends) {

            Intent intent = new Intent(MainActivity.this, TrendActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogPositiveClick(String text) {

        int position = 1;
            Fragment fragment = adapter.getFragment(position);
            if(fragment !=null){
                ((BudgetPageFragment)fragment).onBudgetSet(text);
            }
    }


}
