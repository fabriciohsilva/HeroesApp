package br.com.fabriciohsilva.heroesapp.api

import br.com.fabriciohsilva.heroesapp.model.Hero
import retrofit2.Call
import retrofit2.http.*

interface HeroAPI {

    @GET("/heroes")
    fun getHeroes() : Call<List<Hero>>

    @GET("/heroes/{id}")
    fun getHero(@Path("id") id:String): Call<Hero>

    @POST("/heroes")
    fun saveHero(@Body hero: Hero): Call<Hero>

    @DELETE("heroes/{id}")
    fun deleteHero(@Path("id") id:String): Call<Hero>

    @PATCH("heroes/{id}")
    fun updateHero(@Path("id") id:String, @Body hero: Hero): Call<Hero>

}