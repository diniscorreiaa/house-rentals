package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid


data class User(
    val uid: Int,
    val name: String,
    val email: String,
    val token: Uuid,
    val password: String
)