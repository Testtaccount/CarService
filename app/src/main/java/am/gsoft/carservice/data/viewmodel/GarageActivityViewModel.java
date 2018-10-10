package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.Resource;
import am.gsoft.carservice.data.database.entity.Car;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class GarageActivityViewModel extends ViewModel {

    private final AppRepository mRepository;
    private LiveData<Resource<List<Car>>> mCars;


    public GarageActivityViewModel(AppRepository repository) {
        mRepository = repository;

    }

    public LiveData<Resource<List<Car>>> getCars() {
        if (mCars == null) {
            mCars = mRepository.loadCars();
        }
        return mCars;
    }

}
