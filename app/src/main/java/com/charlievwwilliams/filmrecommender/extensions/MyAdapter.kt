package com.charlievwwilliams.filmrecommender.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.charlievwwilliams.filmrecommender.R
import com.squareup.picasso.Picasso

class MyAdapter(
    private val titleArray: Array<String>,
    private val descriptionArray: Array<String>,
    private val imageArray: Array<String>,
    private val idArray: Array<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView: TextView = view.findViewById(R.id.textView)
        val textView2: TextView = view.findViewById(R.id.textView2)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener.onItemClick(idArray[adapterPosition])
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.film_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = titleArray[position]
        viewHolder.textView2.text = descriptionArray[position]
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + imageArray[position])
            .into(viewHolder.imageView)
    }

    override fun getItemCount() = titleArray.size

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}