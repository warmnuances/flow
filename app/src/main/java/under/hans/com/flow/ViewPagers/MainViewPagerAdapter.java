package under.hans.com.flow.ViewPagers;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import under.hans.com.flow.Home.BudgetPageFragment;
import under.hans.com.flow.Home.DashBoardFragment;

/**
 * Created by Hans on 3/2/2018.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    /** Tabs
     * Dashboard | Statistics
     */

    private static final int NUM_ITEMS = 2;
    private FragmentManager fragmentManager;
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DashBoardFragment dashBoardFragment = new DashBoardFragment();
                return dashBoardFragment;

            case 1:
                BudgetPageFragment budgetPageFragment = new BudgetPageFragment();
                return budgetPageFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }
}
