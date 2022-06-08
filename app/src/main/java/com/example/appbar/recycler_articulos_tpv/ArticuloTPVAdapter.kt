package com.example.appbar.recycler_articulos_tpv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.R
import com.example.appbar.databinding.RecyclerListaArticulosTpvBinding
import com.example.appbar.recycler_articulos.DatosArticulo
import com.squareup.picasso.Picasso

class ArticuloTPVAdapter (private val lista: MutableList<DatosArticulo>): RecyclerView.Adapter<ArticuloTPVAdapter.ViewHolder>() {
    lateinit var context: Context

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val binding = RecyclerListaArticulosTpvBinding.bind(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.recycler_lista_articulos_tpv, parent, false)
        return ViewHolder(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvNombreArticuloTpv.text = item.nombreArticulo.toString()
        holder.binding.tvPrecioArticuloTpv.text = item.precio.toString() + "€"
        item.urlImagenFamilia?.let {
            Picasso.get().load(it).into(holder.binding.imgArticuloTpv0)
        }
    }

//---------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return lista.count()
    }

//---------------------------------------------------------------------------------------------------

}