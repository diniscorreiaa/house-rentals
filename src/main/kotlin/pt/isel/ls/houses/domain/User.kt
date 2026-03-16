package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class User @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: Int, //unique identifier for the user
    val name: String,
    val email: String,
    val token: Uuid
)