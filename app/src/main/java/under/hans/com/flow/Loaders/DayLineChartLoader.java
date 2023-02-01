package under.hans.com.flow.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;

/**
 * Created by Hans on 03/05/2018.
 */

public class DayLineChartLoader extends AsyncTaskLoader<List<Entry>> {

    private static final String TAG = "DayLineChartLoader";

    private int NUM_DAYS_MONTH = 0;
    private String stringDate;
    private Context mContext;
    private Cursor mCursor;
    private List<Entry> dataEntries;

    public DayLineChartLoader(Context context, String strDate) {
        super(context);
        this.NUM_DAYS_MONTH = getDaysInMonth(strDate);
        this.mContext = context;
        this.stringDate = strDate;
    }

    @Override
    public List<Entry> loadInBackground() {
        dataEntries = new ArrayList<>();

        for (int i = 0; i < NUM_DAYS_MONTH; i++){
            dataEntries.add(setDataOnDay(i));
        }

        return dataEntries;
    }

    private Entry setDataOnDay(int i){

        Entry spendingsEntry;
        int amountTotal = 0, startMillis, endMillis;


        startMillis = DateTimeUtils.trendsDateConversion(stringDate) + ((i)*86400) + 86400;
        endMillis = startMillis + 86400-1;

        String startQueryMillis = String.valueOf(startMillis);
        String endQueryMillis = String.valueOf(endMillis);




        String[] projection = {
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,
                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};

        String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
        String[] selectionArgs = {startQueryMillis,endQueryMillis};

        mCursor = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);


        int timeSecs=0;

        if(mCursor.getCount() != 0){
            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){

                int amountId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int amount = mCursor.getInt(amountId);

                int timeSecId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC);
                timeSecs= mCursor.getInt(timeSecId);



                amountTotal = amountTotal + amount;
            }


        }

        else {

        }

        spendingsEntry = new Entry(i+1,FormatAlgorithms.getFloatfromInt(amountTotal));

        return spendingsEntry;

    }

//    private int getYEntriesOnDay(int i){
//
//        int amountTotal = 0, startMillis, endMillis;
//
//
//
//        startMillis = DateTimeUtils.convertDateToIntMillis(stringDate) + (i*86400);
//        endMillis = startMillis + 86400;
//
//        String startQueryMillis = String.valueOf(startMillis);
//        String endQueryMillis = String.valueOf(endMillis);
//
//
//                "i = " + i +
//                "Calculated StartMillis = " + DateTimeUtils.convertDateToIntMillis(stringDate) +
//                "Calculated Millis + index = " + startMillis
//        + "endQueryMillis = " + convertMillisToDate(endMillis));
//
//        String[] projection = {
//                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,
//                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,
//                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};
//
//        String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
//        String[] selectionArgs = {startQueryMillis,endQueryMillis};
//
//        mCursor = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null);
//
//
//        int timeSecs=0;
//
//        if(mCursor.getCount() != 0){
//            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
//
//                int amountId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
//                int amount = mCursor.getInt(amountId);
//
//                int timeSecId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC);
//                timeSecs= mCursor.getInt(timeSecId);
//
//
//
//                amountTotal = amountTotal + amount;
//            }
//
//
//
//            return amountTotal;
//        }
//
//        else {
//            return 0;
//        }
//    }

    private int getDaysInMonth(String strDate){

        int maxDay = 0;
        Calendar calendar = Calendar.getInstance();
        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");

        try {
            calendar.setTime(df.parse(strDate));
            maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        }catch (Exception e){
            e.printStackTrace();
        }

        return maxDay;

    }

    public static String convertMillisToDate(int Millis){
        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format((long)Millis*1000);
        return date;
    }

}
