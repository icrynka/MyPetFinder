package com.example.mypetfinder.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mypetfinder.entities.Pet;

import java.util.List;

@Dao
public interface PetDao {

    @Query("SELECT * FROM pets ORDER BY id")
    List<Pet> getAllPets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPet(Pet pet);

}
