package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AstronomyDatabase
import com.udacity.asteroidradar.repository.AstRepo
import retrofit2.HttpException

//worker class that creates updateAsteroid background job
class UpDateAsteroidWork(context:Context, workParams:WorkerParameters):CoroutineWorker(context, workParams) {

    //work unique name
    companion object{
        const val WORK_NAME = "UpdateAsteroid"
    }

    //defines what will be executed
    override suspend fun doWork(): Result {

        val database = AstronomyDatabase.getDatabase(applicationContext)

        val astRepo = AstRepo(database)

        return try {
            astRepo.insertAstToDataBase()
            Result.success()
        }catch (e:HttpException){
            Result.retry()
        }
    }
}