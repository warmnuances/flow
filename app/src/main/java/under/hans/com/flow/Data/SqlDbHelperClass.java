package under.hans.com.flow.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import under.hans.com.flow.Data.SqlContractClass.InflowEntryClass;
import under.hans.com.flow.Data.SqlContractClass.SpendingsEntryClass;
import under.hans.com.flow.Data.SqlContractClass.UserSettingsClass;
import under.hans.com.flow.Data.SqlContractClass.PresetClass;
import under.hans.com.flow.Data.SqlContractClass.CategoryClass;

/**
 * Created by Hans on 1/5/2018.
 */

public class SqlDbHelperClass extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 23;

    /** ID | NAME | CATEGORY | AMOUNT | DATE | TIMESEC | LABELS | NOTES |
     * Create Inflow table
     * Inflow = funds/income/assets
     */
    final String CREATE_SQL_INFLOW_TABLE = "CREATE TABLE " + InflowEntryClass.TABLE_NAME
            + "("
            + InflowEntryClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InflowEntryClass.COLUMN_INFLOW_NAME + " TEXT NOT NULL, "
            + InflowEntryClass.COLUMN_INFLOW_CATEGORY + " TEXT NOT NULL, "
            + InflowEntryClass.COLUMN_INFLOW_AMOUNT + " INTEGER, "
            + InflowEntryClass.COLUMN_INFLOW_ACCOUNT + " TEXT, "
            + InflowEntryClass.COLUMN_INFLOW_DATE + " TEXT, "
            + InflowEntryClass.COLUMN_INFLOW_TIMESEC + " INTEGER, "
            + InflowEntryClass.COLUMN_INFLOW_LABELS + " TEXT,"
            + InflowEntryClass.COLUMN_INFLOW_NOTES + " TEXT"
            + ");";

    /** ID | NAME | CATEGORY | AMOUNT | DATE | TIMESEC | LABELS | NOTES |
     * Create Items table
     * Items = spendings
     */

    final String CREATE_SQL_SPENDINGS_TABLE = "CREATE TABLE " + SpendingsEntryClass.TABLE_NAME

            + "("
            + SpendingsEntryClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_NAME + " TEXT NOT NULL, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_CATEGORY + " TEXT NOT NULL, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT + " INTEGER, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_ACCOUNT + " TEXT, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_DATE + " TEXT, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " INTEGER, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_LABELS + " TEXT, "
            + SpendingsEntryClass.COLUMN_SPENDINGS_NOTES + " TEXT "
            + ");";


    /** ID | USERNAME | EMAIL | BALANCE |ACCOUNT | LAST UPDATED |
     * Create User Settings table
     *
     */
    final String CREATE_USER_ACCOUNT_SETTINGS_TABLE = "CREATE TABLE " + UserSettingsClass.TABLE_NAME
            + "("
            + UserSettingsClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserSettingsClass.COLUMN_USER_NAME + " TEXT NOT NULL, "
            + UserSettingsClass.COLUMN_USER_EMAIL + " TEXT NOT NULL, "
            + UserSettingsClass.COLUMN_USER_BALANCE + " INTEGER, "
            + UserSettingsClass.COLUMN_USER_ACCOUNT + " TEXT, "
            + UserSettingsClass.COLUMN_USER_BUDGET + " INTEGER, "
            + UserSettingsClass.COLUMN_USER_LAST_UPDATED + " INTEGER "
            + ");";

    /**** [ ID | NAME | CATEGORY | AMOUNT | START DATE | END DATE | TIMESEC START | TIMESEC END
     *       | LABELS | NOTES | INTERVAL | MULTIPLIER | CONSTANT | TYPE | TRIGGER ]
     * Create User Settings table
     *
     */
    final String CREATE_SQL_PRESET_TABLE = "CREATE TABLE " + PresetClass.TABLE_NAME
            + "("
            + PresetClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PresetClass.COLUMN_PRESET_NAME + " TEXT NOT NULL, "
            + PresetClass.COLUMN_PRESET_CATEGORY + " TEXT NOT NULL, "
            + PresetClass.COLUMN_PRESET_AMOUNT + " INTEGER, "
            + PresetClass.COLUMN_PRESET_ACCOUNT + " TEXT, "
            + PresetClass.COLUMN_PRESET_START_DATE + " TEXT, "
            + PresetClass.COLUMN_PRESET_END_DATE + " TEXT, "
            + PresetClass.COLUMN_PRESET_TIMESEC_START + " INTEGER, "
            + PresetClass.COLUMN_PRESET_TIMESEC_END + " INTEGER, "
            + PresetClass.COLUMN_PRESET_LABELS + " TEXT, "
            + PresetClass.COLUMN_PRESET_NOTES + " TEXT, "
            + PresetClass.COLUMN_PRESET_INTERVAL + " INTEGER, "
            + PresetClass.COLUMN_PRESET_MULTIPLIER + " INTEGER, "
            + PresetClass.COLUMN_PRESET_CONSTANT + " TEXT, "
            + PresetClass.COLUMN_PRESET_FLOWTYPE + " TEXT, "
            + PresetClass.COLUMN_PRESET_TIMETRIGGER + " INTEGER, "
            + PresetClass.COLUMN_PRESET_NOTIFY + " INTEGER "
            + ");";

    final String CREATE_SQL_CATEGORIES_TABLE = "CREATE TABLE " + CategoryClass.TABLE_NAME
            + "("
            + CategoryClass._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CategoryClass.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, "
            + CategoryClass.COLUMN_CATEGORY_SUBNAME + " TEXT, "
            + CategoryClass.COLUMN_CATEGORY_BUDGET + " INTEGER, "
            + CategoryClass.COLUMN_CATEGORY_USAGE + " INTEGER, "
            + CategoryClass.COLUMN_CATEGORY_RECENT + " INTEGER, "
            + CategoryClass.COLUMN_CATEGORY_PRIORITY + " INTEGER, "
            + CategoryClass.COLUMN_CATEGORY_PARENT + " TEXT, "
            + CategoryClass.COLUMN_CATEGORY_PATH + " INTEGER "
            + ");";



    public SqlDbHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Spendings
        sqLiteDatabase.execSQL(CREATE_SQL_SPENDINGS_TABLE);

        //inflow
        sqLiteDatabase.execSQL(CREATE_SQL_INFLOW_TABLE);

        //User Account Settings
        sqLiteDatabase.execSQL(CREATE_USER_ACCOUNT_SETTINGS_TABLE);

        //Preset
        sqLiteDatabase.execSQL(CREATE_SQL_PRESET_TABLE);

        //Categories
        sqLiteDatabase.execSQL(CREATE_SQL_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SpendingsEntryClass.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InflowEntryClass.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserSettingsClass.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PresetClass.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryClass.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
