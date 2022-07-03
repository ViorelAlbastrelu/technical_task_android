package com.sliide.usermanager.ui.userslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sliide.usermanager.databinding.ItemUserBinding
import com.sliide.usermanager.domain.model.User

class UsersAdapter(
    private val onLongClickListener: View.OnLongClickListener
) : ListAdapter<User, UsersAdapter.UserViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position), onLongClickListener)
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, onLongClickListener: View.OnLongClickListener) {
            itemView.tag = user.id
            binding.apply {
                root.setOnLongClickListener(onLongClickListener)
                userName.text = user.name
                userEmail.text = user.email
                userCreationTime.text = user.creationTime
            }
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id && oldItem.email == newItem.email
        }
    }
}