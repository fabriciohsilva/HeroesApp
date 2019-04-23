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
        power: String,
        weakness: String,
        villain: Boolean,
        avatar: String
    ) {

        isLoading.value = true
        val hero = Hero(name = name, power = power, weakness = weakness, villain = villain, avatar = avatar)

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

    fun update(
        id: String,
        name: String,
        power: String,
        weakness: String,
        villain: Boolean,
        avatar: String
    ) {

        isLoading.value = true

        val hero = Hero(name = name, power = power, weakness = weakness, villain = villain, avatar = avatar)

        heroRepository.updateHero(id, hero,
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

    fun delete(
       hero: Hero
    ) {

        isLoading.value = true

        heroRepository.deleteHero(hero._id!!,
            onComplete = {
                isLoading.value = false
                responseStatus.value = ResponseStatus(
                    true,
                    "heroi excluído com sucesso"
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