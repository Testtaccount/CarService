package am.gsoft.carservice.ui.activity;

import static am.gsoft.carservice.util.manager.ShortcutManager.addPinnedShortcuts;
import static am.gsoft.carservice.util.manager.ShortcutManager.addShortcutIcon;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import am.gsoft.carservice.R;
import am.gsoft.carservice.app.App;
import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.InjectorUtils;
import am.gsoft.carservice.data.Resource;
import am.gsoft.carservice.data.ResultListener;
import am.gsoft.carservice.data.database.AppDatabase;
import am.gsoft.carservice.data.database.entity.Car;
import am.gsoft.carservice.data.database.entity.Oil;
import am.gsoft.carservice.data.database.entity.Service;
import am.gsoft.carservice.data.viewmodel.LandingActivityViewModel;
import am.gsoft.carservice.data.viewmodel.ViewModelFactory;
import am.gsoft.carservice.firebase.FirebaseAuthHelper;
import am.gsoft.carservice.notification.NotificationsRepository;
import am.gsoft.carservice.ui.activity.main.MainActivity;
import am.gsoft.carservice.util.Logger;
import am.gsoft.carservice.util.ToastUtils;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class LandingActivity extends BaseActivity implements View.OnClickListener {

  private static String TAG = LandingActivity.class.getSimpleName();

  private static final int PERMISSION_RECEIVE_SMS = 97;
  private static final int REQUEST_READ_PHONE_STATE = 7788;

  private AppRepository repository;
  private NotificationsRepository notificationsRepository;

  private FrameLayout phoneNumberConnectButton;
  private TextView buttonTextTv;
  private ProgressBar buttonProgBar;
  private ProgressBar progBar;
  private View revealV;
  private TextView appVersionTextView;

  private FirebaseAuthCallback firebaseAuthCallback;
//  private GetUsersCars mGetUserCars;
//  private GetCarOils mGetCarOils;
  private LandingActivityViewModel mViewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).getBoolean("IS_ICON_CREATED", false)) {
      getSharedPreferences("APP_PREFERENCE", Activity.MODE_PRIVATE).edit().putBoolean("IS_ICON_CREATED", true).apply();

      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        addPinnedShortcuts(getApplicationContext());
      } else {
        addShortcutIcon(getApplicationContext());
      }
    }

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      Window window = getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(getResources().getColor(R.color.black));
    }

    ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(LandingActivityViewModel.class);

    findViews();
    initFields();
    setListeners();

  }

  @Override
  protected int layoutResId() {
    return R.layout.activity_landing;
  }

  private void findViews() {
    phoneNumberConnectButton = (FrameLayout) findViewById(R.id.button);
    buttonTextTv = (TextView) findViewById(R.id.tv_button_txt);
    buttonProgBar = (ProgressBar) findViewById(R.id.progress_bar_button);
    progBar = (ProgressBar) findViewById(R.id.progress);
    revealV = (View) findViewById(R.id.reveal);
//    appVersionTextView = (TextView) findViewById(R.id.app_version_textview);
  }

  private void initFields() {
    repository = InjectorUtils.provideRepository(getApplicationContext());
    notificationsRepository = InjectorUtils.provideNotificationRepository(getApplicationContext());

//    mGetUserCars = new GetUsersCars();
//    mGetCarOils = new GetCarOils();
    firebaseAuthCallback = new LandingActivity.FirebaseAuthCallback();
  }

  private void setListeners() {
    phoneNumberConnectButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button:
        if (checkNetworkAvailableWithError()) {
//          if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
//              != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_RECEIVE_SMS);
//          } else {
//            startLoginByPhone();
          load();
//          }
        }
        break;
    }
  }

  public void load() {
    animateButtonWidth();

    fadeOutTextAndShowProgressDialog();

    nextAction();
  }

  private void animateButtonWidth() {
    ValueAnimator anim = ValueAnimator
        .ofInt(phoneNumberConnectButton.getMeasuredWidth(), getFabWidth());
    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int val = (Integer) valueAnimator.getAnimatedValue();
        LayoutParams layoutParams = phoneNumberConnectButton.getLayoutParams();
        layoutParams.width = val;
        phoneNumberConnectButton.requestLayout();
      }
    });
    anim.setDuration(250);
    anim.start();
    phoneNumberConnectButton.setEnabled(false);
  }

  private void fadeOutTextAndShowProgressDialog() {
    buttonTextTv.animate().alpha(0f)
        .setDuration(250)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            showProgressDialog();
          }
        })
        .start();
  }

  private void nextAction() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//          revealButton();
