package br.com.fabriciohsilva.heroesapp.view.main


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.fabriciohsilva.heroesapp.R
import br.com.fabriciohsilva.heroesapp.model.Hero
import kotlinx.android.synthetic.main.hero_item.view.*

class MainListAdapter( val heroes: List<Hero>, val context: Context ): RecyclerView.Adapter<MainListAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val itemView = LayoutInflater
            .from(context)
            .inflate(R.layout.hero_item, p0, false)

        return NoteViewHolder(itemView)
    }//end override fun onCreateViewHolder

    override fun getItemCount(): Int {
        return heroes.size
    }//end override fun getItemCount

    override fun onBindViewHolder(p0: NoteViewHolder, position: Int) {
        val hero = heroes[position]
        p0.bindView(hero)
    }//end override fun onBindViewHolder

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindView(hero: Hero) = with(itemView) {
            tvName.text = hero.name
            tvPower.text = hero.power
        }//end fun bindView

    }//end class NoteViewHolder

}//end class MainListAdapter