//package under.hans.com.releasecandidate.ViewPagers;
//
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import under.hans.com.releasecandidate.FirstFragment;
//import under.hans.com.releasecandidate.Trends.TrendsCurrentFragment;
//import under.hans.com.releasecandidate.Trends.TrendsLastFragment;
//
///**
// * Created by Hans on 4/23/2018.
// */
//
//public class TrendsViewPagerAdapter extends FragmentPagerAdapter {
//
//    private FragmentManager fragmentManager;
//
//    private static final String TAG = "TrendsViewPagerAdapter";
//
//    public TrendsViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//        fragmentManager = fm;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
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
//
//
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return 2;
//    }
//
//}
