package com.example.appbar.recycler_familias

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class DatosFamilia(val id: Int?=null, val nombreFamilia: String?=null, val urlImagenFamilia: Int?=null): Serializable {

}
