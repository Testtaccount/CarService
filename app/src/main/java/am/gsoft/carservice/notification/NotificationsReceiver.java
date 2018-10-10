package am.gsoft.carservice.notification;


import am.gsoft.carservice.util.Constant;
import am.gsoft.carservice.util.Constant.Action;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationsReceiver extends BroadcastReceiver {

  public static final String TAG = "NotificationsReceiver";

  @Override
  public void onReceive(final Context context, final Intent intent) {
    Bundle extras = intent.getExtras();
    if(extras != null){
      if(extras.containsKey(Constant.Extra.EXTRA_APP_NOTIFICATION_ID)){
        // extract the extra-data in the Notification
        int id = intent.getIntExtra(Constant.Extra.EXTRA_APP_NOTIFICATION_ID,-1);

        switch (intent.getAction()){
          case Action.ACTION_SEND_MONTH_NOTIFICATION:
            NotificationsIntentService.startSendMonthNotification(context,id);
            break;
            case Action.ACTION_CANCEL_NOTIFICATION:
            NotificationsIntentService.startCencelMonthNotification(context,id);
            break;
        }
      }
    }


  }

}
