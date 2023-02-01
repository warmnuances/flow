package under.hans.com.flow.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;

/**
 * Created by Hans on 4/21/2018.
 */

public class WeeksBarChartLoader extends AsyncTaskLoader<List<BarEntry>> {

    private static final String TAG = "LineChartLoader";

    String mCategory;
    Context mContext;

    public WeeksBarChartLoader(Context context, String category) {
        super(context);
        this.mCategory = category;
        this.mContext = context;
    }

    @Override
    public List<BarEntry> loadInBackground() {
        final int WEEK_INTERVAL = 604800;

        //Week 1 start and end
        int startWeek1Interval = DateTimeUtils.getLastMonthMillis();
        int endWeek1Interval = (DateTimeUtils.getLastMonthMillis() + WEEK_INTERVAL) - 1;

        //Week 2 start and end
        int startWeek2Interval = DateTimeUtils.getLastMonthMillis() + WEEK_INTERVAL;
        int endWeek2Interval = (DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL*2)) - 1 ;

        //Week 3 start and end
        int startWeek3Interval = DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL*2);
        int endWeek3Interval = (DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL*3)) - 1 ;

        //Week 4 start and end
        int startWeek4Interval = DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL*3);
        int endWeek4Interval = (DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL*4)) - 1 ;

        //Week 5 start and end
        int startWeek5Interval = DateTimeUtils.getLastMonthMillis() + (WEEK_INTERVAL *4);
        int endWeek5Interval = DateTimeUtils.getThisMonthMillis() - 1;


        ArrayList<BarEntry> entrySets = new ArrayList<>();

        //Week 1
        BarEntry week1Entries = new BarEntry(0,
                FormatAlgorithms.getFloatfromInt(
                        getWeekTotal(mContext,convertString(startWeek1Interval),convertString(endWeek1Interval)
                        )));

        BarEntry week2Entries = new BarEntry(1,
                FormatAlgorithms.getFloatfromInt(
                        getWeekTotal(mContext,convertString(startWeek2Interval),convertString(endWeek2Interval)
                        )));
        BarEntry week3Entries = new BarEntry(2,
                FormatAlgorithms.getFloatfromInt(
                getWeekTotal(mContext,convertString(startWeek3Interval),convertString(endWeek3Interval)
                )));

        BarEntry week4Entries = new BarEntry(3,
                FormatAlgorithms.getFloatfromInt(
                getWeekTotal(mContext,convertString(startWeek4Interval),convertString(endWeek4Interval)
                )));

        BarEntry week5Entries = new BarEntry(4,
                FormatAlgorithms.getFloatfromInt(
                getWeekTotal(mContext,convertString(startWeek5Interval),convertString(endWeek5Interval)
                )));

        entrySets.add(week1Entries);
        entrySets.add(week2Entries);
        entrySets.add(week3Entries);
        entrySets.add(week4Entries);
        entrySets.add(week5Entries);


        return entrySets;
    }

    private int getWeekTotal(Context mContext,String startTimeSec,String endTimeSec){

        Cursor mCursor;
        int total = 0;

        String[] projection = {
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};

        String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY  + "=? AND " +
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
        String[] selectionArgs = {mCategory,startTimeSec,endTimeSec};

        mCursor = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){

            int amountId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
            int amount = mCursor.getInt(amountId);

            total = total + amount;

        }
        return total;

    }

    private String convertString(int intValue){
        return String.valueOf(intValue);
    }
}
