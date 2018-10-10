package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AbsentLiveData;
import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.Resource;
import am.gsoft.carservice.data.database.entity.Car;
import am.gsoft.carservice.data.database.entity.Oil;
import am.gsoft.carservice.data.database.entity.User;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private final AppRepository mRepository;
    private LiveData<Resource<List<Car>>> mCars;
    private LiveData<Resource<List<User>>> mUsers;
    private LiveData<List<User>> mUsersList;



    public MainActivityViewModel(AppRepository repository) {
        mRepository = repository;
        mRepository.loadUsers();
//        mCars = mRepository.loadCars();
//        mOils = mRepository.loadOils();
    }

//    public void select(List<User> item) {
//        selected.setValue(item);
//    }
//
//    public LiveData<List<User>> getSelected() {
//        return selected;
//    }

//    private  LiveData<Resource<List<Oil>>> mOils;
//    private final LiveData<List<Oil>> mOils;


    public LiveData<Resource<List<Car>>> getCars() {
        if (mCars == null) {
            mCars = mRepository.loadCars();
        }
        return mCars;
    }

    public LiveData<List<Oil>> getCarOils(String carKey) {
        if (carKey == null) {
            return AbsentLiveData.create();
        } else {
//            if (mOils == null){
            return mRepository.getOils(carKey);
        }
//        return mOils;
    }

    public LiveData<List<User>> getUsers() {
        if (mUsersList == null) {
            mUsersList = mRepository.getUsers();
        }
        return mUsersList;
    }



}
