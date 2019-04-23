package br.com.fabriciohsilva.heroesapp.view.form

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.model.ResponseStatus
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.loading.*

class FormActivity : AppCompatActivity() {

    private lateinit var formViewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        var hero =  getIntent().getSerializableExtra("hero") as? Hero

        if (hero != null ){
            etName.editText?.setText(hero.name)
            etPower.editText?.setText(hero.power)
        }

        btnSave.setOnClickListener {
            val valid = validateForm()
            if(valid) {
                if (hero != null) {
                    formViewModel.update(
                        hero._id!!,
                        etName.editText?.text.toString(),
                        etPower.editText?.text.toString()
                    )
                } else {
                    formViewModel.save(
                        etName.editText?.text.toString(),
                        etPower.editText?.text.toString()
                    )
                }
            }

        }

        registerObserver()
    }

    private fun validateForm(): Boolean {
        var name = true
        var power = true
        var weakness = true

        if (etName.editText?.length() === 0) {
            etName.error = getText(R.string.nameRequired)
            name = false
        } else {
            etName.error = null
        }

        if (etPower.editText?.length() === 0) {
            etPower.error = getText(R.string.powerRequired)
            power = false
        } else {
            etPower.error = null
        }

        if (etWeakness.editText?.length() === 0) {
            etWeakness.error = getText(R.string.weaknessRequired)
            weakness = false
        } else {
            etWeakness.error = null
        }

        return name && power && weakness
    }

    private fun registerObserver() {
        formViewModel.responseStatus.observe(this, responseObserver)
        formViewModel.isLoading.observe(this, loadingObserver)
    }

    private var loadingObserver = Observer<Boolean> {
        if (it == true) {
            containerLoading.visibility = View.VISIBLE
        } else {
            containerLoading.visibility = View.GONE
        }
    }

    private var responseObserver = Observer<ResponseStatus> {
        Toast.makeText(this, it?.mensagem, Toast.LENGTH_SHORT).show()
        if (it?.sucesso == true) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

}
