package br.com.fabriciohsilva.heroesapp.view.form

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.model.ResponseStatus
import br.com.fabriciohsilva.heroesapp.repository.HeroRepository

class FormViewModel : ViewModel() {


    val heroRepository = HeroRepository()
    val responseStatus: MutableLiveData<ResponseStatus> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun save(
        name: String,
        power: String
    ) {

        isLoading.value = true
        val hero = Hero(name = name, power = power)

        heroRepository.saveHero(hero,
            onComplete = {
                isLoading.value = false
                responseStatus.value = ResponseStatus(
                    true,
                    "Dado gravado com sucesso"
                )
            }, onError = {
                isLoading.value = false
                responseStatus.value = ResponseStatus(
                    false,
                    it?.message!!
                )
            })

    }

}//end class FormularioViewModel