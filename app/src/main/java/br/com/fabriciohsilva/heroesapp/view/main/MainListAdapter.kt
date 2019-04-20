package br.com.fabriciohsilva.heroesapp.view.main


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivity
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val itemView = LayoutInflater
            .from(context)
            .inflate(R.layout.hero_item, p0, false)

        //formViewModel = ViewModelProviders.of(context).get(MainActivity::class.java)
        //formViewModel = ViewModelProviders.of(itemView.getParentActivity()).get(MainActivity::class.java)

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
            context.startActivity(intent)
        }

        p0.itemView.setOnLongClickListener {
            formViewModel.delete(heroes.get(position))
            Toast.makeText(context, heroes.get(position).name + " Excluído da lista", Toast.LENGTH_SHORT).show()
            true
        }

    }//end override fun onBindViewHolder

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(hero: Hero) = with(itemView) {
            tvName.text  = hero.name
            tvPower.text = hero.power
        }//end fun bindView

    }//end class NoteViewHolder

}//end class MainListAdapter