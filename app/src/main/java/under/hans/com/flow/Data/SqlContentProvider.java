package under.hans.com.flow.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import under.hans.com.flow.Data.SqlContractClass.InflowEntryClass;
import under.hans.com.flow.Data.SqlContractClass.SpendingsEntryClass;
import under.hans.com.flow.Data.SqlContractClass.UserSettingsClass;
import under.hans.com.flow.Data.SqlContractClass.PresetClass;
import under.hans.com.flow.Data.SqlContractClass.CategoryClass;

/**
 * Created by Hans on 1/5/2018.
 */

public class SqlContentProvider extends ContentProvider {

    //Db helper instance
    private SqlDbHelperClass mSqlDbHelperClass;

    //Uri Matcher IDs
    public static final int allItems = 100;
    public static final int individualItems = 101;
    public static final int allInflow = 150;
    public static final int individualInflow =151;
    public static final int allUserSettings = 200;
    public static final int individualUserSettings = 201;
    public static final int allPreset = 250;
    public static final int individualPreset = 251;
    public static final int allCategories = 300;
    public static final int individualCategories = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(SqlContractClass.AUTHORITY, SqlContractClass.PATH_SPENDINGS, allItems);
        uriMatcher.addURI(SqlContractClass.AUTHORITY, SqlContractClass.PATH_SPENDINGS + "/#", individualItems);

        uriMatcher.addURI(SqlContractClass.AUTHORITY,SqlContractClass.PATH_INFLOW, allInflow);
        uriMatcher.addURI(SqlContractClass.AUTHORITY, SqlContractClass.PATH_INFLOW + "/#", individualInflow);

        uriMatcher.addURI(SqlContractClass.AUTHORITY, SqlContractClass.PATH_USER_SETTINGS, allUserSettings);
        uriMatcher.addURI(SqlContractClass.AUTHORITY, SqlContractClass.PATH_USER_SETTINGS + "/#" , individualUserSettings);

        uriMatcher.addURI(SqlContractClass.AUTHORITY,SqlContractClass.PATH_PRESET, allPreset);
        uriMatcher.addURI(SqlContractClass.AUTHORITY,SqlContractClass.PATH_PRESET + "/#", individualPreset);

