package com.example.appbar.clases_auxiliares

import android.content.Context

class PrefsLogin (val c: Context) {

    val FICHERO = "ficheroDatos"
    val EMAIL = "email"
    val ROL = "rol"
    val storage  = c.getSharedPreferences(FICHERO, 0)

//---------------------------------------------------------------------------------------------------

    public fun guardarEmail(email: String){
        storage.edit().putString(EMAIL, email).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun leerEmail() : String?{
        return storage.getString(EMAIL, null)
    }

//---------------------------------------------------------------------------------------------------

    public fun guardarRol(rol: Int){
        storage.edit().putInt(ROL, rol).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun leerRol() : Int?{
        return storage.getInt(ROL, 0)!!
    }
//---------------------------------------------------------------------------------------------------

    public fun borrarTodo(){
        storage.edit().clear().apply()
    }

//---------------------------------------------------------------------------------------------------

}