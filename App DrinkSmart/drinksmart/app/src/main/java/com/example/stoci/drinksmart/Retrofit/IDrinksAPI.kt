package com.example.stoci.drinksmart.Retrofit

import com.example.stoci.drinksmart.Model.Drink
import io.reactivex.Observable
import retrofit2.http.GET
import java.util.*

interface IDrinksAPI {
    @get:GET("fetch_all_drinks")
    val drinks:Observable<List<Drink>>
}