        uriMatcher.addURI(SqlContractClass.AUTHORITY,SqlContractClass.PATH_CATEGORIES, allCategories);
        uriMatcher.addURI(SqlContractClass.AUTHORITY,SqlContractClass.PATH_CATEGORIES + "/#", individualCategories);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mSqlDbHelperClass = new SqlDbHelperClass(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        SQLiteDatabase db;

        switch (match){
            //Items
            case allItems:
                db = mSqlDbHelperClass.getReadableDatabase();
                returnCursor = db.query(SpendingsEntryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case individualItems:
                db = mSqlDbHelperClass.getReadableDatabase();
                selection = SpendingsEntryClass._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(SpendingsEntryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            //Inflow
            case allInflow:
                db = mSqlDbHelperClass.getReadableDatabase();
                returnCursor = db.query(InflowEntryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case individualInflow:
                db = mSqlDbHelperClass.getReadableDatabase();
                selection = InflowEntryClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(InflowEntryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;

            //User Settings
            case allUserSettings:
                db = mSqlDbHelperClass.getReadableDatabase();
                returnCursor = db.query(UserSettingsClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case individualUserSettings:
                db = mSqlDbHelperClass.getReadableDatabase();
                selection = UserSettingsClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(UserSettingsClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case allPreset:
                db = mSqlDbHelperClass.getReadableDatabase();
                returnCursor = db.query(PresetClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case individualPreset:
                db = mSqlDbHelperClass.getReadableDatabase();
                selection = PresetClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(PresetClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case allCategories:
                db = mSqlDbHelperClass.getReadableDatabase();
                returnCursor = db.query(CategoryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case individualCategories:
                db = mSqlDbHelperClass.getReadableDatabase();
                selection = CategoryClass._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnCursor = db.query(CategoryClass.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return returnCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase;
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            //Items
            case allItems:
                sqLiteDatabase = mSqlDbHelperClass.getWritableDatabase();

                long Iid = sqLiteDatabase.insert(SpendingsEntryClass.TABLE_NAME,null,values);
                if(Iid > 0 ){
                    returnUri = ContentUris.withAppendedId(SpendingsEntryClass.CONTENT_URI, Iid);
                }else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row " + uri);
                }
                break;

            //Inflow
            case allInflow:
                sqLiteDatabase = mSqlDbHelperClass.getWritableDatabase();

                long fId = sqLiteDatabase.insert(InflowEntryClass.TABLE_NAME,null,values);
                if(fId > 0){
                    returnUri = ContentUris.withAppendedId(InflowEntryClass.CONTENT_URI,fId);
                }else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row " + uri);
                }
                break;

            //User Settings
            case allUserSettings:
                sqLiteDatabase = mSqlDbHelperClass.getWritableDatabase();

                long Uid = sqLiteDatabase.insert(UserSettingsClass.TABLE_NAME,null,values);

                if(Uid > 0){
                    returnUri = ContentUris.withAppendedId(UserSettingsClass.CONTENT_URI, Uid);
                } else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row " + uri);
                }
                break;

            case allPreset:
                sqLiteDatabase = mSqlDbHelperClass.getWritableDatabase();

                long Pid = sqLiteDatabase.insert(PresetClass.TABLE_NAME,null,values);

                if(Pid > 0){
                    returnUri = ContentUris.withAppendedId(PresetClass.CONTENT_URI, Pid);
                } else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row " + uri);
                }
                break;

            case allCategories:
                sqLiteDatabase = mSqlDbHelperClass.getWritableDatabase();

                long cId = sqLiteDatabase.insert(CategoryClass.TABLE_NAME,null,values);

                if(cId > 0){
                    returnUri = ContentUris.withAppendedId(CategoryClass.CONTENT_URI, cId);
                } else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db;
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch(match){
            case allUserSettings:
                db = mSqlDbHelperClass.getWritableDatabase();
                tasksDeleted = db.delete(UserSettingsClass.TABLE_NAME,null,null);
                break;

            //items
            case individualItems:
                db = mSqlDbHelperClass.getWritableDatabase();
                String iId = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(SpendingsEntryClass.TABLE_NAME, "_id=?" ,new String[]{iId});
                break;

            //inflow
            case individualInflow:
                db = mSqlDbHelperClass.getWritableDatabase();
                String fId = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(InflowEntryClass.TABLE_NAME, "_id=?", new String[]{fId});
                break;

            case individualPreset:
                db = mSqlDbHelperClass.getWritableDatabase();
                String Pid = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(PresetClass.TABLE_NAME, "_id=?", new String[]{Pid});
                break;

            case individualCategories:
                db = mSqlDbHelperClass.getWritableDatabase();
                String cId = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CategoryClass.TABLE_NAME, "_id=?", new String[]{cId});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db;

        int match = sUriMatcher.match(uri);
        int taskUpdated;

        switch (match) {
            //items
            case individualItems:
                db = mSqlDbHelperClass.getWritableDatabase();
                String iId = uri.getPathSegments().get(1);
                taskUpdated = db.update
                        (SpendingsEntryClass.TABLE_NAME, values, "_id=?", new String[]{iId});

                break;

            //inflow
            case individualInflow:
                db = mSqlDbHelperClass.getWritableDatabase();
                String fId = uri.getPathSegments().get(1);
                taskUpdated = db.update
                        (InflowEntryClass.TABLE_NAME, values,"_id=?", new String[]{fId});
                break;


            //User Settings
            case allUserSettings:
                db = mSqlDbHelperClass.getWritableDatabase();
                taskUpdated = db.update(UserSettingsClass.TABLE_NAME,values,selection,selectionArgs);
                break;


            case individualUserSettings:
                db = mSqlDbHelperClass.getWritableDatabase();
                String Uid = uri.getPathSegments().get(1);
                taskUpdated = db.update
                        (UserSettingsClass.TABLE_NAME, values , "_id = ?",new String[]{Uid});

                break;

            case individualPreset:
                db = mSqlDbHelperClass.getWritableDatabase();
                String Pid = uri.getPathSegments().get(1);
                taskUpdated = db.update
                        (PresetClass.TABLE_NAME, values , "_id = ?",new String[]{Pid});
                break;

            case individualCategories:
                db = mSqlDbHelperClass.getWritableDatabase();
                String cId = uri.getPathSegments().get(1);
                taskUpdated = db.update
                        (CategoryClass.TABLE_NAME, values , "_id = ?",new String[]{cId});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (taskUpdated !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return taskUpdated;
    }
}
