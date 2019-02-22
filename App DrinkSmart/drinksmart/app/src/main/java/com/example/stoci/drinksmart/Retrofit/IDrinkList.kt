package com.example.stoci.drinksmart.Retrofit

import com.example.stoci.drinksmart.Model.Drinks
import io.reactivex.Observable
import retrofit2.http.GET


interface IDrinkList {
    @get:GET("fetch_all_drinks")
    val listDrink:Observable<Drinks>
}