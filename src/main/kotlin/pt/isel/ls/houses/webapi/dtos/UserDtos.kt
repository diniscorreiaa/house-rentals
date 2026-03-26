package pt.isel.ls.houses.webapi.dtos

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/////////// Input ///////////

data class CreateUserIn(val name: String, val email: String)


/////////// Output ///////////

data class CreateUserOut @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: Int,
    val token: Uuid
)

data class GetUserOut (val uid: Int, val name: String, val email: String)