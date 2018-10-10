package am.gsoft.carservice.data.viewmodel;

import am.gsoft.carservice.data.AppRepository;
import am.gsoft.carservice.data.database.entity.Oil;
import android.arch.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;


public class EditOilActivityViewModel extends ViewModel {

  private final AppRepository mRepository;
  private Map<String, String> ph;

  public EditOilActivityViewModel(AppRepository repository) {
    mRepository = repository;
  }

  public void editOil(Oil editedOil) {
    mRepository.editOil(editedOil);
    updatePO(editedOil);
  }

  private void updatePO(Oil editedOil) {
    Map<String, String> map = getPO();
    map.put(editedOil.getBrand(), editedOil.getServiceCompanyId());
    mRepository.saveOilPOMap((HashMap<String, String>) map);
  }


  public HashMap<String, String> getPO() {
    return mRepository.getOilPOMap()==null?new HashMap<>():mRepository.getOilPOMap();
  }


}
