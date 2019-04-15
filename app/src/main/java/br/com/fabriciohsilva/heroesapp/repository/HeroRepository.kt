package br.com.fabriciohsilva.heroesapp.repository

import br.com.fabriciohsilva.heroesapp.api.getHeroAPI
import br.com.fabriciohsilva.heroesapp.model.Hero
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeroRepository {

    fun searchAll(
        onComplete:(List<Hero>?) -> Unit,
        onError:(Throwable?) -> Unit
    ) {
        getHeroAPI()
            .getHeroes()
            .enqueue(object : Callback<List<Hero>> {
                override fun onFailure(call: Call<List<Hero>>, t: Throwable) {
                    onError(t)
                }//end override fun onFailure

                override fun onResponse(call: Call<List<Hero>>, response: Response<List<Hero>>) {
                    if(response.isSuccessful) {
                        onComplete(response.body())
                    } else {
                        onError(Throwable("Error on fetch data."))
                    }//end if(response.isSuccessful)

                }//end override fun onResponse

            })//end .enqueue

    }//end fun searchAll


    fun saveHero(hero: Hero,
               onComplete: (Hero) -> Unit,
               onError: (Throwable?) -> Unit) {
        getHeroAPI()
            .saveHero(hero)
            .enqueue(object : Callback<Hero>{
                override fun onFailure(call: Call<Hero>, t: Throwable) {
                    onError(t)
                }//end override fun onFailure

                override fun onResponse(call: Call<Hero>, response: Response<Hero>) {
                    onComplete(response.body()!!)
                }//end override fun onResponse
            })//end getHeroAPI.enqueue
    }//end fun saveHero

}//end class HeroRepository