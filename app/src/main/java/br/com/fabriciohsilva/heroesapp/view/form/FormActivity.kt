package br.com.fabriciohsilva.heroesapp.view.form

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
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
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream




class FormActivity : AppCompatActivity() {

    private lateinit var formViewModel: FormViewModel
    //private lateinit var helper: utilsHelper
    private val RESULT_LOAD_IMAGE = 1
    private var heroAvatar: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)


        formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        var hero =  getIntent().getSerializableExtra("hero") as? Hero

        if (hero != null ){
            etName.editText?.setText(hero.name)
            etPower.editText?.setText(hero.power)
            etWeakness.editText?.setText(hero.weakness)
            swVilain.isChecked = hero.villain

            if (hero.avatar != null && hero.avatar != "") {
                heroAvatar = hero.avatar!!
                decodeBase64AndSetImage(hero.avatar!!, ibHeroAvatar)
            }

        }//end if (hero != null )

        ibHeroAvatar.setOnClickListener {
            if (checkAndRequestPermissions()) {
                val i = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
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


    private fun checkAndRequestPermissions(): Boolean {
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val listPermissionsNeeded = ArrayList<String>()

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }//end private fun checkAndRequestPermissions


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

                // Fill with actual results from user
                if (grantResults.size > 0) {

                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]

                    if ( perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        val i = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK(getString(R.string.permission_required),
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE ->
                                            // proceed with logic by disabling the related features or quit the app.
                                            finish()
                                    }
                                })
                        } else {
                            explain(getString(R.string.permission_required))
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }

    }//end override fun onRequestPermissionsResult


    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton(getString(R.string.cancel), okListener)
            .create()
            .show()
    }

    private fun explain(msg: String) {
        val dialog = android.support.v7.app.AlertDialog.Builder(this)
        dialog.setMessage(msg)
            .setPositiveButton(getString(R.string.yes)) { paramDialogInterface, paramInt ->
                //startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("br.com.fabriciohsilva.heroesapp")))
                val intent = Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                val uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
            .setNegativeButton(getString(R.string.cancel)) { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(
                selectedImage!!,
                filePathColumn, null, null, null
            )
            cursor!!.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()

            val imageView = findViewById<View>(R.id.ibHeroAvatar) as ImageView
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))

            //var heroAvatar = getBase64String(picturePath)
            textViewIMG.text = getBase64String(picturePath)
            heroAvatar = textViewIMG.text.toString()
        }
    }//end override fun onActivityResult


    private fun getBase64String(mCurrentPhotoPath: String): String {
        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream) // In case you want to compress your image, here it's at 40%
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
