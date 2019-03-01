package com.example.stoci.drinksmart.Retrofit

import com.example.stoci.drinksmart.Model.Drink
import io.reactivex.Observable
import retrofit2.http.GET

interface ITop10API {
    @get:GET("fetch_top10")
    val top10: Observable<List<Drink>>
}