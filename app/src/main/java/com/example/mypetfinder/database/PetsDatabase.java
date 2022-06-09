
package com.example.mypetfinder.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mypetfinder.dao.PetDao;
import com.example.mypetfinder.entities.Pet;

@Database(entities = Pet.class, version = 1, exportSchema = false)
public abstract class PetsDatabase extends RoomDatabase {

    private static PetsDatabase petsDatabase;

    public static synchronized PetsDatabase getPetsDatabase(Context context){
        if (petsDatabase == null){
            petsDatabase = Room.databaseBuilder(
                    context,
                    PetsDatabase.class,
                    "pets_db"
            ).build();
        }
        return petsDatabase;
    }

    public abstract PetDao petDao();
}

