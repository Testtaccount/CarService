package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.Resource;
import am.gsoft.carservice.data.database.entity.Car;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;


public class EditCarActivityViewModel extends ViewModel {

  private final AppRepository mRepository;
  private LiveData<Resource<List<Car>>> mCars;
//  private LiveData<Integer> count;


  public EditCarActivityViewModel(AppRepository repository) {
    mRepository = repository;
   }

  public LiveData<Resource<List<Car>>> getCars() {
    if (mCars == null) {
      mCars = mRepository.loadCars();
    }
    return mCars;
  }

  public LiveData<Car> getCar(String carKey) {
    return mRepository.getCar(carKey);
  }

  public void editCar(Car editedCar) {
    mRepository.editCar(editedCar);
  }

  public void deleteCar(Car currentCar) {
    mRepository.deleteCar(currentCar);
  }

  public LiveData<Integer> getCarsCont() {
    return mRepository.getCarsCount();
  }
}
