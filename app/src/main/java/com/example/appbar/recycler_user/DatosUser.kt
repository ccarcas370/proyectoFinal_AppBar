package com.example.appbar.recycler_user

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class DatosUser (val id: Int?=null, val email: String?=null, val password: String?=null, val rol: Int?=null): Serializable {

}
