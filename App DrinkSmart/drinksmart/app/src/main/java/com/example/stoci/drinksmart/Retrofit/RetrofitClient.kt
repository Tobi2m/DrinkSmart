package com.example.stoci.drinksmart.Retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private var ourInstance: Retrofit?= null
    val instance:Retrofit
        get(){
            if(ourInstance == null)
                ourInstance = Retrofit.Builder()
                        .baseUrl("http://guarded-inlet-38884.herokuapp.com/")
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build()
                return ourInstance!!

        }

}

object RetrofitClientDrinks {
<<<<<<< HEAD
    private var ourInstance: Retrofit? = null
    val instance: Retrofit
        get() {
            if (ourInstance == null) {
=======
    private var ourInstance: Retrofit?= null
    val instance:Retrofit
        get(){
            if(ourInstance == null) {
>>>>>>> 1bf9eb24fc69419d3f16ac1848e4cbe2d6a01544
                ourInstance = Retrofit.Builder()
                        .baseUrl("http://guarded-inlet-38884.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return ourInstance!!

        }
<<<<<<< HEAD
}

object RetrofitClientDrinksTop10 {
    private var ourInstance:Retrofit?=null
    val instance: Retrofit
        get() {
            if (RetrofitClientDrinksTop10.ourInstance == null) {
                RetrofitClientDrinksTop10.ourInstance = Retrofit.Builder()
                        .baseUrl("http://guarded-inlet-38884.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return ourInstance!!

        }
=======

>>>>>>> 1bf9eb24fc69419d3f16ac1848e4cbe2d6a01544
}