//        }
//
//        fadeOutProgressDialog();

        delayedStartNextActivity();
      }
    }, 2000);
  }

  @RequiresApi(api = VERSION_CODES.LOLLIPOP)
  private void revealButton() {
    phoneNumberConnectButton.setElevation(0f);

    revealV.setVisibility(VISIBLE);
    progBar.setVisibility(VISIBLE);

    int cx = revealV.getWidth();
    int cy = revealV.getHeight();

    int x = (int) (getFabWidth() / 2 + phoneNumberConnectButton.getX());
    int y = (int) (getFabWidth() / 2 + phoneNumberConnectButton.getY());

    float finalRadius = Math.max(cx, cy) * 1.2f;

    Animator reveal = null;
    reveal = ViewAnimationUtils.createCircularReveal(revealV, x, y, getFabWidth(), finalRadius);
    reveal.setDuration(350);

    reveal.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
//        reset(animation);
//                finish();
      }

      private void reset(Animator animation) {
        super.onAnimationEnd(animation);
        phoneNumberConnectButton.setEnabled(true);
        revealV.setVisibility(INVISIBLE);
        progBar.setVisibility(INVISIBLE);
        buttonTextTv.setVisibility(VISIBLE);
        buttonTextTv.setAlpha(1f);
        phoneNumberConnectButton.setElevation(4f);
        LayoutParams layoutParams = phoneNumberConnectButton.getLayoutParams();
        layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
        phoneNumberConnectButton.requestLayout();
      }
    });

    reveal.start();
    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));

  }

  private void fadeOutProgressDialog() {
    //hide
    buttonProgBar.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);

      }
    }).start();
  }

  private void delayedStartNextActivity() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
//        startActivity(new Intent(LandingActivity.this, MainActivity.class));

        startLoginByPhone();

      }
    }, 200);
  }

  private void showProgressDialog() {
    //show
    buttonProgBar.setAlpha(1f);
    buttonProgBar.getIndeterminateDrawable()
        .setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
    buttonProgBar.setVisibility(VISIBLE);
  }

  private int getFabWidth() {
    return (int) getResources().getDimension(R.dimen.fab_size);
  }

  protected void startLoginByPhone() {

    if (App.getInstance().isSimulator) {
      onReceiveFirebaseAuthResult(-1, new Intent(Intent.ACTION_ALL_APPS));
    } else {
      new FirebaseAuthHelper().loginByPhone(LandingActivity.this);
    }

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Log.d(TAG, "onActivityResult, result code = " + requestCode);
    if (requestCode == FirebaseAuthHelper.RC_SIGN_IN) {
      onReceiveFirebaseAuthResult(resultCode, data);
    }

  }

  private void onReceiveFirebaseAuthResult(int resultCode, Intent data) {
    IdpResponse response = IdpResponse.fromResultIntent(data);

    // Successfully signed in
    if (resultCode == RESULT_OK) {
      FirebaseAuthHelper.getIdTokenForCurrentUser(firebaseAuthCallback);
      return;
    } else {
      //Sign in failed
      if (response == null) {
        // User pressed back button
        fadeOutProgressDialog();
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
          revealV.setVisibility(INVISIBLE);
          progBar.setVisibility(INVISIBLE);
          phoneNumberConnectButton.setElevation(4f);
        }
        phoneNumberConnectButton.setEnabled(true);
        buttonTextTv.setVisibility(VISIBLE);
        buttonTextTv.setAlpha(1f);
        LayoutParams layoutParams = phoneNumberConnectButton.getLayoutParams();
        layoutParams.width = (int) (getResources().getDisplayMetrics().density * 300);
        phoneNumberConnectButton.requestLayout();
        Log.i(TAG, "BACK button pressed");
        return;
      }

      if (response.getErrorCode() == ErrorCodes.NO_NETWORK
          || response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
        ToastUtils.shortToast(R.string.dlg_internet_connection_error);
        return;
      }
    }
  }

  private Service createNewService() {
    Service service = new Service();
    if (App.getInstance().isSimulator) {
      String phoneNumber = "+37499266857";
      ;//firebaseUser.getPhoneNumber(); //"+37477939733";  //firebaseUser.getPhoneNumber()    //Phone number

//      phoneNumber = phoneNumber.replace("+", "");
//      phoneNumber = phoneNumber.replace(" ", "");

      // FirebaseDbUtil.getmFirebaseDb().getReference(USERS).push().getKey();

      service.setKey(
          "oyTyrPcSmMeYdIAkyU1gOG8eAEe2");//(F5gYIqvaHJDaklUr3I56H9H15t5)//setKey(firebaseUser.getUid());//setKey("YwCS7MYdvwR2eXmCihCxcWjb2Xm1");//setKey(firebaseUser.getUid());
//      service.setId(IdGenerator.getId()); // TODO increment id
      service.setServiceName("NAME");
      service.setAddress("ADDRESS");
      service.setPhoneNumber(phoneNumber);//setPhoneNumber(firebaseUser.getPhoneNumber());
//        service.setUserCars(new ArrayList<Car>());

      return service;
    } else {
      FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      String phoneNumber = firebaseUser
          .getPhoneNumber(); //"+37477939733";  //firebaseUser.getPhoneNumber()    //Phone number
//      phoneNumber = phoneNumber.replace("+", "");
//      phoneNumber = phoneNumber.replace(" ", "");

      // FirebaseDbUtil.getmFirebaseDb().getReference(USERS).push().getKey();
      service.setKey(firebaseUser.getUid());//setKey("YwCS7MYdvwR2eXmCihCxcWjb2Xm1");//setKey(firebaseUser.getUid());
      service.setServiceName("NAME");
      service.setAddress("ADDRESS");
      service.setPhoneNumber(phoneNumber);//setPhoneNumber(firebaseUser.getPhoneNumber());
//       service.setUserCars(new ArrayList<Car>());

      return service;
    }

  }

  private void performLoginSuccessAction() {
//    repository.getCars(this,mGetUserCars);

    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      revealButton();
      fadeOutProgressDialog();
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          Intent intent = new Intent(LandingActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }, 200);
    } else {
      fadeOutProgressDialog();
      // add reset here if needed !!!
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          Intent intent = new Intent(LandingActivity.this, MainActivity.class);
          startActivity(intent);
          finish();
        }
      }, 200);
    }

