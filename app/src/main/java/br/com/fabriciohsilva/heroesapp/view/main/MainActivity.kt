package br.com.fabriciohsilva.heroesapp.view.main

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.view.form.FormActivity
import br.com.fabriciohsilva.heroesapp.view.form.FormViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.loading.*

class MainActivity : AppCompatActivity() {

    //var mainViewModel: MainViewModel? = null
    lateinit var mainViewModel: MainViewModel
    lateinit var formViewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        mainViewModel.searchAll()

        registerObservers()

        fab.setOnClickListener {
                view -> startActivityForResult( Intent(this, FormActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            mainViewModel.searchAll()
        }
    }


    private fun registerObservers() {
        mainViewModel.isLoading.observe(this, isloadingObserver)
        mainViewModel.msgError.observe(this, msgErrorObserver)
        mainViewModel.heroes.observe(this, notasObserver)
    }//end private fun registerObservers

    private var isloadingObserver = Observer<Boolean>{
        if(it == true){
            containerLoading.visibility = View.VISIBLE
        } else {
            containerLoading.visibility = View.GONE
        }//end if(it == true)
    }//end private var isloadingObserver

    private var msgErrorObserver = Observer<String>{
        if (it!!.isNotEmpty()) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }//end if (it!!.isNotEmpty())
    }//end private var msgErrorObserver

    private  var notasObserver = Observer<List<Hero>> {
        rvHeroes.adapter = MainListAdapter(it!!, this , mainViewModel, formViewModel)
        rvHeroes.layoutManager = LinearLayoutManager(this)
    }

}
