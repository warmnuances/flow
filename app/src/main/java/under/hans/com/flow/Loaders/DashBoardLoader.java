package under.hans.com.flow.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.models.Transaction;

/**
 * Created by Hans on 4/4/2018.
 */

public class DashBoardLoader extends AsyncTaskLoader<List<Transaction>>{

    private List<Transaction> transactionsDataset;
    private String strArgs;
    Context mContext;
    private static final String TAG = "DashboardAsyncLoader";

    private List<Transaction> firstdataset = new ArrayList<>();
    private List<Transaction> dataset = new ArrayList<>();

    Transaction transaction ;

    public DashBoardLoader(Context context, List<Transaction> transactionArrayList,String strQuery) {
        super(context);
        this.transactionsDataset = transactionArrayList;
        this.mContext = context;
        this.strArgs = strQuery;
    }


    @Override
    public List<Transaction> loadInBackground() {
        if(strArgs.equals("")){
            transactionsDataset = sortDataList();
            return transactionsDataset;
        }
        else{
            transactionsDataset = sortSearchList();
            return transactionsDataset;
        }

    }

    private List<Transaction> sortSearchList(){
        final List<Transaction> unsortedlist = FillSearchList();


        List<Transaction> sortedlist = new ArrayList<>();

        Collections.sort(unsortedlist);

        for(Transaction t: unsortedlist){
            sortedlist.add(t);
        }


        return sortedlist;
    }

    private List<Transaction> FillSearchList(){

        String[] projection1 = {SqlContractClass.SpendingsEntryClass._ID
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC};

        String selection1 = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME + " LIKE ?";

        String[] selectionArgs1 = {"%"+strArgs+"%"};

        String[] projection2 = {SqlContractClass.InflowEntryClass._ID
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC};

        String selection2 = SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME + " LIKE ?";

        String[] selectionArgs2 = {"%"+strArgs+"%"};

        Cursor c1 = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI
                ,projection1
                ,selection1
                ,selectionArgs1
                , SqlContractClass.SpendingsEntryClass._ID + " DESC"
                ,null);

        Cursor c2 = mContext.getContentResolver().query(SqlContractClass.InflowEntryClass.CONTENT_URI
                ,projection2
                ,selection2
                ,selectionArgs2
                ,null
                ,null);


