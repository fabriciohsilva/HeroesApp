package br.com.fabriciohsilva.heroesapp.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.view.form.FormActivity
import br.com.fabriciohsilva.heroesapp.view.form.FormViewModel
import kotlinx.android.synthetic.main.hero_item.view.*
import java.io.ByteArrayInputStream


class MainListAdapter( val heroes: List<Hero>, val context: Context, val mainViewModel: MainViewModel, val formViewModel: FormViewModel): RecyclerView.Adapter<MainListAdapter.HeroViewHolder>() {


    val activity = context as Activity

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HeroViewHolder {
        val itemView = LayoutInflater
            .from(context)
            .inflate(R.layout.hero_item, p0, false)

        val activity = context as Activity

        return HeroViewHolder(itemView)
    }//end override fun onCreateViewHolder

    @Override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            mainViewModel.searchAll()
        }
    }


    override fun getItemCount(): Int {
        return heroes.size
    }//end override fun getItemCount

    override fun onBindViewHolder(p0: HeroViewHolder, position: Int) {
        val hero = heroes[position]
        p0.bindView(hero)

        p0.itemView.setOnClickListener {
                //view -> startActivityForResult( Intent(this, FormActivity::class.java), 1)
            //

            var intent = Intent(context, FormActivity::class.java)
            intent.putExtra("hero", heroes.get(position))
            startActivityForResult(activity, intent, 1, null)
            //context.startActivity(intent)
        }

        p0.itemView.setOnLongClickListener {

            val builder = AlertDialog.Builder(context)
            // Set the alert dialog title
            builder.setTitle(context.getText(R.string.delete_item))
            builder.setMessage(context.getString(R.string.confirm_delete) + " " +heroes.get(position).name +
                    " " + context.getText(R.string.from_list) + "?")

            // Do something when user press the positive button
            builder.setPositiveButton(context.getText(R.string.yes)){dialog, which ->
                var nameHero = heroes.get(position).name

                formViewModel.delete(heroes.get(position))
                mainViewModel.searchAll()
                Toast.makeText(context, nameHero + " " + context.getString(R.string.deleted_item), Toast.LENGTH_SHORT).show()
            }


            // Display a negative button on alert dialog
            builder.setNegativeButton(context.getString(R.string.no)){dialog,which ->
                Toast.makeText(context, context.getString(R.string.no_deleted), Toast.LENGTH_SHORT).show()
            }

//            // Display a neutral button on alert dialog
//            builder.setNeutralButton("Cancel"){_,_ ->
//                Toast.makeText(context, "Cancelou a ação", Toast.LENGTH_SHORT).show()
//            }

            // Finally, make the alert dialog using builder
            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on app interface
            dialog.show()

            true
        }

    }//end override fun onBindViewHolder

    class HeroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(hero: Hero) = with(itemView) {
            tvName.text = hero.name
            tvPower.text = hero.power
            tvWeakness.text = hero.weakness
            if (hero.villain){
                hero_villain_logo.setBackgroundResource(R.drawable.villain)
            } else {
                hero_villain_logo.setBackgroundResource(R.drawable.hero)
            }
            if (hero.avatar != null && hero.avatar != "")
                decodeBase64AndSetImage(hero.avatar!!, ivHeroAvatar)
            else
                ivHeroAvatar.setBackgroundResource(R.drawable.default_avatar)
        }//end fun bindView

        private fun decodeBase64AndSetImage(completeImageData: String, imageView: ImageView) {
            val imageDataBytes = completeImageData.substring(completeImageData.indexOf(",") + 1)
            val stream = ByteArrayInputStream(Base64.decode(imageDataBytes.toByteArray(), Base64.DEFAULT))
            val bitmap = BitmapFactory.decodeStream(stream)

            imageView.setImageBitmap(bitmap)
        }// private fun decodeBase64AndSetImage

    }//end class NoteViewHolder

}//end class MainListAdapter