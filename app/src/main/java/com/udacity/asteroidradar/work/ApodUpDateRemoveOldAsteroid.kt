package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AstronomyDatabase
import com.udacity.asteroidradar.repository.APODRepo
import com.udacity.asteroidradar.repository.AstRepo
import retrofit2.HttpException

//worker class model to apod update and remove old asteroids
class ApodUpDateRemoveOldAsteroid(context:Context, workParams: WorkerParameters): CoroutineWorker(context, workParams){

    //unique name
    companion object{
        const val WORK_NAME = "ApodUpDateRemoveOldAsteroid"
    }

    //work
    override suspend fun doWork(): Result {
        val databas = AstronomyDatabase.getDatabase(applicationContext)

        val apodRepo = APODRepo(databas)

        val astRepo = AstRepo(databas)

        return try {
            apodRepo.insertApodToDatabase()
            astRepo.removeOldAsteroid()
            Result.success()

        }catch (e:HttpException){
            Result.retry()
        }

    }

}