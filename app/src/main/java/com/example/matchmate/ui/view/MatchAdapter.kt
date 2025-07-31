package com.example.matchmate.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchmate.R
import com.example.matchmate.data.model.UserProfile

class MatchAdapter(
    private val onAcceptClicked: (UserProfile) -> Unit,
    private val onDeclineClicked: (UserProfile) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private val matchList = mutableListOf<UserProfile>()
    private var showLoading = false

    fun submitList(users: List<UserProfile>) {
        matchList.apply {
            clear()
            addAll(users)
        }
        notifyDataSetChanged()
    }

    fun showLoadingSpinner(show: Boolean) {
        if (showLoading != show) {
            showLoading = show
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = matchList.size + if (showLoading) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (position < matchList.size) VIEW_TYPE_USER else VIEW_TYPE_LOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> MatchViewHolder(
                inflater.inflate(
                    R.layout.item_match_card,
                    parent,
                    false
                )
            )

            VIEW_TYPE_LOADING -> LoadingViewHolder(
                inflater.inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchViewHolder && position < matchList.size) {
            holder.bind(matchList[position])
        }
    }

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvNationalityAge: TextView = itemView.findViewById(R.id.tvNationalityAge)
        private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        private val btnDecline: Button = itemView.findViewById(R.id.btnDecline)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(user: UserProfile) = with(user) {
            tvName.text = "${name.title} ${name.first} ${name.last}"
            tvNationalityAge.text = "$nat, ${dob.age}"

            Glide.with(itemView.context)
                .load(picture.large)
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivProfile)

            val isPending = status == itemView.context.getString(R.string.pending)
            tvStatus.text = status
            tvStatus.visibility = if (isPending) View.GONE else View.VISIBLE
            btnAccept.visibility = if (isPending) View.VISIBLE else View.GONE
            btnDecline.visibility = if (isPending) View.VISIBLE else View.GONE

            btnAccept.setOnClickListener {
                onAcceptClicked(this)
                updateButtonVisibility()
            }

            btnDecline.setOnClickListener {
                onDeclineClicked(this)
                updateButtonVisibility()
            }
        }

        private fun updateButtonVisibility() {
            btnAccept.visibility = View.GONE
            btnDecline.visibility = View.GONE
            tvStatus.visibility = View.VISIBLE
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
