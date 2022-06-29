package com.sliide.usermanager.ui.userslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sliide.usermanager.databinding.ItemUserBinding
import com.sliide.usermanager.model.User

class UsersAdapter : ListAdapter<User, UsersAdapter.UserViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                userName.text = user.name
                userEmail.text = user.email
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