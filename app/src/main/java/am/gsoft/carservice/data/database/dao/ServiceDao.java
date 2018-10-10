package am.gsoft.carservice.data.database.dao;

import am.gsoft.carservice.data.database.entity.Service;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface ServiceDao {

  @Query("SELECT * FROM service WHERE `key`=:key")
  Service get(String key);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  long insert(Service service);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  int update(Service service);

  @Delete
  void delete(Service service);

  @Query("DELETE FROM service WHERE `key`=:key")
  void delete(String key);

  @Query("DELETE FROM service")
  void nukeTable();
}
