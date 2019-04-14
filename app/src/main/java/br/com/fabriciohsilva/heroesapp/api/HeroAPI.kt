package br.com.fabriciohsilva.heroesapp.api

import br.com.fabriciohsilva.heroesapp.model.Hero
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HeroAPI {

    @GET("/heroes")
    fun getHeroes() : Call<List<Hero>>

    @GET("/heroes/{id}")
    fun getHero(@Path("id") id:String)

    @POST("/heroes")
    fun saveHero(@Body hero: Hero): Call<Hero>

}