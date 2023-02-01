package under.hans.com.flow.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.models.BudgetCategory;

public class BudgetPageLoader extends AsyncTaskLoader<List<BudgetCategory>> {

    List<BudgetCategory> budgetCategoryList;
    Cursor categoryCursor,mCursor;
    Context mContext;

    public BudgetPageLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public List<BudgetCategory> loadInBackground() {
        budgetCategoryList = new ArrayList<>();
        BudgetCategory budgetCategory;



        String queryStartMillis = String.valueOf(DateTimeUtils.getThisMonthMillis());
        String queryEndMillis = String.valueOf(DateTimeUtils.getNextMonthMillis());

        String[] projectionCat = {SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME};
        categoryCursor = mContext.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                projectionCat,
                null,
                null,
                null);


        for(categoryCursor.moveToFirst();!categoryCursor.isAfterLast();categoryCursor.moveToNext()){

            int totalAmount = 0;
            int percentage = 0;
            budgetCategory = new BudgetCategory();

            int nameCategoryIndex = categoryCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME);
            String nameCategory = categoryCursor.getString(nameCategoryIndex);


            String[] projectionSpent = {SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};
            String selectionSpent = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY  + "=? AND " +
                    SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
            String[] selectionArgsSpent = {nameCategory,queryStartMillis,queryEndMillis};

            mCursor = mContext.getContentResolver().query(SqlContractClass.SpendingsEntryClass.CONTENT_URI,
                    projectionSpent,
                    selectionSpent,
                    selectionArgsSpent,
                    null);

            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
                int amountIndex = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int amount = mCursor.getInt(amountIndex);

                totalAmount = amount + totalAmount;
            }



            percentage = totalAmount/getBudget();
            budgetCategory.setCategoryName(nameCategory);
            budgetCategory.setCategoryPercentage(percentage);

            budgetCategoryList.add(budgetCategory);
        }

        return budgetCategoryList;
    }

    private int getBudget(){
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

            return budget;
        }

        return 0;
    }

}
