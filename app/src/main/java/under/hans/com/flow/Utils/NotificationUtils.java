package under.hans.com.flow.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import under.hans.com.flow.AlarmService.AlarmScheduler;
import under.hans.com.flow.AlarmService.ReminderAlarmService;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Forms.AddPresetActivity;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;

/**
 * Created by Hans on 3/30/2018.
 */

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";


    private static final int NOTIFICATION_ID = 42;
    private static final String PRESET_NOTIFICATION_CHANNEL_ID = "preset_notification_channel";


    private static final int PRESET_NOTIFICATION_PENDING_INTENT_ID = 100;
    private static final int ACTION_EDIT_PENDING_INTENT_ID = 10;
    private static final int ACTION_DISMISS_PENDING_INTENT_ID = 20;

    private static final String NOTIFICATION_CHANNEL_NAME = "Primary";

    private static Cursor mCursor;

    public static void createPresetNotification(Context context,Uri uri){


        String notifyTitle = "";
        String notifyCategory = "";
        String strId;
        int getEndDate,getInterval,getCurrentDate,shouldNotify = 0,getTimeTrigger,getId;
        int insertTimeTrigger;

        long getFutureTime;



        if(uri != null){
            mCursor = context.getContentResolver().query(uri, null, null, null, null);
        }

        try {
            if (mCursor != null && mCursor.moveToFirst()) {


                getId = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass._ID);
                getCurrentDate = (int)(System.currentTimeMillis()/1000);
                getEndDate = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END);
                getInterval = SqlContractClass.getColumnInt(mCursor,SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL);
                getFutureTime = getCurrentDate + getInterval;
                shouldNotify  = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY);


                strId = String.valueOf(getId);
                String whereClause = SqlContractClass.PresetClass._ID + "=?";
                String[] whereArgs = {strId};

                getTimeTrigger = SqlContractClass.getColumnInt(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER);
                insertTimeTrigger = getTimeTrigger + getInterval;
                ContentValues cv = new ContentValues();
                cv.put(SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER,insertTimeTrigger);
                context.getContentResolver().update(uri,cv,whereClause,whereArgs);

                if(getEndDate > getCurrentDate ||getEndDate == 0){
                    new AlarmScheduler().setAlarm(context,(getFutureTime*1000),uri);
                }

                DatabaseUtils.insertItemOnNotify(context,mCursor);
                notifyTitle = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_NAME);
                notifyCategory = SqlContractClass.getColumnString(mCursor, SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY);

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if(shouldNotify == 1){
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                //@params CHANNEL ID, name ,importance
                NotificationChannel mChannel = new NotificationChannel(
                        PRESET_NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);


                notificationManager.createNotificationChannel(mChannel);
            }


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                    (context, PRESET_NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(notifyCategory)
                    .setContentText(notifyTitle)
                    .setSmallIcon(R.drawable.ic_flow_rectangle)
                    .setContentIntent(contentIntent(context))
                    .setAutoCancel(true)

                    .addAction(editPresetAction(context,uri))
                    .addAction(dismissPresetAction(context));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        }



    }


    private static NotificationCompat.Action editPresetAction(Context context, Uri uri){
        String actionTitle = "EDIT";

        Intent editPresetIntent = new Intent(context, AddPresetActivity.class);
        editPresetIntent.setData(uri);

        PendingIntent editPreset = PendingIntent.getActivity(
                context,
                ACTION_EDIT_PENDING_INTENT_ID,
                editPresetIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action editPresetAction =
                new NotificationCompat.Action(R.drawable.ic_flow_rectangle,
                        actionTitle,
                        editPreset);

        return editPresetAction;
    }

    private static NotificationCompat.Action dismissPresetAction(Context context){
        String actionTitle = "DISMISS";

        Intent dismissNotificationIntent = new Intent(context, ReminderAlarmService.class);
        dismissNotificationIntent.setAction(ReminderAlarmService.ACTION_DISMISS_NOTIFICATION);

        PendingIntent dismissPreset = PendingIntent.getService(
                context,
                ACTION_DISMISS_PENDING_INTENT_ID,
                dismissNotificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action editPresetAction =
                new NotificationCompat.Action(R.drawable.ic_flow_rectangle,
                        actionTitle,
                        dismissPreset);

        return editPresetAction;
    }

    public static void clearAllNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, PresetOverview.class);

        return PendingIntent.getActivity(
                context,
                PRESET_NOTIFICATION_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
