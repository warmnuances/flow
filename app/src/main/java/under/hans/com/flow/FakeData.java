package under.hans.com.flow;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;

/**
 * Created by Hans on 4/18/2018.
 */

public class FakeData  {

    public static final void insertFakeData(Context context){

        String strFood = context.getResources().getString(R.string.Food_and_beverage);

        List<ContentValues> cvList = new ArrayList<>();
        ContentValues contentValues = new ContentValues();

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Fried Rice");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,550);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1492444800);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"18/4/2017");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Alcohol");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,5000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1483113600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"31/12/2016");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Beef Noodles");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,550);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1483113600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"31/12/2016");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Steak");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,2000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1520092800);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"4/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Ching Chong Kao yu");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,5000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1520092800);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"4/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Ma La Go Rou");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,5000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1520179200);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"5/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Masala Thosai");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,500);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1520697600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"11/3/2018");
        cvList.add(contentValues);


        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Egg Prata");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,500);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1520697600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"11/3/2018");
        cvList.add(contentValues);


        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Nasi Lemak");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,300);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1489248000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"12/3/2017");
        cvList.add(contentValues);


        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Rendang");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,500);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1521302400);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"18/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Pad Thai");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,780);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1521907200);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"25/3/2018");
        cvList.add(contentValues);


        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY,strFood);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Khao Pad Thai");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,18000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1522425600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"31/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY
                ,context.getResources().getString(R.string.Transport));
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_NAME,"Bus Tickets");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,51000);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC,1522425600);
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_DATE,"31/3/2018");
        cvList.add(contentValues);

        for(ContentValues cv: cvList){
            context.getContentResolver().insert(SqlContractClass.SpendingsEntryClass.CONTENT_URI,cv);
        }

        cvList.clear();

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY,"Lottery");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME,"4D TOTO");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT,300000);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,1520697600);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE,"11/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY,"Income");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME,"Pocket Money");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT,50000);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,1520697600);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE,"11/3/2018");
        cvList.add(contentValues);

        contentValues = new ContentValues();
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_CATEGORY,"Insurance");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_NAME,"Prudential");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_AMOUNT,50000);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_TIMESEC,1456675200);
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_ACCOUNT,"Bank Account");
        contentValues.put(SqlContractClass.InflowEntryClass.COLUMN_INFLOW_DATE,"29/2/2016");
        cvList.add(contentValues);

        for(ContentValues cv: cvList){
            context.getContentResolver().insert(SqlContractClass.InflowEntryClass.CONTENT_URI,cv);
        }

    }
}
