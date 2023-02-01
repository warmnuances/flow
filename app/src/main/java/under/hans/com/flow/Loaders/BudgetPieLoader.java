package under.hans.com.flow.Loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;

/**
 * Created by Hans on 4/22/2018.
 */

public class BudgetPieLoader extends AsyncTaskLoader<List<PieEntry>> {
    private static final String TAG = "BudgetPieLoader";

    private Context mContext;

    public BudgetPieLoader(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public List<PieEntry> loadInBackground() {

        List<PieEntry> yValuesData = new ArrayList<>();
        Cursor mCursor;

        //Cursor
        String[] projection = {
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,
                SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET};

        mCursor = mContext.getContentResolver().query(SqlContractClass.CategoryClass.CONTENT_URI,
                projection,
                null,
                null,
                null);

        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
            int nameIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME);
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.CategoryClass.COLUMN_CATEGORY_BUDGET);

            String name = mCursor.getString(nameIndex);
            int budget = mCursor.getInt(budgetIndex);

            if(budget > 0){
                yValuesData.add(new PieEntry(budget, name));
            }
        }

        return yValuesData;

    }


}
