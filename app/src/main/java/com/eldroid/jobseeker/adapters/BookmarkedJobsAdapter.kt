package com.eldroid.jobseeker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.jobseeker.R
import com.eldroid.jobseeker.data.Job
import com.eldroid.jobseeker.databinding.ItemBookmarkedJobBinding

class BookmarkedJobsAdapter(
    private val onBookmarkClickListener: (Job) -> Unit
) : ListAdapter<Job, BookmarkedJobsAdapter.BookmarkedJobViewHolder>(DiffCallback()) {

    class BookmarkedJobViewHolder(binding: ItemBookmarkedJobBinding) : RecyclerView.ViewHolder(binding.root) {
        val jobPositionTextView = binding.jobPositionView
        val companyTextView = binding.companyView
        val locationTextView = binding.locationView
        val jobContentView = binding.jobContentView
        val salaryView = binding.salaryView
        val bookmarkImageView = binding.imageViewBookmark
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkedJobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookmarkedJobBinding.inflate(inflater, parent, false)
        return BookmarkedJobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkedJobViewHolder, position: Int) {
        val currentJob = getItem(position)

        holder.jobPositionTextView.text = currentJob.jobPosition
        holder.companyTextView.text = currentJob.company
        holder.locationTextView.text = currentJob.location
        holder.jobContentView.text = currentJob.jobContent
        holder.salaryView.text = currentJob.salary

        val bookmarkIconResId = if (currentJob.isBookmarked) {
            R.drawable.ic_bookmark_filled
        } else {
            R.drawable.ic_bookmark_border
        }
        holder.bookmarkImageView.setImageResource(bookmarkIconResId)

        holder.bookmarkImageView.setOnClickListener {
            onBookmarkClickListener.invoke(currentJob)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }
}
