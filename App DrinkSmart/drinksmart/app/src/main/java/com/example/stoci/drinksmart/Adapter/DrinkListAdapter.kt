package com.example.stoci.drinksmart.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.R

class DrinkListAdapter(internal  var context:Context,
                       internal  var drinkList:List<Drink>):RecyclerView.Adapter<DrinkListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.drink_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(drinkList[position].bild).into(holder.img_drink)
        holder.txt_drink.text= drinkList[position].bezeichnung
    }

    inner class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        internal var img_drink:ImageView
        internal var txt_drink:TextView
        init {
            img_drink= itemView.findViewById(R.id.drink_image) as ImageView
            txt_drink= itemView.findViewById(R.id.drink_name) as TextView
        }
    }
}