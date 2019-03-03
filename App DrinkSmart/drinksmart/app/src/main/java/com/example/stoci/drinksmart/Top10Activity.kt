package com.example.stoci.drinksmart

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.stoci.drinksmart.Adapter.DrinkAdapter
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.Retrofit.IDrinksAPI
import com.example.stoci.drinksmart.Retrofit.IDrinksTop10
import com.example.stoci.drinksmart.Retrofit.RetrofitClientDrinks
import com.example.stoci.drinksmart.Retrofit.RetrofitClientDrinksTop10
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_top10.*

class Top10Activity : AppCompatActivity() {

    internal lateinit var jsonAPI: IDrinksTop10
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top10)


        top10Toolbar.setTitle("Top 10")
        setSupportActionBar(top10Toolbar)

        //Init API
        val retrofit= RetrofitClientDrinksTop10.instance
        jsonAPI=retrofit.create(IDrinksTop10::class.java)

        //View
        recycler_top10.setHasFixedSize(true)
        recycler_top10.layoutManager= LinearLayoutManager(this)
        fetchData()

    }

    private fun fetchData() {
        compositeDisposable.add(jsonAPI.drinkstop10
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{drinkstop10->displayData(drinkstop10)}
        )

    }

    private fun displayData(drinks: List<Drink>?) {
        val adapter = DrinkAdapter(this,drinks!!)
        recycler_top10.adapter=adapter
    }

}