        for(c1.moveToFirst();!c1.isAfterLast();c1.moveToNext()){

            int spendingsIndexId = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass._ID);
            int amtIndex  = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
            int nameIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME);
            int categoryIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY);
            int dateIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE);
            int timesecIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC);



            int spendingsID = c1.getInt(spendingsIndexId);
            int amount = c1.getInt(amtIndex);
            String name = c1.getString(nameIndex);
            String category = c1.getString(categoryIndex);
            String date = c1.getString(dateIndex);
            int timeSec = c1.getInt(timesecIndex);

            String day = "",month = "";



            Uri transactionUri = ContentUris.withAppendedId(SqlContractClass.SpendingsEntryClass.CONTENT_URI
                    ,spendingsID);


            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


            try {
                calendar.setTime(dateFormat.parse(date));

                month = String.valueOf(calendar.get(Calendar.MONTH));
                day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            }catch (Exception e){
                e.printStackTrace();
            }


            transaction = new Transaction();
            transaction.setmUri(transactionUri);
            transaction.setAmount(amount);
            transaction.setName(name);
            transaction.setCategory(category);
            transaction.setMonth(DateTimeUtils.getMonthName(Integer.parseInt(month)));
            transaction.setDay(day);
            transaction.setTimeMillis(timeSec);
            transaction.setType(1);
            firstdataset.add(transaction);
        }



        //Total days eqn: Assuming 31 days in a month, 372 days in a year
        for(c2.moveToFirst();!c2.isAfterLast();c2.moveToNext()){

            int inflowIndexID = c2.getColumnIndex(SqlContractClass.InflowEntryClass._ID);
            int amountIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT);
            int nameIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME);
            int categoryIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY);
            int dateIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE);
            int timesecIndex= c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC);

            int inflowID = c2.getInt(inflowIndexID);
            int amount = c2.getInt(amountIndex);
            String name = c2.getString(nameIndex);
            String category = c2.getString(categoryIndex);
            String date = c2.getString(dateIndex);
            int timeSec = c2.getInt(timesecIndex);

            Uri transactionUri = ContentUris.withAppendedId(SqlContractClass.InflowEntryClass.CONTENT_URI
                    ,inflowID);

            String day = "",month = "";

            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                calendar.setTime(dateFormat.parse(date));

                month = String.valueOf(calendar.get(Calendar.MONTH));
                day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            }catch (Exception e){
            }


            transaction = new Transaction();
            transaction.setmUri(transactionUri);
            transaction.setAmount(amount);
            transaction.setName(name);
            transaction.setCategory(category);
            transaction.setMonth(DateTimeUtils.getMonthName(Integer.parseInt(month)));
            transaction.setDay(day);
            transaction.setType(2);
            transaction.setTimeMillis(timeSec);
            dataset.add(transaction);
        }

        dataset.addAll(firstdataset);

        return dataset;
    }


    private List<Transaction> sortDataList(){

        final List<Transaction> unsortedList = FillDataList();


        List<Transaction> sortedList = new ArrayList<>();

        Collections.sort(unsortedList);

        for(Transaction t: unsortedList){
            sortedList.add(t);
        }


        return sortedList;
    }

    private List<Transaction> FillDataList(){

        String[] projection1 = {SqlContractClass.SpendingsEntryClass._ID
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE
                , SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC};

        String[] projection2 = {SqlContractClass.InflowEntryClass._ID
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE
                , SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC};

        Cursor c1 = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI
                ,projection1
                ,null
                ,null
                , SqlContractClass.SpendingsEntryClass._ID + " DESC"
                ,null);

        Cursor c2 = mContext.getContentResolver().query(SqlContractClass.InflowEntryClass.CONTENT_URI
                ,projection2
                ,null
                ,null
                ,null
                ,null);


        for(c1.moveToFirst();!c1.isAfterLast();c1.moveToNext()){

            int spendingsIndexId = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass._ID);
            int amtIndex  = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
            int nameIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME);
            int categoryIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY);
            int dateIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE);
            int timesecIndex = c1.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC);



            int spendingsID = c1.getInt(spendingsIndexId);
            int amount = c1.getInt(amtIndex);
            String name = c1.getString(nameIndex);
            String category = c1.getString(categoryIndex);
            String date = c1.getString(dateIndex);
            int timeSec = c1.getInt(timesecIndex);

            String day = "",month = "";



            Uri transactionUri = ContentUris.withAppendedId(SqlContractClass.SpendingsEntryClass.CONTENT_URI
                    ,spendingsID);


            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


            try {
                calendar.setTime(dateFormat.parse(date));

                month = String.valueOf(calendar.get(Calendar.MONTH));
                day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            }catch (Exception e){

            }


            transaction = new Transaction();
            transaction.setmUri(transactionUri);
            transaction.setAmount(amount);
            transaction.setName(name);
            transaction.setCategory(category);
            transaction.setMonth(DateTimeUtils.getMonthName(Integer.parseInt(month)));
            transaction.setDay(day);
            transaction.setTimeMillis(timeSec);
            transaction.setType(1);
            firstdataset.add(transaction);
        }



        //Total days eqn: Assuming 31 days in a month, 372 days in a year
        for(c2.moveToFirst();!c2.isAfterLast();c2.moveToNext()){

            int inflowIndexID = c2.getColumnIndex(SqlContractClass.InflowEntryClass._ID);
            int amountIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT);
            int nameIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME);
            int categoryIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY);
            int dateIndex = c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE);
            int timesecIndex= c2.getColumnIndex(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC);

            int inflowID = c2.getInt(inflowIndexID);
            int amount = c2.getInt(amountIndex);
            String name = c2.getString(nameIndex);
            String category = c2.getString(categoryIndex);
            String date = c2.getString(dateIndex);
            int timeSec = c2.getInt(timesecIndex);

            Uri transactionUri = ContentUris.withAppendedId(SqlContractClass.InflowEntryClass.CONTENT_URI
                    ,inflowID);


            String day = "",month = "";

            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            try {
                calendar.setTime(dateFormat.parse(date));

                month = String.valueOf(calendar.get(Calendar.MONTH));
                day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

            }catch (Exception e){
            }


            transaction = new Transaction();
            transaction.setmUri(transactionUri);
            transaction.setAmount(amount);
            transaction.setName(name);
            transaction.setCategory(category);
            transaction.setMonth(DateTimeUtils.getMonthName(Integer.parseInt(month)));
            transaction.setDay(day);
            transaction.setType(2);
            transaction.setTimeMillis(timeSec);
            dataset.add(transaction);
        }

        dataset.addAll(firstdataset);

        return dataset;
    }
}
