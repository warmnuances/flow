package under.hans.com.flow.ViewPagers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import under.hans.com.flow.Preset.PresetCategoryFragment;
import under.hans.com.flow.Preset.PresetInflowFragment;
import under.hans.com.flow.Preset.PresetSpendingsFragment;

/**
 * Created by Hans on 3/8/2018.
 */

public class PresetViewPagerAdapter extends FragmentPagerAdapter {


    /** Tabs
     * Spendings | Inflow | Categories | Labels
     */

    private static final int NUM_ITEMS = 3;
    private FragmentManager fm;

    public PresetViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PresetSpendingsFragment presetSpendings = new PresetSpendingsFragment();
                return presetSpendings;

            case 1:
                PresetInflowFragment presetInflow = new PresetInflowFragment();
                return presetInflow;


            case 2:
                PresetCategoryFragment presetCategory = new PresetCategoryFragment();
                return presetCategory;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
