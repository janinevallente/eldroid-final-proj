package com.eldroid.jobseeker.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eldroid.jobseeker.data.Discussion
import com.google.firebase.firestore.FirebaseFirestore

class DiscussionViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _discussions = MutableLiveData<List<Discussion>>()
    val discussions: LiveData<List<Discussion>> get() = _discussions

    fun loadDiscussions() {
        firestore.collection("discussions")
            .get()
            .addOnSuccessListener { documents ->
                val discussionList = mutableListOf<Discussion>()
                for (document in documents) {
                    val discussion = document.toObject(Discussion::class.java)
                    discussionList.add(discussion)
                }
                _discussions.value = discussionList
            }
            .addOnFailureListener {
                Log.d("DiscussionViewModel","Firestore Error")
            }
    }
}