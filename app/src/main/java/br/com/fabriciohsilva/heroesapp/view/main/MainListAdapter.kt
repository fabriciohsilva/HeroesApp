package br.com.fabriciohsilva.heroesapp.view.main



import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.Hero
import br.com.fabriciohsilva.heroesapp.view.form.FormActivity
import br.com.fabriciohsilva.heroesapp.view.form.FormViewModel
import kotlinx.android.synthetic.main.hero_item.view.*





class MainListAdapter( val heroes: List<Hero>, val context: Context ): RecyclerView.Adapter<MainListAdapter.NoteViewHolder>() {

    lateinit var formViewModel: FormViewModel
    val activity = context as Activity
    val PICK_CONTACT_REQUEST = 1  // The request code

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val itemView = LayoutInflater
            .from(context)
            .inflate(R.layout.hero_item, p0, false)

        //val activity = context as Fragment
        //formViewModel = ViewModelProviders.of(activity).get(FormViewModel::class.java)

        return NoteViewHolder(itemView)
    }//end override fun onCreateViewHolder

    override fun getItemCount(): Int {
        return heroes.size
    }//end override fun getItemCount

    override fun onBindViewHolder(p0: NoteViewHolder, position: Int) {
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
            builder.setTitle("Exluir item")
            builder.setMessage("Deseja excluir o item " + heroes.get(position).name + " da lista ?")

            // Do something when user press the positive button
            builder.setPositiveButton("YES"){dialog, which ->
                Toast.makeText(context, heroes.get(position).name + " Excluído da lista", Toast.LENGTH_SHORT).show()
                //formViewModel.delete(heroes.get(position))
            }


            // Display a negative button on alert dialog
            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(context, " O item não foi excluído da lista", Toast.LENGTH_SHORT).show()
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

    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == 1) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(hero: Hero) = with(itemView) {
            tvName.text  = hero.name
            tvPower.text = hero.power
        }//end fun bindView

    }//end class NoteViewHolder

}//end class MainListAdapter