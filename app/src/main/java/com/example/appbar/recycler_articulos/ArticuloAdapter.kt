package com.example.appbar.recycler_articulos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.R
import com.example.appbar.databinding.RecyclerListaArticulosBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ArticuloAdapter(private val lista: MutableList<DatosArticulo>): RecyclerView.Adapter<ArticuloAdapter.ViewHolder>() {
    lateinit var context : Context
    private lateinit var db : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    class ViewHolder (v: View) : RecyclerView.ViewHolder(v) {
        val binding = RecyclerListaArticulosBinding.bind(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.recycler_lista_articulos, parent, false)
        return ViewHolder(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvNombreArticulo.text = item.nombreArticulo.toString()
        holder.binding.tvPrecioArticulo.text = item.precio.toString() + "€"
        item.urlImagenFamilia?.let {
            Picasso.get().load(it).into(holder.binding.imgArticulo)
        }
    }

//---------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return lista.count()
    }

//---------------------------------------------------------------------------------------------------

    public fun removeAt(position: Int){
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("articulos").child(lista[position].id.toString())
        reference.removeValue()
        notifyItemRemoved(position)
    }

//---------------------------------------------------------------------------------------------------
}