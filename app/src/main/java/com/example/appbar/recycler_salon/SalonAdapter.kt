package com.example.appbar.recycler_salon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appbar.R
import com.example.appbar.databinding.RecyclerSalonesCreadosBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SalonAdapter(private val lista: MutableList<DatosSalon>): RecyclerView.Adapter<SalonAdapter.ViewHolder>() {

//---------------------------------------------------------------------------------------------------

    lateinit var context : Context
    private lateinit var db : FirebaseDatabase
    private lateinit var reference : DatabaseReference

//---------------------------------------------------------------------------------------------------

    class ViewHolder (v: View) : RecyclerView.ViewHolder(v) {
        val binding = RecyclerSalonesCreadosBinding.bind(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalonAdapter.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.recycler_salones_creados, parent, false)
        return ViewHolder(v)
    }

//---------------------------------------------------------------------------------------------------

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvNombreSalon.text = item.nombreSalon.toString()
    }

//---------------------------------------------------------------------------------------------------

    override fun getItemCount(): Int {
        return lista.count()
    }

//---------------------------------------------------------------------------------------------------

    public fun removeAt(position: Int){
        db = FirebaseDatabase.getInstance("https://aplicacion-proyecto-e79a2-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("salones").child(lista[position].id.toString())
        reference.removeValue()
        notifyItemRemoved(position)
    }

//---------------------------------------------------------------------------------------------------
}



