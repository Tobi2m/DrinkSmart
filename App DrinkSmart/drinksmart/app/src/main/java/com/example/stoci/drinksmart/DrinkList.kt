package com.example.stoci.drinksmart


import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import com.example.stoci.drinksmart.Adapter.DrinkListAdapter
import com.example.stoci.drinksmart.Common.Common
import com.example.stoci.drinksmart.Common.ItemOffsetDecoration
import com.example.stoci.drinksmart.Retrofit.IDrinkList
import com.example.stoci.drinksmart.Retrofit.RetrofitClientDrinks
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_drink_list.*


class DrinkList : Fragment() {

    internal var compositeDisposable = CompositeDisposable()
    internal var iDrinkList:IDrinkList

    internal lateinit var  recycler_view:RecyclerView

    init {
        val retrofit = RetrofitClientDrinks.instance
        iDrinkList = retrofit.create(IDrinkList::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_drink_list, container, false)


        recycler_view=itemView.findViewById(R.id.drink_recyclerview) as RecyclerView
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=GridLayoutManager(activity,1)
        val itemDecoration= ItemOffsetDecoration(activity!!,R.dimen.spacing)
        recycler_view.addItemDecoration(itemDecoration)

        fetchData()

        return itemView
    }

    private fun fetchData() {
        compositeDisposable.add(iDrinkList.listDrink
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ drinks ->
                    Common.drinkList=drinks.drink!!
                    val adapter = DrinkListAdapter(activity!!,Common.drinkList)

                    recycler_view.adapter = adapter
                }
        )
    }


}
