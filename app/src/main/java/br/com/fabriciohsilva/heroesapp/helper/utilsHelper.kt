package br.com.fabriciohsilva.heroesapp.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import android.app.Activity
import br.com.fabriciohsilva.heroesapp.model.Hero
import android.widget.RatingBar
import android.widget.EditText
import android.widget.Switch


public class utilsHelper {

    private val fieldName: EditText? = null
    private val fieldPower: EditText? = null
    private val fieldWeakness: EditText? = null
    private val fieldVillain: Switch? = null
    //private val fieldAvatar: String? = null

    private val hero: Hero? = null

    public fun utilsHelper(activity: Activity) {

    }

    public fun getBase64String(mCurrentPhotoPath: String): String {

        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }//end private fun getBase64String

    public fun decodeBase64AndSetImage(completeImageData: String, imageView: ImageView) {
        // Incase you're storing into aws or other places where we have extension stored in the starting.
        val imageDataBytes = completeImageData.substring(completeImageData.indexOf(",") + 1)
        val stream = ByteArrayInputStream(Base64.decode(imageDataBytes.toByteArray(), Base64.DEFAULT))
        val bitmap = BitmapFactory.decodeStream(stream)

        imageView.setImageBitmap(bitmap)
    }// private fun decodeBase64AndSetImage


}