package under.hans.com.flow.AlarmService;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by Hans on 3/9/2018.
 */

public class AlarmManagerInstance {
    private static final String TAG = "AlarmManagerInstance";

    private static AlarmManager sAlarmManager;

    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (sAlarmManager != null) {
            throw new IllegalStateException("Alarm Manager Already Set");
        }
        sAlarmManager = alarmManager;
    }
    /*package*/
    static synchronized AlarmManager getAlarmManager(Context context) {
        if (sAlarmManager == null) {
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
