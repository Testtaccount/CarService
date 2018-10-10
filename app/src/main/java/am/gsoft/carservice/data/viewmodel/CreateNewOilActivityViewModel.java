package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.database.entity.Oil;
import android.arch.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;

public class CreateNewOilActivityViewModel extends ViewModel {

  private final AppRepository mRepository;
  private Map<String, String> ph;

  public CreateNewOilActivityViewModel(AppRepository repository) {
    mRepository = repository;
  }

  public void saveOil(Oil newOil) {
    mRepository.saveOil(newOil);
    savePO(newOil);
  }

  private void savePO(Oil newOil) {
    Map<String, String> map = getPO();

    map.put(newOil.getBrand(), newOil.getServiceCompanyId());

    mRepository.saveOilPOMap((HashMap<String, String>) map);
  }


  public HashMap<String, String> getPO() {
    return mRepository.getOilPOMap()==null?new HashMap<>():mRepository.getOilPOMap();
  }


}
