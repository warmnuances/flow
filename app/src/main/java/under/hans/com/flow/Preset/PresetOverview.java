package under.hans.com.flow.Preset;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import under.hans.com.flow.About.AboutActivity;
import under.hans.com.flow.BudgetPlan.BudgetingActivity;
import under.hans.com.flow.Forms.AddCategoryActivity;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.Trends.TrendActivity;
import under.hans.com.flow.ViewPagers.PresetViewPagerAdapter;
import under.hans.com.flow.Forms.AddPresetActivity;
import under.hans.com.flow.R;

/**
 * Created by Hans on 3/2/2018.
 */

public class PresetOverview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentPagerAdapter presetPagerAdapter;
    private FloatingActionButton fabPreset;

    private ViewPager mViewPager;
    private int stack;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);
        setupViewpager();
        setupNavigationDrawer();

        Intent intent = getIntent();
        if(intent !=null){
            page = intent.getIntExtra("page",0);
            mViewPager.setCurrentItem(page);
        }


    }

    private void setupViewpager(){
        mViewPager = (ViewPager)findViewById(R.id.presetViewPager);
        presetPagerAdapter = new PresetViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(presetPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    stack = 0;
                }else if(position == 1){
                    stack = 1;
                }
                else if(position == 2){
                    stack = 1;
                }
                else {
                    Toast.makeText(PresetOverview.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    /** ---------------------- Level 2 Setup UI ---------------------------------------**/


    private void setupNavigationDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setOverflowIcon(getDrawable(R.drawable.overflow_icon));
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
            if(stack >= 1){
                mViewPager.setCurrentItem(0);
            }else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(PresetOverview.this, MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_preset) {



        } else if (id == R.id.nav_budget) {
            Intent intent = new Intent(PresetOverview.this, BudgetingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_trends) {

            Intent intent = new Intent(PresetOverview.this, TrendActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(PresetOverview.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
