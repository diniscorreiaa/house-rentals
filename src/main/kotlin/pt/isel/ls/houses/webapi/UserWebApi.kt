package pt.isel.ls.houses.webapi

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.exceptions.InvalidIdentifier
import pt.isel.ls.houses.exceptions.RequiredPathParamNotFound
import pt.isel.ls.houses.services.UserServices
import pt.isel.ls.houses.webapi.dtos.CreateUserIn
import pt.isel.ls.houses.webapi.dtos.CreateUserOut
import pt.isel.ls.houses.webapi.dtos.GetUserOut

class UserWebApi(private val userServices: UserServices) {

    fun createUser(request: Request): Response {
        val user = Json.decodeFromString<CreateUserIn>(request.bodyString())
        val newUser = userServices.createUser(user.name, user.email)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateUserOut(newUser.uid, newUser.token)))
    }

    fun getUser(request: Request): Response {
        val userId = request.path("uid").let {
            it ?: throw RequiredPathParamNotFound("uid")
            it.toIntOrNull() ?: throw InvalidIdentifier("uid")
        }

        val getUser = userServices.getUser(userId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GetUserOut(getUser.uid, getUser.name, getUser.email)))
    }
}