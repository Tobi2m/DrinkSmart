package com.example.stoci.drinksmart

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.mancj.materialsearchbar.MaterialSearchBar

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val lv =findViewById(R.id.ListView) as ListView
        val searchbar = findViewById(R.id.searchBar) as MaterialSearchBar

        searchbar.setHint("Search...")
        searchbar.setSpeechMode(true)

        var drinks = arrayOf("Flying Hirsch","Jägermeister","Pina Colada","Bier","Vodka-Redbull","Jacky Cola")

        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,drinks )

        lv.setAdapter(adapter)

        searchbar.addTextChangeListener(object:TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.getFilter().filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        lv.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                Toast.makeText(this@SearchActivity,adapter.getItem(i)!!.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}
