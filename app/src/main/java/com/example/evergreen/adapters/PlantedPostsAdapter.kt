package com.example.evergreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evergreen.R
import com.example.evergreen.model.Post
import com.example.evergreen.utils.Constants
import kotlinx.android.synthetic.main.item_post_approved.view.*
import kotlinx.android.synthetic.main.item_post_planted.view.*

class PlantedPostsAdapter(private val context: Context,
                          private var list: ArrayList<Post>,
                          private var creatorsList : ArrayList<String>,
                          private var planterList : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_post_planted,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.imageBefore)
                .centerCrop()
                .placeholder(R.drawable.ic_post_image_150)
                .into(holder.itemView.iv_post_image_planted)


            holder.itemView.tv_location_planted.text = model.location
            if(model.descriptionByCreator.isNotEmpty())
                holder.itemView.tv_description_planted.text = model.descriptionByCreator
            else
                holder.itemView.tv_description_planted.text = Constants.NO_DESCRIPTION_AVAILABLE

            holder.itemView.tv_posted_by_planted.text = creatorsList[position]
            model.postedBy = creatorsList[position]
            model.bookedBy = planterList[position]
            holder.itemView.tag = model

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder: RecyclerView.ViewHolder, View.OnClickListener{

        var btn_switch_image =itemView.findViewById(R.id.btn_switch_image_planted) as Button
        var context =itemView.context
        var switch_postedBy = itemView.findViewById(R.id.tv_posted_by_planted) as TextView


        constructor(itemView: View) : super(itemView) {
           btn_switch_image.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val model= itemView.tag as Post
            if(btn_switch_image.text == "SEE CURRENT IMAGE"){
                Glide
                        .with(context)
                        .load(model.imageAfter)
                        .centerCrop()
                        .placeholder(R.drawable.ic_post_image_150)
                        .into(itemView.iv_post_image_planted)

                if(model.descriptionByPlanter.isNotEmpty())
                    itemView.tv_description_planted.text = model.descriptionByPlanter
                else
                    itemView.tv_description_planted.text = Constants.NO_DESCRIPTION_AVAILABLE

                btn_switch_image.text = "SEE PREVIOUS IMAGE"

                switch_postedBy.text = model.bookedBy


            }
            else{
                Glide
                        .with(context)
                        .load(model.imageBefore)
                        .centerCrop()
                        .placeholder(R.drawable.ic_post_image_150)
                        .into(itemView.iv_post_image_planted)

                if(model.descriptionByCreator.isNotEmpty())
                    itemView.tv_description_planted.text = model.descriptionByCreator
                else
                    itemView.tv_description_planted.text = Constants.NO_DESCRIPTION_AVAILABLE

                btn_switch_image.text = "SEE CURRENT IMAGE"

                switch_postedBy.text = model.postedBy

            }
        }

    }
}