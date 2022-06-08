package com.example.appbar.recycler_familias

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.R
import com.example.appbar.databinding.RecyclerListaFamiliasBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class FamiliaAdapter (private val lista: MutableList<DatosFamilia>): RecyclerView.Adapter<FamiliaAdapter.ViewHolder>() {

//---------------------------------------------------------------------------------------------------

    lateinit var context : Context
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference
//---------------------------------------------------------------------------------------------------

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = RecyclerListaFamiliasBinding.bind(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.recycler_lista_familias, parent, false)
        return ViewHolder(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvNombreFamilia.text = item.nombreFamilia.toString()
        item.urlImagenFamilia?.let {
            Picasso.get().load(it).into(holder.binding.imgFamilia)
        }
    }

//---------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return lista.count()
    }

//---------------------------------------------------------------------------------------------------

    public fun removeAt(position: Int){
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("familias").child(lista[position].id.toString())
        reference.removeValue()
        notifyItemRemoved(position)
    }

//---------------------------------------------------------------------------------------------------

    public fun cogerId(position: Int): Int{
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("familias").child(lista[position].id.toString())
        var idFamilia = lista[position].id
        if (idFamilia != null) {
            return idFamilia.toInt()
        }
        return -100
    }

//---------------------------------------------------------------------------------------------------

}