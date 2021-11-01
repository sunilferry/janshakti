package apps.janshakti.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import apps.janshakti.database.daos.AppDao;
import apps.janshakti.database.tables.Attendence;
import apps.janshakti.database.tables.OfficeLocation;

@Database(entities = {OfficeLocation.class, Attendence.class}, exportSchema = false, version = 2)
public abstract class LocationDatabase extends RoomDatabase {
    private static final String DB_NAME="dgme_db";
    private static  LocationDatabase instance;

    public static synchronized LocationDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),LocationDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
        }
        return  instance;
    }

    public abstract AppDao appDao();

}
