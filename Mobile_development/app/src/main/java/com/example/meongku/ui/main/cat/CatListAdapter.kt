package com.example.meongku.ui.main.cat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meongku.R
import com.example.meongku.api.catlist.Cat

class CatListAdapter(private var cats: List<Cat> = emptyList()) :
    RecyclerView.Adapter<CatListAdapter.CatViewHolder>() {

    private var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat_list, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
    }

    override fun getItemCount(): Int = cats.size

    fun updateCats(newCats: List<Cat>) {
        cats = newCats
        notifyDataSetChanged()
    }

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageView: ImageView = itemView.findViewById(R.id.ivKucing)
        private val textView: TextView = itemView.findViewById(R.id.tvRas)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(cat: Cat) {
            textView.text = cat.race
            Glide.with(itemView.context)
                .load(cat.photo)
                .into(imageView)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val cat = cats[position]
                onItemClick?.invoke(cat.id)
            }
        }
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClick = listener
    }
}
