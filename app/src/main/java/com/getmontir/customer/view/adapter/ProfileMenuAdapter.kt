package com.getmontir.customer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.getmontir.customer.R
import com.getmontir.customer.data.ProfileMenuItem

class ProfileMenuAdapter(
    private val dataSet: Array<ProfileMenuItem>,
    private val callback: ProfileMenuCallback
): RecyclerView.Adapter<ProfileMenuAdapter.ViewHolder>() {

    interface ProfileMenuCallback {
        fun onItemClicked(tag: String)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val layout: LinearLayout = view.findViewById(R.id.layout)
        val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
        val txtName: TextView = view.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_menu, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.apply {

            // Setup view
            imgIcon.setImageResource( item.icon )
            txtName.text = item.title

            // Setup layout
            layout.tag = item.id
            layout.setOnClickListener {
                callback.onItemClicked(item.id)
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

}