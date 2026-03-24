package pt.isel.ls.houses.webapi.dtos

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class CreateUser @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: Int,
    val token: Uuid
)

data class GetUser (val uid: Int, val name: String, val email: String)