//    mViewModel.getCars().observe(this, new Observer<Resource<List<Car>>>() {
//      @Override
//      public void onChanged(@Nullable Resource<List<Car>> listResource) {
//        List<Car> cars = null;
//        if (listResource != null) {
//          switch (listResource.status) {
//            case LOADING:
////                  ToastUtils.shortToast("LOADING !!");
//              break;
//            case SUCCESS:
//              cars = listResource.data;
//              if (cars != null && cars.size() != 0) {
//                loadCarsOils(cars);
//
//                //    runOnUiThread(new Runnable() {
////      @Override
////      public void run() {
//                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//                  revealButton();
//                  fadeOutProgressDialog();
//                  new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      Intent intent = new Intent(LandingActivity.this, MainActivity.class);
//                      startActivity(intent);
//                      finish();
//                    }
//                  }, 200);
//                } else {
//                  fadeOutProgressDialog();
//                  // add reset here if needed !!!
//                  new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      Intent intent = new Intent(LandingActivity.this, MainActivity.class);
//                      startActivity(intent);
//                      finish();
//                    }
//                  }, 200);
//                }
////      }
////    });
//
//              } else {
//
//                ToastUtils.shortToast(R.string.msg_no_cars);
////            Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
////            intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
////            startActivity(intent);
////            finish();
////            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//                  revealButton();
//                  fadeOutProgressDialog();
//                  new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
//                      intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//                      startActivity(intent);
//                      finish();
//                    }
//                  }, 200);
//                } else {
//                  fadeOutProgressDialog();
//                  // add reset here if needed !!!
//                  new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                      Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
//                      intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//                      startActivity(intent);
//                      finish();
//                    }
//                  }, 200);
//                }
//              }
//              // send analytics data
////        GoogleAnalyticsHelper.pushAnalyticsData(this, user, "User Sign In");
////        FlurryAnalyticsHelper.pushAnalyticsData(this);
//              break;
//            case ERROR:
//              ToastUtils.shortToast("ERROR !!");
//              break;
//          }
//        }
//
//      }
//    });

  }

  private void loadCarsOils(List<Car> cars) {
    if (cars != null) {

      for (Car c : cars) {
        //foroilcheck
        AppDatabase.getInstance(getApplicationContext()).mOilDao()
            .getAll().observeForever(new Observer<List<Oil>>() {
          @Override
          public void onChanged(@Nullable List<Oil> oils) {
            if (oils!=null) {
//              ToastUtils.shortToast("OILS: " + oils.size());
              Logger.d("loadFromDb","OILS SIZE: "+oils.size());
            }

          }
        });
//        repository.getOils(c, this, new ResultListener<List<Oil>>() {
//          @Override
//          public void onLoad(List<Oil> oils) {
//            if (oils != null && oils.size() > 0) {
//              Oil mUser = oils.get(oils.size() - 1);
//              notificationsRepository.setMonthlyNotification(c,null,mUser);
//              Logger.d("mUser", mUser.toString());
//            }
//          }
//
//          @Override
//          public void onFail(String e) {
//            ToastUtils.shortToast("onFail !!");
//          }
//        });


        mViewModel.getCarOils(c.getKey()).observeForever( new Observer<Resource<List<Oil>>>() {
          @Override
          public void onChanged(@Nullable Resource<List<Oil>> listResource) {
            //        mOilLiveData = appSharedHelper.getSaveOilListByCarKey(currentCar.getKey());
            if (listResource != null) {
              switch (listResource.status) {
                case LOADING:
//                  ToastUtils.shortToast("LOADING !!");
                  break;
                case SUCCESS:
                  mViewModel.getCarOils(c.getKey()).removeObserver(this::onChanged);
                  List<Oil> oils= listResource.data;
                  if (oils != null && oils.size() > 0) {
                    Oil oil = oils.get(oils.size() - 1);
                    if(oil.getCarKey().equals(c.getKey())) {
                      notificationsRepository.setMonthlyNotification(null,oil);
                      Logger.d("loadFromDb"," N: "+c.toString() +" : "+ oil.toString());
                    }
                    Logger.d("mUser", oil.toString());
                    return;
                  }

                   break;
                case ERROR:
                  ToastUtils.shortToast("ERROR !!");
                  break;
              }
            }
          }
        });



      }
    }
  }

  private class FirebaseAuthCallback implements FirebaseAuthHelper.RequestFirebaseIdTokenCallback {

    @Override
    public void onSuccess(String authToken) {
      Log.d(TAG, "FirebaseAuthCallback onSuccess()");

      showProgressDialog();

      repository.createService(createNewService(), new ResultListener<Service>() {
        @Override
        public void onLoad(Service service) {
          appSharedHelper.saveAuthToken(authToken);
          appSharedHelper.saveFirstAuth(false);
          appSharedHelper.saveSavedRememberMe(true);

          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              performLoginSuccessAction();
            }
          });

        }

        @Override
        public void onFail(String e) {
          ToastUtils.shortToast("Fail " + e);

        }
      });

    }

    @Override
    public void onError(Exception e) {
      Log.d(TAG, "FirebaseAuthCallback onError()" + e);
    }
  }

