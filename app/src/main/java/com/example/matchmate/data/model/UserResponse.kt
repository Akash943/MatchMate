package com.example.matchmate.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class UserResponse(
    val results: List<UserProfile>,
    val info: Info,
)

@Entity(tableName = "user_profile")
data class UserProfile @JvmOverloads constructor(
    val gender: String,
    @Embedded(prefix = "name_")
    val name: Name,
    @Ignore
    val location: Location? = null,
    @PrimaryKey
    val email: String,
    @Ignore
    val login: Login? = null,
    @Embedded(prefix = "dob_")
    val dob: Dob,
    @Ignore
    val registered: Registered? = null,
    val phone: String,
    val cell: String,
    @Ignore
    val id: Id? = null,
    @Embedded(prefix = "picture_")
    val picture: Picture,
    val nat: String,
    var status: String = "PENDING", // ACCEPTED/ DECLINED/ PENDING
    var isSynced: Boolean = true
)

data class Name(
    val title: String,
    val first: String,
    val last: String,
)

data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: Coordinates,
    val timezone: Timezone,
)

data class Street(
    val number: Long,
    val name: String,
)

data class Coordinates(
    val latitude: String,
    val longitude: String,
)

data class Timezone(
    val offset: String,
    val description: String,
)

data class Login(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String,
)

data class Dob(
    val date: String = "",
    val age: Int,
)

data class Registered(
    val date: String,
    val age: Long,
)

data class Id(
    val name: String,
    val value: String,
)

data class Picture(
    val large: String = "",
    val medium: String = "",
    val thumbnail: String = "",
)

data class Info(
    val seed: String,
    val results: Long,
    val page: Long,
    val version: String,
)
