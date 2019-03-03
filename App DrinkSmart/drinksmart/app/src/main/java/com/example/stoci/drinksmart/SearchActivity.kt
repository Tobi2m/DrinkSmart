package com.example.stoci.drinksmart

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.stoci.drinksmart.Adapter.DrinkAdapter
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.Retrofit.IDrinksAPI
import com.example.stoci.drinksmart.Retrofit.RetrofitClient
import com.example.stoci.drinksmart.Retrofit.RetrofitClientDrinks
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    internal lateinit var jsonAPI: IDrinksAPI
<<<<<<< HEAD
    var compositeDisposable: CompositeDisposable= CompositeDisposable()
=======
     var compositeDisposable: CompositeDisposable= CompositeDisposable()
>>>>>>> 1bf9eb24fc69419d3f16ac1848e4cbe2d6a01544

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchToolbar.setTitle("Drinks")
        setSupportActionBar(searchToolbar)

        //Init API
        val retrofit= RetrofitClientDrinks.instance
        jsonAPI=retrofit.create(IDrinksAPI::class.java)

        //View
        recycler_search.setHasFixedSize(true)
        recycler_search.layoutManager= LinearLayoutManager(this)
        fetchData()

    }

    private fun fetchData() {
        compositeDisposable.add(jsonAPI.drinks
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{drinks->displayData(drinks)}
        )

    }

    private fun displayData(drinks: List<Drink>?) {
        val adapter = DrinkAdapter(this,drinks!!)
        recycler_search.adapter=adapter
    }
}
