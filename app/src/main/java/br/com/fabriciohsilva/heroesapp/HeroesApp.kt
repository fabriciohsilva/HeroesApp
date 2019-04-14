package br.com.fabriciohsilva.heroesapp

import com.facebook.stetho.Stetho
import android.app.Application


class HeroesApp : Application() {

    @Override
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }//end override fun onCreate()

}//end class HeroesApp