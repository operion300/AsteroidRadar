package com.udacity.asteroidradar.domain

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.ApodUpDateRemoveOldAsteroid
import com.udacity.asteroidradar.work.UpDateAsteroidWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

//override application class
class RadarApplication: Application() {

    //coroutine scope where work will be executed
    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        //this method of timber library configure create the application Logs
        Timber.plant(Timber.DebugTree())

        //initialize works
        initWork()
    }

    //initialize work requests
    private fun initWork()= appScope.launch {
        setupWork()
    }

    //setup work requests
    private fun setupWork(){

        //constraints to execute work
        val constraints  =  Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    setRequiresDeviceIdle(true)
                }
            }.build()

        //define work frequency and set constraints
        val asteroidRequest = PeriodicWorkRequestBuilder<UpDateAsteroidWork>(
            6,
            TimeUnit.DAYS,
        )
            .setConstraints(constraints)
            .build()

        val apodAstRequest = PeriodicWorkRequestBuilder<ApodUpDateRemoveOldAsteroid>(
            1,
            TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        //manage works
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            UpDateAsteroidWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            asteroidRequest
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            ApodUpDateRemoveOldAsteroid.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            apodAstRequest
        )


    }
}
