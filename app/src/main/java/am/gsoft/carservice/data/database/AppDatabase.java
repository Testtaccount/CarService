package am.gsoft.carservice.data.database;

import am.gsoft.carservice.data.database.dao.AppNotificationDao;
import am.gsoft.carservice.data.database.dao.CarDao;
import am.gsoft.carservice.data.database.dao.OilDao;
import am.gsoft.carservice.data.database.dao.ServiceDao;
import am.gsoft.carservice.data.database.dao.UserDao;
import am.gsoft.carservice.data.database.entity.AppNotification;
import am.gsoft.carservice.data.database.entity.Car;
import am.gsoft.carservice.data.database.entity.Oil;
import am.gsoft.carservice.data.database.entity.Service;
import am.gsoft.carservice.data.database.entity.User;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class,Car.class, Oil.class,Service.class,AppNotification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

  public static final String DATABASE_NAME = "carservice.db";

  private static AppDatabase sInstance;
  private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

  public abstract UserDao mUserDao();
  public abstract CarDao mCarDao();
  public abstract OilDao mOilDao();
  public abstract ServiceDao mServiceDao();
  public abstract AppNotificationDao mAppNotificationDao();

  public static AppDatabase getInstance(final Context context) {
    if (sInstance == null) {
      synchronized (AppDatabase.class) {
        if (sInstance == null) {
          sInstance = Room.databaseBuilder(context.getApplicationContext(),
              AppDatabase.class, AppDatabase.DATABASE_NAME)
//              .addCallback(new Callback() {
//                @Override
//                public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                  super.onCreate(db);
//                  AppDatabase database = AppDatabase.getInstance(context);
//                  database.setDatabaseCreated();
//                }
//              })
              .build();
        }
      }
    }
    return sInstance;
  }

  private void updateDatabaseCreated(final Context context) {
    if (context.getDatabasePath(DATABASE_NAME).exists()) {
      setDatabaseCreated();
    }
  }

  private void setDatabaseCreated(){
    mIsDatabaseCreated.postValue(true);
  }

  public LiveData<Boolean> getDatabaseCreated() {
    return mIsDatabaseCreated;
  }

}
