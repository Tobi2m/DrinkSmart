package com.example.stoci.drinksmart.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.R

class DrinkListAdapter(internal  var context:Context,
                       internal  var drinkList:List<Drink>):RecyclerView.Adapter<DrinkListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.drink_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return drinkList.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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