package com.example.appbar.clases_auxiliares

import android.content.Context

class Prefs (val c: Context) {
    val FICHERO = "fichero"
    val CONTADORIDSALON = "contadorIdSalon"
    val CONTADORIDFAMILIA = "contadorIdFamilia"
    val CONTADORIDARTICULO = "contadorIdArticulo"
    val CONTADORIDUSER = "contadorIdUser"
    val CONTADORPEDIDOS = "contadorPedidos"
    val NOMBRESALONENLINEAPEDIDO = "nombresalonenlineapedido"
    val storage = c.getSharedPreferences(FICHERO, 0)

//---------------------------------------------------------------------------------------------------

   public fun guardarContadorIdSalon (contadorIdSalon : Int) {
        storage.edit().putInt(CONTADORIDSALON, contadorIdSalon).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun guardarContadorIdFamilia (contadorIdFamilia : Int) {
        storage.edit().putInt(CONTADORIDFAMILIA, contadorIdFamilia).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun guardarContadorIdArticulo(contadorIdArticulo : Int) {
        storage.edit().putInt(CONTADORIDARTICULO, contadorIdArticulo).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun guardarContadorIdUser(contadorIdUser: Int) {
        storage.edit().putInt(CONTADORIDUSER, contadorIdUser).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun guardarContadorPedidos (contadorPedidos : Int) {
        storage.edit().putInt(CONTADORPEDIDOS, contadorPedidos).apply()
    }

//---------------------------------------------------------------------------------------------------

    public fun leerContadorIdSalon (): Int {
        return storage.getInt(CONTADORIDSALON, 0)!!
    }

//---------------------------------------------------------------------------------------------------

    public fun leerContadorIdFamilia (): Int {
        return storage.getInt(CONTADORIDFAMILIA, 0)!!
    }

//---------------------------------------------------------------------------------------------------

    public fun leerContadorIdArticulo (): Int {
        return storage.getInt(CONTADORIDARTICULO, 0)!!
    }

//---------------------------------------------------------------------------------------------------

    public fun leerContadorIdUser (): Int {
        return storage.getInt(CONTADORIDUSER, 0)!!
    }

//---------------------------------------------------------------------------------------------------

    public fun leerContadorPedidos (): Int {
        return storage.getInt(CONTADORPEDIDOS, 0)!!
    }

//---------------------------------------------------------------------------------------------------

    public fun borrarTodo(){
        storage.edit().clear().apply()
    }

//---------------------------------------------------------------------------------------------------

}