package pt.isel.ls.houses.webapi

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.data.memory.UserDataMem
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.UserDoesNotExist
import pt.isel.ls.houses.services.UserServices
import pt.isel.ls.houses.webapi.dtos.CreateUser
import pt.isel.ls.houses.webapi.dtos.GetUser
import kotlin.uuid.ExperimentalUuidApi

class UserWebApi {
    private val userServices = UserServices(UserDataMem())

    @OptIn(ExperimentalUuidApi::class)
    fun createUser(request: Request): Response {
        val user = Json.decodeFromString<User>(request.bodyString())
        val newUser = userServices.createUser(user.name, user.email)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateUser(newUser.uid, newUser.token)))
    }

    fun getUser(request: Request): Response {
        val userId = request.path("uid") ?: throw UserDoesNotExist()
        val getUser = userServices.getUser(userId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GetUser(getUser.uid, getUser.name, getUser.email)))
    }
}