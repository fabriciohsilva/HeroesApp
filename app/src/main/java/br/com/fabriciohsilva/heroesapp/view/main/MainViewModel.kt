package br.com.fabriciohsilva.heroesapp.view.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.repository.HeroRepository

class MainViewModel: ViewModel() {

    val heroRepository = HeroRepository()

    val heroes : MutableLiveData<List<Hero>> = MutableLiveData()
    val msgError : MutableLiveData<String> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    fun searchAll() {
        isLoading.value = true

        heroRepository.searchAll(
            onComplete = {
                isLoading.value = false
                heroes.value = it

            }, //end onComplete
            onError = {
                isLoading.value = false
                msgError.value = it?.message
            })//end onError

    }//end fun searchAll()

}//end class MainViewModel: ViewModel()