//  public class GetUsersCars implements ResultListener<List<Car>> {
//
//    @Override
//    public void onLoad(List<Car> cars) {
//      runOnUiThread(new Runnable() {
//        @Override
//        public void run() {
//          if (cars != null && cars.size() != 0) {
//            loadCarsOils(cars);
//            //    runOnUiThread(new Runnable() {
////      @Override
////      public void run() {
//            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//              revealButton();
//              fadeOutProgressDialog();
//              new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                  Intent intent = new Intent(LandingActivity.this, MainActivity.class);
//                  startActivity(intent);
//                  finish();
//                }
//              }, 200);
//            } else {
//              fadeOutProgressDialog();
//              // add reset here if needed !!!
//              new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                  Intent intent = new Intent(LandingActivity.this, MainActivity.class);
//                  startActivity(intent);
//                  finish();
//                }
//              }, 200);
//            }
////      }
////    });
//
//          } else {
//
//            ToastUtils.shortToast(R.string.msg_no_cars);
////            Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
////            intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
////            startActivity(intent);
////            finish();
////            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//              revealButton();
//              fadeOutProgressDialog();
//              new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                  Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
//                  intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//                  startActivity(intent);
//                  finish();
//                }
//              }, 200);
//            } else {
//              fadeOutProgressDialog();
//              // add reset here if needed !!!
//              new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                  Intent intent = new Intent(LandingActivity.this, CreateNewCarActivity.class);
//                  intent.setAction(ACTION_MAIN_ACTIVITY_INTENT);
//                  startActivity(intent);
//                  finish();
//                }
//              }, 200);
//            }
//          }
//        }
//      });
//
//    }
//
//    @Override
//    public void onFail(String e) {
//      ToastUtils.shortToast(R.string.msg_something_wrong);
//      System.out.println(e);
//    }
//  }
//
//  public class GetCarOils implements ResultListener<List<Oil>> {
//
//    @Override
//    public void onLoad(List<Oil> oils) {
//      System.out.println(oils);
//    }
//
//    @Override
//    public void onFail(String e) {
//      System.out.println(e);
//    }
//  }

}