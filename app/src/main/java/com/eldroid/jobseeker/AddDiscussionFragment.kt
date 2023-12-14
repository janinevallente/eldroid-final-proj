package com.eldroid.jobseeker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eldroid.jobseeker.databinding.FragmentAddDiscussionBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddDiscussionFragment : Fragment() {

    private var _binding: FragmentAddDiscussionBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postButton.setOnClickListener {
            postDiscussion()
        }
    }

    private fun postDiscussion() {
        val name = binding.name.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val title = binding.discussionTitle.text.toString().trim()
        val content = binding.discussionContent.text.toString().trim()

        // Validate if necessary
        if (name.isEmpty() || email.isEmpty() || title.isEmpty() || content.isEmpty()) {
            showToast("Please fill in all fields.")
            return
        }

        // Create a map with discussion data
        val discussionData = hashMapOf(
            "name" to name,
            "email" to email,
            "title" to title,
            "content" to content
        )

        // Add the discussion to Firestore
        firestore.collection("discussions")
            .add(discussionData)
            .addOnSuccessListener {
                binding.name.text!!.clear()
                binding.email.text!!.clear()
                binding.discussionTitle.text!!.clear()
                binding.discussionContent.text!!.clear()
                showToast("Discussion posted successfully.")
            }
            .addOnFailureListener {
                showToast("Failed to post discussions.")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}