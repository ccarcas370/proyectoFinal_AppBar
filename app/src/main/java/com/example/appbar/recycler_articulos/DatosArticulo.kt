package com.example.appbar.recycler_articulos

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class DatosArticulo(val id: Int?=null, val idFamilia: Int?=null, val nombreArticulo: String?=null, val precio: Double?=null, val urlImagenFamilia: Int?=null): Serializable {

}