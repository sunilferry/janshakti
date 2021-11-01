package apps.janshakti.database.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendance")
public class Attendence {
    @PrimaryKey
    private  int id;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "user_id")
    private String user_id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "lat")
    private String lat;
    @ColumnInfo(name = "lng")
    private String lng;

    public Attendence(int id, String type, String user_id, String image, String lat, String lng) {
        this.id = id;
        this.type = type;
        this.user_id = user_id;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
