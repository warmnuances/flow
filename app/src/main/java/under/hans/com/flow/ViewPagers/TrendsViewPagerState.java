package under.hans.com.flow.ViewPagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import under.hans.com.flow.FirstFragment;

public class TrendsViewPagerState extends FragmentStatePagerAdapter {
    private FragmentManager fragmentManager;

    private static final String TAG = "TrendsViewPagerAdapter";

    public TrendsViewPagerState(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return TrendsCurrentFragment.initTrendFrag(position);
//
//            case 1:
//                return TrendsLastFragment.initTrendFrag(position);
//
//            default:
//                return null;
//        }

        return FirstFragment.initTrendFrag(position);

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}
