package com.eldroid.jobseeker.data

data class Job(
    val id: String = "",
    val jobPosition: String = "",
    val company: String = "",
    val location: String = "",
    val jobContent: String = "",
    val salary: String = "",
    val isBookmarked: Boolean
){
    // Default no-argument constructor required by Firestore
    constructor() : this("", "", "", "",""," ",true)
}