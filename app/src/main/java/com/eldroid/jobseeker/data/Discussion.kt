package com.eldroid.jobseeker.data

data class Discussion(
    val name: String = "",
    val email: String = "",
    val title: String = "",
    val content: String = ""
) {
    // Default no-argument constructor required by Firestore
    constructor() : this("", "", "", "")
}
