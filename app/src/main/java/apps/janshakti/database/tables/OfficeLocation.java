package apps.janshakti.database.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "office_location")
public class OfficeLocation {
    @PrimaryKey
    private  int id;
    @ColumnInfo(name = "lat")
    private String lat;
    @ColumnInfo(name = "lng")
    private String lng;

    public OfficeLocation(int id, String lat, String lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    @Ignore
    public OfficeLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
