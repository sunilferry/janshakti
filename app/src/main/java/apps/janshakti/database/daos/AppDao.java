package apps.janshakti.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import apps.janshakti.database.tables.Attendence;
import apps.janshakti.database.tables.OfficeLocation;

@Dao
public interface AppDao {
    @Query("Select * from office_location")
    List<OfficeLocation> getLocationList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(OfficeLocation location);

    @Update
    void updateLocation(OfficeLocation location);

    @Delete
    void deleteLocation(OfficeLocation location);


    @Query("Select * from attendance")
    List<Attendence> getAttendanceList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendance(Attendence attendence);


    @Query("DELETE FROM office_location")
    void deleteAll();
}
