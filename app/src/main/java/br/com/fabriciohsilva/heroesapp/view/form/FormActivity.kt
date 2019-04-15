package br.com.fabriciohsilva.heroesapp.view.form

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.ResponseStatus
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.loading.*

class FormActivity : AppCompatActivity() {

    private lateinit var formViewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        btnSave.setOnClickListener {
            formViewModel.save(
                etName.editText?.text.toString(),
                etPower.editText?.text.toString()
            )
        }

        registerObserver()
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
