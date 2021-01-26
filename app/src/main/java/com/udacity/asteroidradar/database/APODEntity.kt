package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.DomainAPOD

//Astronomy pic of day database entity
@Entity
data class APODEntity (
        @PrimaryKey
        val id: Int,
        val title: String,
        val url: String
)

//convert database entity to domain object
fun APODEntity.asDomainObj():DomainAPOD{
    return DomainAPOD(
        title = title,
        url = url
    )
}

