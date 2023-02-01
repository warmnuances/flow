package under.hans.com.flow.ViewPagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import under.hans.com.flow.Start.StartBudgetFragment;
import under.hans.com.flow.Start.StartFirstFragment;
import under.hans.com.flow.Start.StartTipsFragment;

public class StartViewPagerAdapter extends FragmentPagerAdapter {

    private final static int NUM_ITEMS = 3;
    public StartViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                StartFirstFragment firstFragment = new StartFirstFragment();
                return firstFragment;

            case 1:
                StartBudgetFragment budgetFragment = new StartBudgetFragment();
                return budgetFragment;

            case 2:
                StartTipsFragment startTipsFragment = new StartTipsFragment();
                return startTipsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
