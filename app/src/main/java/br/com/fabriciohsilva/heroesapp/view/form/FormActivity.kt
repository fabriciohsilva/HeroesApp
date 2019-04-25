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
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream
import br.com.fabriciohsilva.heroesapp.helper.utilsHelper
import java.io.FileNotFoundException


class FormActivity : AppCompatActivity() {

    private lateinit var formViewModel: FormViewModel
    private lateinit var helper: utilsHelper
    private val RESULT_LOAD_IMAGE = 1
    private var heroAvatar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        //helper = utilsHelper(this);


        formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        var hero =  getIntent().getSerializableExtra("hero") as? Hero

        if (hero != null ){
            etName.editText?.setText(hero.name)
            etPower.editText?.setText(hero.power)
            etWeakness.editText?.setText(hero.weakness)
            swVilain.isChecked = hero.villain

            if (hero.avatar != null) {
                heroAvatar = hero.avatar!!
                decodeBase64AndSetImage(hero.avatar!!, ibHeroAvatar)
            }

        }//end if (hero != null )

        ibHeroAvatar.setOnClickListener {
            val i = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }//end ibHeroAvatar.setOnClickListener


        btnSave.setOnClickListener {
            val valid = validateForm()
            if(valid) {
                if (hero != null) {
                    formViewModel.update(
                        hero._id!!,
                        etName.editText?.text.toString(),
                        etPower.editText?.text.toString(),
                        etWeakness.editText?.text.toString(),
                        swVilain.isChecked,
                        heroAvatar
                    )
                } else {
                    formViewModel.save(
                        etName.editText?.text.toString(),
                        etPower.editText?.text.toString(),
                        etWeakness.editText?.text.toString(),
                        swVilain.isChecked,
                        heroAvatar
                    )
                }
            }

        }

        registerObserver()
    }//end override fun onCreate

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
//            val selectedImage = data.data
//            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//
//            val cursor = contentResolver.query(
//                selectedImage!!,
//                filePathColumn, null, null, null
//            )
//            cursor!!.moveToFirst()
//
//            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//            val picturePath = cursor.getString(columnIndex)
//            cursor.close()
//
//            val imageView = findViewById<View>(R.id.ibHeroAvatar) as ImageView
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//
//            var heroAvatar = getBase64String(picturePath)
//            //var heroAvatar = getBa
//        }
//    }//end override fun onActivityResult

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                val imageStream = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                ibHeroAvatar.setImageBitmap(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(this, getString(R.string.no_picked_image), Toast.LENGTH_LONG).show()
        }
    }

    private fun getBase64String(mCurrentPhotoPath: String): String {

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }//end private fun getBase64String

    private fun decodeBase64AndSetImage(completeImageData: String, imageView: ImageView) {
        // Incase you're storing into aws or other places where we have extension stored in the starting.
        val imageDataBytes = completeImageData.substring(completeImageData.indexOf(",") + 1)
        val stream = ByteArrayInputStream(Base64.decode(imageDataBytes.toByteArray(), Base64.DEFAULT))
        val bitmap = BitmapFactory.decodeStream(stream)

        imageView.setImageBitmap(bitmap)
    }// private fun decodeBase64AndSetImage


    private fun validateForm(): Boolean {
        var name = true
        var power = true
        var weakness = true

        if (etName.editText?.length() === 0) {
            etName.error = getText(R.string.name_required)
            name = false
        } else {
            etName.error = null
        }

        if (etPower.editText?.length() === 0) {
            etPower.error = getText(R.string.power_required)
            power = false
        } else {
            etPower.error = null
        }

        if (etWeakness.editText?.length() === 0) {
            etWeakness.error = getText(R.string.weakness_required)
            weakness = false
        } else {
            etWeakness.error = null
        }

        return name && power && weakness
    }//end private fun validateForm

    private fun registerObserver() {
        formViewModel.responseStatus.observe(this, responseObserver)
        formViewModel.isLoading.observe(this, loadingObserver)
    }//end private fun registerObserver

    private var loadingObserver = Observer<Boolean> {
        if (it == true) {
            containerLoading.visibility = View.VISIBLE
        } else {
            containerLoading.visibility = View.GONE
        }
    }//end private var loadingObserver

    private var responseObserver = Observer<ResponseStatus> {
        Toast.makeText(this, it?.mensagem, Toast.LENGTH_SHORT).show()
        if (it?.sucesso == true) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }//end private var responseObserver

}//end class FormActivity
