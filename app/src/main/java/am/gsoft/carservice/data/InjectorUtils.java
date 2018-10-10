package am.gsoft.carservice.data;

import am.gsoft.carservice.app.AppExecutors;
import am.gsoft.carservice.data.database.AppDatabase;
import am.gsoft.carservice.data.network.ApiFactory;
import am.gsoft.carservice.data.network.AppNetworkService;
import am.gsoft.carservice.data.viewmodel.ViewModelFactory;
import am.gsoft.carservice.firebase.FirebaseApi;
import am.gsoft.carservice.notification.NotificationsController;
import am.gsoft.carservice.notification.NotificationsRepository;
import am.gsoft.carservice.util.helpers.SharedHelper;
import android.content.Context;

public class InjectorUtils {

    public static AppRepository provideRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        AppNetworkService appNetworkService =  ApiFactory.getAppNetworkService();
        FirebaseApi firebaseApi=FirebaseApi.getInstance();
        SharedHelper appSharedHelper=SharedHelper.getInstance();
        return AppRepository.getInstance(database, appNetworkService, executors,firebaseApi,appSharedHelper);
    }

    public static NotificationsRepository provideNotificationRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NotificationsController notificationsController=new NotificationsController(context);
        return NotificationsRepository.getInstance(database, executors,notificationsController);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new ViewModelFactory(repository);
    }
}