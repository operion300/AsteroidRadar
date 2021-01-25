package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import retrofit2.http.DELETE


//dao with database access methods
@Dao
interface AstronomyDao{
    @Query("SELECT * FROM apodentity WHERE id = 1")
    fun getApod():LiveData<APODEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAPOD( pic: APODEntity)

    @Query("SELECT * FROM asteroidentity ORDER BY closeApproachDate ASC")
    //fun getAst(): LiveData<List<AsteroidEntity>>
    suspend fun getAst(): List<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAst(vararg ast:AsteroidEntity)

    @Query("DELETE FROM asteroidentity WHERE closeApproachDate < :today")
    fun removeAsteroid(today:String)

}

//database creation
@Database(version = 1,entities = [APODEntity::class, AsteroidEntity::class])
abstract class AstronomyDatabase:RoomDatabase(){
    abstract val astronomyDao: AstronomyDao
    companion object{
        private lateinit var INSTANCE:AstronomyDatabase
        fun getDatabase(ctx:Context):AstronomyDatabase{
           synchronized(AstronomyDatabase::class.java){
               if (!::INSTANCE.isInitialized){
                   INSTANCE = Room.databaseBuilder(
                           ctx.applicationContext,
                           AstronomyDatabase::class.java,
                           "astronomyDB"
                   ).build()
               }
           }
            return INSTANCE
        }
    }
}

