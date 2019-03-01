package com.example.stoci.drinksmart

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.stoci.drinksmart.Adapter.DrinkAdapter
import com.example.stoci.drinksmart.Model.Drink
import com.example.stoci.drinksmart.Retrofit.ITop10API
import com.example.stoci.drinksmart.Retrofit.RetrofitClientTop10
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_top10.*

class Top10Activity : AppCompatActivity() {

    internal lateinit var jsonAPI: ITop10API
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top10)

        top10Toolbar.setTitle("Top 10")
        setSupportActionBar(top10Toolbar)

        //Init API
        val retrofit= RetrofitClientTop10.instance
        jsonAPI=retrofit.create(ITop10API::class.java)

        //View
        recycler_top10.setHasFixedSize(true)
        recycler_top10.layoutManager= LinearLayoutManager(this)
        fetchData()

    }

    private fun fetchData() {
        compositeDisposable.add(jsonAPI.top10
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{top10->displayData(top10)}
        )

    }

    private fun displayData(top10: List<Drink>?) {
        val adapter = DrinkAdapter(this,top10!!)
        recycler_top10.adapter=adapter
    }
}

