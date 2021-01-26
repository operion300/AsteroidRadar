package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AstronomyDatabase
import com.udacity.asteroidradar.database.asDomainObj
import com.udacity.asteroidradar.domain.DomainAPOD
import com.udacity.asteroidradar.network.ApiObj
import com.udacity.asteroidradar.network.asDatabaseAstObj
import com.udacity.asteroidradar.network.asDatabaseObj
import com.udacity.asteroidradar.network.getFormattedDate
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


//astronomy pic of day repository
class APODRepo(private val astronomyDatabase:AstronomyDatabase) {


    //add network data to database
    suspend fun insertApodToDatabase(){
        withContext(Dispatchers.IO){
            val netApod = ApiObj.retrofitService.getPicOfDayAsync().await()

            //check if media type to update when it is not a video
            if (netApod.mediaType != "video"){
                astronomyDatabase.astronomyDao.insertAPOD(netApod.asDatabaseObj())
            }
        }
    }

    //converting database object to domain object to be accessed in app
    val domainApod:LiveData<DomainAPOD> = Transformations.map(astronomyDatabase.astronomyDao.getApod()){
       it?.asDomainObj()
    }


}

//asteroid repository
class AstRepo(private val astronomyDatabase:AstronomyDatabase){
    //insert asteroids to dataBase
    suspend fun insertAstToDataBase(){
        withContext(Dispatchers.IO){
            val statDate = getFormattedDate()
            val endDate = getFormattedDate(Constants.DEFAULT_END_DATE_DAYS)
            val apiResponse = ApiObj.retrofitService.getAsteroidAsync(endDate = endDate,startDate = statDate).await()
            astronomyDatabase.astronomyDao.insertAst(*apiResponse.asDatabaseAstObj())
        }
    }


    //remove asteroids
    fun removeOldAsteroid(){
        astronomyDatabase.astronomyDao.removeAsteroid(getFormattedDate())
    }
}