package under.hans.com.flow.AlarmService;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.Preset.PresetSpendingsFragment;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.NotificationUtils;

/**
 * Created by delaroy on 9/22/17.
 */

public class ReminderAlarmService extends IntentService {

    private static final String TAG = "ReminderAlarmService";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";


    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {

        String lastPathSegment = uri.getLastPathSegment();
        int requestCode = Integer.parseInt(lastPathSegment);

        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, requestCode, action, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();

        if(ACTION_DISMISS_NOTIFICATION.equals(action)){
            NotificationUtils.clearAllNotification(this);

        }else {
            Uri uri = intent.getData();
            NotificationUtils.createPresetNotification(this,uri );
        }

    }


}