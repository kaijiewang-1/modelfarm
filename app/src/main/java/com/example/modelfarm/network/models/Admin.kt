package com.example.modelfarm.network.models

data class AdminGetUsers(
    val users: List<UserWitnRole>
)


data class UserWitnRole(
    val user: User,
    val role: String
)