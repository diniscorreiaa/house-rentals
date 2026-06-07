package pt.isel.ls.houses.webapi.dtos

import kotlinx.serialization.Serializable
import kotlin.String
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

@Serializable
data class UserLoginIn(val username: String, val password: String)

@Serializable
data class UserLoginOut(val token: Uuid, val uid: Int)
