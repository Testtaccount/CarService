package am.gsoft.carservice.notification;

import am.gsoft.carservice.data.database.AppDatabase;
import am.gsoft.carservice.data.database.entity.AppNotification;
import android.app.IntentService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import java.util.List;

public class OnBootUpNotificationSchedulerIntentService extends IntentService {


    public OnBootUpNotificationSchedulerIntentService() {
        super("OnBootUpNotificationSchedulerIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            NotificationsController notificationsController=new NotificationsController(getApplicationContext());
            LiveData<List<AppNotification>> liveData = AppDatabase.getInstance(getApplicationContext()).mAppNotificationDao().getAllEnabledNotifications();

            liveData.observeForever(new Observer<List<AppNotification>>() {
                @Override
                public void onChanged(@Nullable List<AppNotification> appNotifications) {
                    liveData.removeObserver(this);
                    for(AppNotification mn: appNotifications){
                        notificationsController.scheduleNotification(mn);
                    }
                }
            });

//            AlarmController controller = new AlarmController(this);
//            // IntentService works in a background thread, so this won't hold us up.
//            AlarmCursor cursor = new AlarmsTableManager(this).queryEnabledAlarms();
//            while (cursor.moveToNext()) {
//                Alarm alarm = cursor.getItem();
//                if (!alarm.isEnabled()) {
//                    throw new IllegalStateException(
//                        "queryEnabledAlarms() returned alarm(s) that aren't enabled");
//                }
//                controller.scheduleNotification(alarm, true);
//            }
//            cursor.close();

        }
    }

}
