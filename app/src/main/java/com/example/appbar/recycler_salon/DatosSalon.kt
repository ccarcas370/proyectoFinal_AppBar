package com.example.appbar.recycler_salon

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class DatosSalon(val id: Int?=null, val nombreSalon: String?=null): Serializable{

}


