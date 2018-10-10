package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AbsentLiveData;
import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.Resource;
import am.gsoft.carservice.data.database.entity.Car;
import am.gsoft.carservice.data.database.entity.Oil;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class LandingActivityViewModel extends ViewModel {

    private final AppRepository mRepository;
    private LiveData<Resource<List<Car>>> mCars;
//    private LiveData<Resource<List<Oil>>> mOils;
//    private final LiveData<List<Oil>> mOils;

    public LandingActivityViewModel(AppRepository repository) {
        mRepository = repository;
//        mCars = mRepository.loadCars();
//        mOils = mRepository.loadOils();
    }

    public LiveData<Resource<List<Car>>> getCars() {
        if (mCars == null) {
            mCars = mRepository.loadCars();
        }
        return mCars;
    }

    public LiveData<Resource<List<Oil>>> getCarOils(String carKey) {
        if (carKey == null){
            return AbsentLiveData.create();
        }else {
//            if (mOils == null){
            return mRepository.loadOils(carKey);
        }
//        return mOils;
    }

}
