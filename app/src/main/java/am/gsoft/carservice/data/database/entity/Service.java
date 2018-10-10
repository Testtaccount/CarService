package am.gsoft.carservice.data.database.entity;

import am.gsoft.carservice.BuildConfig;
import am.gsoft.carservice.R;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Service {

  @NonNull
  @PrimaryKey
  private String key;
  private String serviceName;
  private String phoneNumber;
  private String address;
  private String imageUil = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.ic_bg_servicing;


  public Service(@NonNull String key, String serviceName, String phoneNumber, String address,String imageUil) {
    this.key = key;
    this.serviceName = serviceName;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.imageUil = imageUil;
  }

  @Ignore
  public Service() {
  }

  @NonNull
  public String getKey() {
    return key;
  }

  public void setKey(@NonNull String key) {
    this.key = key;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
  public String getImageUil() {
    return imageUil;
  }

  public void setImageUil(String imageUil) {
    this.imageUil = imageUil;
  }

  @Override
  public String toString() {
    return "Service{" +
        "key='" + key + '\'' +
        ", serviceName='" + serviceName + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        ", address='" + address + '\'' +
        ", imageUil='" + imageUil + '\'' +
        '}';
  }
}
