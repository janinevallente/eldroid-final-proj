package com.eldroid.jobseeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldroid.jobseeker.adapters.DiscussionAdapter
import com.eldroid.jobseeker.databinding.FragmentDiscussionsBinding
import com.eldroid.jobseeker.models.DiscussionViewModel

class DiscussionsFragment : Fragment() {

    private var _binding: FragmentDiscussionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DiscussionViewModel
    private lateinit var discussionAdapter: DiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscussionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DiscussionViewModel::class.java)

        // Initialize RecyclerView and Adapter
        discussionAdapter = DiscussionAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = discussionAdapter
        }

        // Observe changes in discussions LiveData
        viewModel.discussions.observe(viewLifecycleOwner) { discussions ->
            // Update the UI with the new discussions
            discussionAdapter.submitList(discussions)
        }

        // Load discussions when the fragment is created
        viewModel.loadDiscussions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}