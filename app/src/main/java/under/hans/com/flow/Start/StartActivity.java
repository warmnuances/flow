package under.hans.com.flow.Start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import under.hans.com.flow.Dialogs.MainBudgetDialog;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.CustomViewPager;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.ViewPagers.StartViewPagerAdapter;
import under.hans.com.flow.ViewPagers.TrendsViewPagerState;

public class StartActivity extends AppCompatActivity implements MainBudgetDialog.MainBudgetDialogListener {

    private static final String TAG = "StartActivity";

    CustomViewPager mViewPager;
    StartViewPagerAdapter startAdapter;
    Button btNext;
    int count =0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setupViewPager();

        int getPage = getIntent().getIntExtra("page",0);
        if(getPage == 1){
            mViewPager.setCurrentItem(1);
        }

        btNext = (Button)findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                count = count+1;
                if(count == 3){
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void setupViewPager(){

        mViewPager = (CustomViewPager)findViewById(R.id.startPager);
        startAdapter = new StartViewPagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager, true);

        mViewPager.setPagingEnabled(false);
        mViewPager.setAdapter(startAdapter);
    }

    @Override
    public void onDialogPositiveClick(String text) {
        int budgetSet;
        budgetSet = Integer.parseInt(text);
        Log.d(TAG, "onDialogPositiveClick: budget  =  " + budgetSet);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:"
                + R.id.startPager + ":" + 1);
        ((StartBudgetFragment)page).etBudgetClick.setText("$" + text);

        DatabaseUtils.updateUserBudget(this,budgetSet);
    }
}
