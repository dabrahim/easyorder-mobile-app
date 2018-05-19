package com.esmt.darandroidproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Person ... persons);

    @Update
    public void updatePersons(Person ... persons);

    @Delete
    public void delete(Person ... persons);

    @Query("SELECT * FROM persons")
    public Person[] loadAllPersons();

    @Query("SELECT * FROM persons WHERE age > :minAge")
    public Person[] loadAllPersonOlderThan(int minAge);

    @Query("SELECT * FROM persons WHERE first_name LIKE :search OR last_name LIKE :search")
    public List<Person> findPersonWithName(String search);

    @Query("SELECT first_name, last_name FROM persons")
    public List<NameTuple> loadFullName();

    @Query("SELECT first_name, last_name FROM persons WHERE age IN (:ages)")
    public List<NameTuple> loadFromAge(int[] ages);
}

class NameTuple {
    @ColumnInfo(name = "first_name")
    public String firstName;
    public String last_name;

    //Embedded is also authorized here
}

@Entity(tableName = "persons",
        indices = {@Index("id"), @Index(value = {"firstName", "lastName"}, unique = true)},
        foreignKeys = @ForeignKey(entity = House.class, parentColumns = "house_id", childColumns = "houseId"))
//@Entity(primaryKeys = {"firstName", "lastName"})
public class Person {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "person_id")
    public int personId;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;
    public int age;

    @Embedded
    public City city;

    @Ignore
    public String modou;

    public int houseId;
}

class House {
    @PrimaryKey @ColumnInfo(name = "house_id")
    public int houseId;
    public String geoCoord;
}

class City {
    public String name;
    public String country;
}
