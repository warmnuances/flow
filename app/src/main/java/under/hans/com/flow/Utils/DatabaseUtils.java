package under.hans.com.flow.Utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Data.SqlContractClass.InflowEntryClass;
import under.hans.com.flow.Data.SqlContractClass.SpendingsEntryClass;
import under.hans.com.flow.Data.SqlContractClass.UserSettingsClass;
import under.hans.com.flow.Home.MainActivity;
import under.hans.com.flow.R;
import under.hans.com.flow.models.Categories;

/**
 * Created by Hans on 3/2/2018.
 */

public class DatabaseUtils {

    private static final String TAG = "DatabaseUtils";

    public static void addNewUser(Context context){

        Context mContext = context;
        int lastUpdated = (int)System.currentTimeMillis();

        ContentValues cv = new ContentValues();
        cv.put(UserSettingsClass.COLUMN_USER_NAME, "User");
        cv.put(UserSettingsClass.COLUMN_USER_EMAIL, "");
        cv.put(UserSettingsClass.COLUMN_USER_BALANCE, FormatAlgorithms.setFormattedFunds("0"));
        cv.put(UserSettingsClass.COLUMN_USER_ACCOUNT, "Cash");
        cv.put(UserSettingsClass.COLUMN_USER_BUDGET,"0");
        cv.put(UserSettingsClass.COLUMN_USER_LAST_UPDATED, lastUpdated);

        mContext.getContentResolver().insert(UserSettingsClass.CONTENT_URI,cv);

    }

    public static void updateBalance(Context context, int amount, String bool){

        int totalBalance = 0;

        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE};
        String selection = SqlContractClass.UserSettingsClass._ID +  "=?";
        String[] selectionArgs ={"1"} ;

