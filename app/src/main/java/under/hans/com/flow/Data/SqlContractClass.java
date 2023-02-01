package under.hans.com.flow.Data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hans on 1/5/2018.
 */

public class SqlContractClass {



    public static final String AUTHORITY = "under.hans.com.flow";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Spendings Contract Class
     */

    public static final String PATH_SPENDINGS = "Spendings";


    public static final class SpendingsEntryClass implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPENDINGS).build();


        public static final String TABLE_NAME = "spendings";
        public static final String COLUMN_SPENDINGS_NAME= "name";
        public static final String COLUMN_SPENDINGS_CATEGORY = "category";
        public static final String COLUMN_SPENDINGS_AMOUNT = "amount";
        public static final String COLUMN_SPENDINGS_ACCOUNT = "account";
        public static final String COLUMN_SPENDINGS_DATE = "date";
        public static final String COLUMN_SPENDINGS_TIMESEC = "timesec";
        public static final String COLUMN_SPENDINGS_LABELS = "labels";
        public static final String COLUMN_SPENDINGS_NOTES = "notes";
    }

    /**
     * Inflow contract class
     */

    public static final String PATH_INFLOW = "Inflow";

    public static final class InflowEntryClass implements BaseColumns {
        //Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INFLOW).build();

        //Database
        public static final String TABLE_NAME = "inflow";
        public static final String COLUMN_INFLOW_NAME = "name";
        public static final String COLUMN_INFLOW_CATEGORY = "category";
        public static final String COLUMN_INFLOW_AMOUNT = "amount";
        public static final String COLUMN_INFLOW_DATE = "date";
        public static final String COLUMN_INFLOW_ACCOUNT = "account";
        public static final String COLUMN_INFLOW_TIMESEC = "timesec";
        public static final String COLUMN_INFLOW_LABELS = "labels";
        public static final String COLUMN_INFLOW_NOTES = "notes";
    }

    /**
     * User Settings contract class
     */


    public static final String PATH_USER_SETTINGS = "Settings";

    public static final class UserSettingsClass implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_SETTINGS).build();

        public static final String TABLE_NAME = "UserSettings";
        public static final String COLUMN_USER_NAME= "name";
        public static final String COLUMN_USER_EMAIL = "email";
        public static final String COLUMN_USER_BALANCE = "balance";
        public static final String COLUMN_USER_ACCOUNT = "account";
        public static final String COLUMN_USER_BUDGET = "budget";
        public static final String COLUMN_USER_LAST_UPDATED = "last_updated";

    }

    /**
     * Preset Settings Contract Class
     */

    public static final String PATH_PRESET = "Preset";

    public static final class PresetClass implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRESET).build();

        public static final String TABLE_NAME = "preset";
        public static final String COLUMN_PRESET_NAME = "name";
        public static final String COLUMN_PRESET_CATEGORY = "category";
        public static final String COLUMN_PRESET_AMOUNT = "amount";
        public static final String COLUMN_PRESET_START_DATE = "start_date";
        public static final String COLUMN_PRESET_END_DATE = "end_Date";
        public static final String COLUMN_PRESET_ACCOUNT = "account";
        public static final String COLUMN_PRESET_TIMESEC_START= "timesec_start";
        public static final String COLUMN_PRESET_TIMESEC_END= "timesec_end";
        public static final String COLUMN_PRESET_LABELS = "labels";
        public static final String COLUMN_PRESET_NOTES = "notes";
        public static final String COLUMN_PRESET_MULTIPLIER = "multiplier";
        public static final String COLUMN_PRESET_CONSTANT = "constant";
        public static final String COLUMN_PRESET_INTERVAL = "interval";
        public static final String COLUMN_PRESET_FLOWTYPE = "type";
        public static final String COLUMN_PRESET_TIMETRIGGER = "time_trigger";
        public static final String COLUMN_PRESET_NOTIFY = "notify";
        public static final String COLUMN_PRESET_DATE_CREATED = "date_created";
    }

    /**
     * Categories and Labels Contract Class
     */
    public static final String PATH_CATEGORIES = "Categories";

    public static final class CategoryClass implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORIES).build();

        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_CATEGORY_NAME = "name";
        public static final String COLUMN_CATEGORY_SUBNAME = "category";
        public static final String COLUMN_CATEGORY_BUDGET = "budget";
        public static final String COLUMN_CATEGORY_USAGE = "usage";
        public static final String COLUMN_CATEGORY_RECENT = "recent";
        public static final String COLUMN_CATEGORY_PRIORITY = "priority";
        public static final String COLUMN_CATEGORY_PARENT = "parent_category";
        public static final String COLUMN_CATEGORY_PATH = "category_path";
    }



    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName){
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

}
