package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AstronomyDatabase
import com.udacity.asteroidradar.database.asDomainAstObj
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.getFormattedDate
import com.udacity.asteroidradar.network.getFormattedDateInCurrentWeek
import com.udacity.asteroidradar.repository.APODRepo
import com.udacity.asteroidradar.repository.AstRepo
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

//class to handle filter
enum class AsteroidFilter (val filterValue:String){ALL("allList"),DAY("listOfDay"),WEEK("listOfWeek")}

class MainViewModel(app:Application) : ViewModel() {

    //getting database instance to access repository
    private val astronomyDatabase = AstronomyDatabase.getDatabase(app)
    private val apodRepo = APODRepo(astronomyDatabase)
    private val astRepo = AstRepo(astronomyDatabase)

    //liveData to observes navigation
    val navigateToItem: LiveData<Asteroid>
    get() = _navigateToItem
    private val _navigateToItem = MutableLiveData<Asteroid>()

    //liveData to observe list changes according filter
    val domainAst:LiveData<List<Asteroid>>
    get() = _domainAst
    private val _domainAst = MutableLiveData<List<Asteroid>>()

    //init block to repository methods initialization
    init {
        viewModelScope.launch {
            apodRepo.insertApodToDatabase()
            astRepo.insertAstToDataBase()
        }
        //init asteroid filtered list with standard value
        filteredList(AsteroidFilter.ALL)
    }

    //apod liveData from repository to binding
    val domainApod = apodRepo.domainApod

    //method to filter list from database
     private fun filteredList(filter:AsteroidFilter){
         viewModelScope.launch {
             val databaseList = astronomyDatabase.astronomyDao.getAst()
             val list: List<Asteroid> = when(filter){
                 AsteroidFilter.DAY -> databaseList.filter { it.closeApproachDate ==  getFormattedDate()}.asDomainAstObj()
                 AsteroidFilter.WEEK ->  databaseList.filter { it.closeApproachDate >= getFormattedDate() && it.closeApproachDate < getFormattedDateInCurrentWeek()}.asDomainAstObj()
                 AsteroidFilter.ALL -> databaseList.asDomainAstObj()
             }
             _domainAst.value = list
         }
    }

    //refresh list according menu item clicked
    fun updateFilter(filter:AsteroidFilter){
        filteredList(filter)
    }

    //set item clicked to be observed
    fun itemDetail(item:Asteroid){
        _navigateToItem.value = item
    }

    //restart item value
    fun navigateComplete(){
        _navigateToItem.value = null
    }


    //viewModel factory to pass application as argument
    class ViewModelFactory(val app:Application): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("ViewModel class unknown")
        }

    }

}