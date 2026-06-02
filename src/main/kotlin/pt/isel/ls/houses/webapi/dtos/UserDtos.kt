package pt.isel.ls.houses.webapi.dtos

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/////////// Input ///////////
@Serializable
data class CreateUserIn(val name: String, val email: String, val password: String)


/////////// Output ///////////
@Serializable
data class CreateUserOut(
    val uid: Int,
    val token: Uuid
)

@Serializable
data class GetUserOut(val uid: Int, val name: String, val email: String)