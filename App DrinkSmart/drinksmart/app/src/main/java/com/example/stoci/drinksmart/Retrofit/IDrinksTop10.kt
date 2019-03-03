package com.example.stoci.drinksmart.Retrofit

import com.example.stoci.drinksmart.Model.Drink
import io.reactivex.Observable
import retrofit2.http.GET

interface IDrinksTop10 {
    @get:GET("fetch_top10")
    val drinkstop10: Observable<List<Drink>>
}