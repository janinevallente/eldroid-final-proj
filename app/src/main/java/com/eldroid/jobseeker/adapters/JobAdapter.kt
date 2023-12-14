package com.eldroid.jobseeker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eldroid.jobseeker.R
import com.eldroid.jobseeker.data.Job
import com.eldroid.jobseeker.databinding.ItemJobBinding

class JobAdapter(private val onBookmarkClickListener: (Job) -> Unit) :
    ListAdapter<Job, JobAdapter.JobViewHolder>(DiffCallback()) {

    class JobViewHolder(binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        val jobPositionTextView = binding.jobPositionView
        val companyTextView = binding.companyView
        val locationTextView = binding.locationView
        val jobContentView = binding.jobContentView
        val salaryView = binding.salaryView
        val bookmarkImageView = binding.imageViewBookmark
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)

        holder.jobPositionTextView.text = job.jobPosition
        holder.companyTextView.text = job.company
        holder.locationTextView.text = job.location
        holder.jobContentView.text = job.jobContent
        holder.salaryView.text = job.salary

        val bookmarkIconResId = if (!job.isBookmarked) {
            R.drawable.ic_bookmark_filled
        } else {
            R.drawable.ic_bookmark_border
        }
        holder.bookmarkImageView.setImageResource(bookmarkIconResId)

        holder.bookmarkImageView.setOnClickListener {
            onBookmarkClickListener.invoke(job)
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