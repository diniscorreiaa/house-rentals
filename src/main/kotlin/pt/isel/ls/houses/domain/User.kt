package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class User @OptIn(ExperimentalUuidApi::class) constructor(
    val idUser: Int,
    val name: String,
    val email: String,
    val token: Uuid
)