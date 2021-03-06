package am.gsoft.carservice.ui.activity;

import am.gsoft.carservice.R;
import am.gsoft.carservice.ui.activity.main.MainActivity;
import am.gsoft.carservice.util.VersionUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SplashActivity extends BaseActivity {

  private static final String TAG = SplashActivity.class.getSimpleName();

  @Override
  protected int layoutResId() {
    return R.layout.activity_splash;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (VersionUtils.isAfter21()) {
      getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

//    if (checkPlayServices()) {
      // if user isn't authorized then suggest him to authorize
      if (!appSharedHelper.isSavedRememberMe()) {
        startLandingActivity();
      } else {
        startMainActivity();
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//          @Override
//          public void run() {
//            int c = AppDatabase.getInstance(getApplicationContext()).mCarDao().getCount();
//            if (c != 0) {
//
//              Intent intent = getIntent();
//              Bundle extras = intent.getExtras();
//              if (extras != null) {
//                if (extras.containsKey(EXTRA_NOTIFICATION_MESSAGE_CAR_KEY)) {
//                  // extract the extra-data in the Notification
//                  String carKey = extras.getString(EXTRA_NOTIFICATION_MESSAGE_CAR_KEY);
//
////                    int p = CarDataDbHalper.getInstance().getCarPosition(carId);
//                  appSharedHelper.saveSpinnerPositionForCar(carKey);
//                }
//              }
//              startMainActivity();
//            } else {
//              runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                  ToastUtils.shortToast(R.string.msg_no_cars);
//                }
//              });
//              Intent intent = new Intent(SplashActivity.this, CreateNewCarActivity.class);
//              intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//              startActivity(intent);
//              finish();
//              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//          }
//        });

      }

  }

  private void startMainActivity() {
    Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  private void startLandingActivity() {
    Intent intent = new Intent(this, LandingActivity.class);
    startActivity(intent);
    finish();
  }

}