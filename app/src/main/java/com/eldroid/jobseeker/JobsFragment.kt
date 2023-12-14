package com.eldroid.jobseeker

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.jobseeker.adapters.JobAdapter
import com.eldroid.jobseeker.data.Job
import com.eldroid.jobseeker.databinding.FragmentJobsBinding
import com.eldroid.jobseeker.models.JobViewModel

class JobsFragment : Fragment() {

    private lateinit var binding: FragmentJobsBinding
    private lateinit var jobAdapter: JobAdapter
    private lateinit var jobViewModel: JobViewModel

    private var allJobs: List<Job> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize ViewModel
        jobViewModel = ViewModelProvider(this)[JobViewModel::class.java]

        // Initialize RecyclerView and Adapter
        jobAdapter = JobAdapter { job ->
            jobViewModel.onBookmarkClick(job)
        }
        binding.recyclerViewJobs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = jobAdapter
        }

        // Observe changes in jobs from ViewModel
        jobViewModel.getJobs().observe(viewLifecycleOwner) { jobs ->
            allJobs = jobs
            jobAdapter.submitList(allJobs)
        }

        setHasOptionsMenu(true) // Enable options menu for search

        binding.searchViewJobs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query change
                filterJobs(newText)
                return true
            }
        })

        return view
    }

    private fun filterJobs(query: String?) {
        val filteredJobs = if (query.isNullOrBlank()) {
            allJobs
        } else {
            allJobs.filter { job ->
                job.jobPosition.contains(query, ignoreCase = true) ||
                        job.company.contains(query, ignoreCase = true) ||
                        job.location.contains(query, ignoreCase = true)
            }
        }

        jobAdapter.submitList(filteredJobs)
    }
}