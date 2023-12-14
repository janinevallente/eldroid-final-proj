package com.eldroid.jobseeker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.jobseeker.data.Discussion
import com.eldroid.jobseeker.databinding.ItemDiscussionBinding

class DiscussionAdapter : ListAdapter<Discussion, DiscussionAdapter.DiscussionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val binding = ItemDiscussionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscussionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val discussion = getItem(position)
        holder.bind(discussion)
    }

    class DiscussionViewHolder(private val binding: ItemDiscussionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(discussion: Discussion) {
            binding.apply {
                // Bind discussion data to views
                binding.nameView.text = discussion.name
                binding.emailView.text = discussion.email
                binding.titleView.text = discussion.title
                binding.contentView.text = discussion.content
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Discussion>() {
        override fun areItemsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
            return oldItem.title == newItem.title // Adjust based on your item's identifier
        }

        override fun areContentsTheSame(oldItem: Discussion, newItem: Discussion): Boolean {
            return oldItem == newItem
        }
    }
}