        Cursor cursor = context.getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);

        if(cursor != null && cursor.moveToFirst()){
            totalBalance = cursor.getInt(cursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE));
        }

        if(bool.equals("inflow")){
            totalBalance = totalBalance + amount;
        }
        else if(bool.equals("outflow")){
            totalBalance = totalBalance - amount;
        }


        ContentValues cv = new ContentValues();
        cv.put(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE, totalBalance);


        String whereClause = SqlContractClass.UserSettingsClass._ID + "=?";
        String[] whereArgs = {"1"};
        Uri uri = SqlContractClass.UserSettingsClass.CONTENT_URI;

        context.getContentResolver()
                .update(uri, cv,whereClause,whereArgs);


    }

    public static void updateBalanceOnDelete(Context context, int amount, String bool){

        int totalBalance = 0;

        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE};
        String selection = SqlContractClass.UserSettingsClass._ID +  "=?";
        String[] selectionArgs ={"1"} ;

        Cursor cursor = context.getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);

        if(cursor != null && cursor.moveToFirst()){
            totalBalance = cursor.getInt(cursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE));
        }

        if(bool.equals("inflow")){
            totalBalance = totalBalance - amount;
        }
        else if(bool.equals("outflow")){
            totalBalance = totalBalance + amount;
        }


        ContentValues cv = new ContentValues();
        cv.put(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE, totalBalance);


        String whereClause = SqlContractClass.UserSettingsClass._ID + "=?";
        String[] whereArgs = {"1"};
        Uri uri = SqlContractClass.UserSettingsClass.CONTENT_URI;

        context.getContentResolver()
                .update(uri, cv,whereClause,whereArgs);


    }


    public static void updateBalance(Context context, int oldAmount, String bool,int newAmount){
        int totalBalance = 0;
        int amount = newAmount - oldAmount ;

        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE};
        String selection = SqlContractClass.UserSettingsClass._ID +  "=?";
        String[] selectionArgs ={"1"} ;

        Cursor cursor = context.getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);

        if(cursor != null && cursor.moveToFirst()){
            totalBalance = cursor.getInt(cursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE));
        }

        if(bool.equals("inflow")){
            totalBalance = totalBalance + amount;
        }
        else if(bool.equals("outflow")){
            totalBalance = totalBalance - amount;
        }

        ContentValues cv = new ContentValues();
        cv.put(SqlContractClass.UserSettingsClass.COLUMN_USER_BALANCE, totalBalance);


        String whereClause = SqlContractClass.UserSettingsClass._ID + "=?";
        String[] whereArgs = {"1"};
        Uri uri = SqlContractClass.UserSettingsClass.CONTENT_URI;

        context.getContentResolver()
                .update(uri, cv,whereClause,whereArgs);


    }

    public static List<ContentValues> getCategoriesList(Context context){

        List<ContentValues> cvList = new ArrayList<>();


        ContentValues cv;
        cv = new ContentValues();
        int imgResFood = R.drawable.food_and_beverage;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,
                context.getResources().getString(R.string.Food_and_beverage));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResFood);
        cvList.add(cv);


        cv = new ContentValues();
        int imgResTransport = R.drawable.transport;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,
                context.getResources().getString(R.string.Transport));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResTransport);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResSubscription = R.drawable.subscription;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,context.getResources().getString(R.string.Subscription));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResSubscription);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResShopping = R.drawable.shopping;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,
                context.getResources().getString(R.string.Shopping));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResShopping);
        cvList.add(cv);


        cv = new ContentValues();
        int imgResEntertainment = R.drawable.entertainment;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME, context.getResources().getString(R.string.Entertainment));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResEntertainment);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResPersonalCare = R.drawable.personal_care;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME, context.getResources().getString(R.string.Personal_care));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResPersonalCare);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResEducation = R.drawable.education;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME, context.getResources().getString(R.string.Education));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResEducation);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResTravel = R.drawable.travel;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,context.getResources().getString(R.string.Travel));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResTravel);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResHealthCare = R.drawable.healthcare;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME, context.getResources().getString(R.string.Healthcare));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResHealthCare);
        cvList.add(cv);

        cv = new ContentValues();
        int imgResInvestment = R.drawable.investment;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,context.getResources().getString(R.string.Investment));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResInvestment);
        cvList.add(cv);


        cv = new ContentValues();
        int imgResBills = R.drawable.bills;
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME, context.getResources().getString(R.string.Bills));
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,10);
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH,imgResBills);
        cvList.add(cv);

        return cvList;
    }

    public static List<Categories> getCategoryDataset(Context context){

        Categories objCategory;
        List<Categories> categoryDataset = new ArrayList<>();

        String[] projection = { SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY,
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_RECENT,
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_USAGE,
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH};


        Cursor mCursor = context.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                projection,
                null,
                null,
                null,
                null);

        if(mCursor !=null){
            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
                int nameIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME);
                int priorityIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PRIORITY);
                int usageIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_USAGE);
                int pathIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH);

                String name = mCursor.getString(nameIndex);
                int priority = mCursor.getInt(priorityIndex);
                int usage = mCursor.getInt(usageIndex);
                int pathRes = mCursor.getInt(pathIndex);


                boolean isPriority;

                isPriority = FormatAlgorithms.checkPriority(context,name);
                int totalPriority = FormatAlgorithms.getCategoryPriority(usage,priority,isPriority);
                objCategory = new Categories();



                objCategory.setPathRes(pathRes);
                objCategory.setCategoryName(name);
                objCategory.setCompareSort(totalPriority);
                categoryDataset.add(objCategory);
            }

            mCursor.close();
        }
        return categoryDataset;
    }

    public static void insertItemOnNotify(Context context,Cursor mCursor){

        Uri contentUri;
        String insertName,insertCategory,insertAccount,insertDate,insertAccType;
        int insertAmt, insertTimeSec, getEndTimeSec,getInterval;

        getEndTimeSec = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END);
        getInterval = SqlContractClass.getColumnInt(mCursor,SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL);

        insertName = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_NAME);
        insertCategory = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY);
        insertAccount = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_ACCOUNT);
        insertDate = DateTimeUtils.getCurrentDate();
        insertAccType = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE);

        insertAmt = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT);
        insertTimeSec =(int)(System.currentTimeMillis()/1000);


        updateBalance(context,insertAmt,insertAccType);

        if(insertAccType.equals("inflow")){
            contentUri = InflowEntryClass.CONTENT_URI;

            ContentValues cv = new ContentValues();
            cv.put(InflowEntryClass.COLUMN_INFLOW_NAME, insertName);
            cv.put(InflowEntryClass.COLUMN_INFLOW_CATEGORY, insertCategory);
            cv.put(InflowEntryClass.COLUMN_INFLOW_ACCOUNT, insertAccount);
            cv.put(InflowEntryClass.COLUMN_INFLOW_DATE, insertDate);
            cv.put(InflowEntryClass.COLUMN_INFLOW_AMOUNT, insertAmt);
            cv.put(InflowEntryClass.COLUMN_INFLOW_TIMESEC, insertTimeSec);

            context.getContentResolver().insert(contentUri, cv);
            context.getContentResolver().notifyChange(contentUri, null);
            

        }else if(insertAccType.equals("outflow")) {
            contentUri = SpendingsEntryClass.CONTENT_URI;

            ContentValues cv = new ContentValues();
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_NAME, insertName);
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY, insertCategory);
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT, insertAccount);
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_DATE, insertDate);
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT, insertAmt);
            cv.put(SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC, insertTimeSec);
            context.getContentResolver().insert(contentUri, cv);
            context.getContentResolver().notifyChange(contentUri, null);
        }

        else{
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        }


    }

    public static int getDatabaseCount(Context context){
        int totalRecords;
        Cursor mCursorSpendings,mCursorInflow;


        mCursorSpendings = context.getContentResolver().query(SpendingsEntryClass.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);

        mCursorInflow = context.getContentResolver().query(InflowEntryClass.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);

        totalRecords = mCursorSpendings.getCount() + mCursorInflow.getCount();


        return totalRecords;

    }

    public static void updateUserBudget(Context context, int budgetSet){

        int budget = budgetSet;

        Uri userUri = ContentUris.withAppendedId(SqlContractClass.UserSettingsClass.CONTENT_URI,
                1);

        ContentValues cv = new ContentValues();
        cv.put(UserSettingsClass.COLUMN_USER_BUDGET, budget);

        int rowsUpdated = context.getContentResolver().update(userUri,cv,null,null);

        if(rowsUpdated != -1){

        }else{

        }

    }


    public static String getUserBudget(Context context){

        Cursor mCursor;
        String[] projection = {UserSettingsClass.COLUMN_USER_BUDGET};
        String selection = UserSettingsClass._ID + "=?";
        String[] selectionArgs = {"1"};

        mCursor =context.getContentResolver().query(UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

        if(mCursor !=null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(UserSettingsClass.COLUMN_USER_BUDGET);
            int budget = mCursor.getInt(budgetIndex);

            mCursor.close();
            return String.valueOf(budget);
        }
        else{
            return "0";
        }


    }

    public static void updateCategoryBudget(Context context, int budgetSet, Uri categoryUri){

        ContentValues cv = new ContentValues();
        cv.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET, budgetSet);

        context.getContentResolver().update(categoryUri,cv,null,null);

    }

    public static String getCategoryBudget(Context context,Uri categoryUri){

        Cursor mCursor;
        String[] projection = {SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET};

        mCursor =context.getContentResolver().query(categoryUri,
                projection,
                null,
                null,
                null);

        if(mCursor !=null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET);
            int budget = mCursor.getInt(budgetIndex);

            mCursor.close();
            return String.valueOf(budget);
        }
        else{
            return "0";
        }

    }

    public static String getPercentage(Context context,String category){

        int spendings = 0, budget = 0;
        float percentage,result,total = 0;
        String thisTimeSec = String.valueOf(DateTimeUtils.getLastMonthMillis());
        String nextTimeSec = String.valueOf(DateTimeUtils.getThisMonthMillis());


        /**---------------------------------Budget Cursor---------------------------------------**/

        String[] projection1 = {SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET};
        String selection1 = SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME + "=?";
        String[] selectionArgs1 = {category};
        Cursor c1 = context.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                projection1,
                selection1,
                selectionArgs1,
                null);

        if(c1!=null && c1.moveToFirst()){
            int budgetIndex = c1.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET);
            budget = c1.getInt(budgetIndex);
        }



        /**---------------------------------Spendings Cursor---------------------------------------**/

        String[] projection2 = {SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};
        String selection2 = SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY  + "=? AND " +
                SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;

        String[] selectionArgs2 = {category,thisTimeSec,nextTimeSec};

        Cursor c2 = context.getContentResolver().query(SpendingsEntryClass.CONTENT_URI,
                projection2,
                selection2,
                selectionArgs2,
                null);




        for(c2.moveToFirst();!c2.isAfterLast();c2.moveToNext()){
            int amountIndex = c2.getColumnIndex(SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
            int amount = c2.getInt(amountIndex);

            spendings = spendings + amount;

        }


        /**---------------------------------Result---------------------------------------**/
        if(budget == 0){
            return "0";
        }
        else {
            percentage = spendings/budget;

            result = Math.round(percentage*100)/100;

            total = 100 - result;

            c1.close();
            c2.close();

            return String.valueOf(total);
        }
    }

    public static int getCategoryPosition(Context context, String category){

        String[] projection = {SqlContractClass.CategoryClass._ID};
        String selection = SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME + " =?";
        String[] selectionArgs = {category};

        Cursor mCursor = context.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);

        if(mCursor!= null && mCursor.moveToFirst()){
            int idIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass._ID);
            int id = mCursor.getInt(idIndex)-1;

            mCursor.close();
            return id;
        }else{
            return 0;
        }

    }

    public static int getBudget(Context mContext){
        int budget = 0  ;
        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
        Cursor mCursor = mContext.getContentResolver().query(
                SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                null,
                null,
                null,
                null);

        if(mCursor!= null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
            budget = mCursor.getInt(budgetIndex);

            mCursor.close();
            return budget;
        }

        return 0;
    }

    public static int getCategoryPath(Context context, String name){
        String[] category = {SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH};
        String selection = SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME + "=?";
        String[] selectionArgs ={name};

        Cursor mCursor = context.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                category,
                selection,
                selectionArgs,
                null);

        if(mCursor!=null && mCursor.moveToFirst()){
            int path = mCursor.getInt(
                    mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PATH));

            return path;
        }
        else {
            return R.drawable.ic_categories;
        }
    }



}
