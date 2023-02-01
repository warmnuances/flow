package under.hans.com.flow.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import under.hans.com.flow.AlarmService.AlarmScheduler;
import under.hans.com.flow.Data.SqlContractClass;

/**
 * Created by Hans on 4/27/2018.
 */

public class AlarmBootReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmBootReceiver";


    //setAlarm : context, futureTime, mUri;
    //setRepeatAlarm: context,futureTime, mUri, Interval

    private Cursor mCursor;

    //Variables
    int presetId,presetTrigger,presetTimeSecEnd, presetInterval,presetStart,nowTime;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            nowTime = (int) System.currentTimeMillis()/1000;

            String[] projection = {SqlContractClass.PresetClass._ID,
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER,
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END,
                    SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL,
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START};

            mCursor = context.getContentResolver().query(SqlContractClass.PresetClass.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);

            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){

                int idIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass._ID);
                int triggerIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER);
                int timeSecEndIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END);
                int intervalIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL);
                int startIndex = mCursor.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START);

                presetId = mCursor.getInt(idIndex);
                presetTrigger = mCursor.getInt(triggerIndex);
                presetTimeSecEnd = mCursor.getInt(timeSecEndIndex);
                presetInterval = mCursor.getInt(intervalIndex) * 1000;
                presetStart = mCursor.getInt(startIndex);

                Uri presetUri = ContentUris.withAppendedId(SqlContractClass.PresetClass.CONTENT_URI
                        ,presetId);


                if(presetTimeSecEnd == 0){
                    int futureTime;

                    futureTime = presetInterval - ((nowTime - presetStart)%presetInterval);

                    new AlarmScheduler().setRepeatAlarm(context,futureTime,presetUri,presetInterval);

                }else {

                    int futureTime;

                    futureTime = presetInterval - ((nowTime - presetStart)%presetInterval);

                    if(nowTime < presetTimeSecEnd){
                        new AlarmScheduler().setAlarm(context,futureTime,presetUri);
                    }
                }
            }
        }
    }
}
