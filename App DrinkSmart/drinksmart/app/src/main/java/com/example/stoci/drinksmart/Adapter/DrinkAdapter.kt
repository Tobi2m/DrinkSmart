package com.example.stoci.drinksmart.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.R

class DrinkAdapter(internal var context:Context,internal var drinkList:List<Drink>): RecyclerView.Adapter<DrinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.drink_layout,parent,false)
        return DrinkViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.txt_drink_name.text=drinkList[position].Bezeichnung.toString()


    }
}