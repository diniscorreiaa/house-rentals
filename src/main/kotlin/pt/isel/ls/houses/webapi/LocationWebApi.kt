package pt.isel.ls.houses.webapi

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Invalid
import org.http4k.routing.path
import pt.isel.ls.houses.data.memory.LocationDataMem
import pt.isel.ls.houses.exceptions.InvalidIdentifier
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.RequiredPathParamNotFound
import pt.isel.ls.houses.services.LocationServices
import pt.isel.ls.houses.webapi.dtos.CreateLocationIn
import pt.isel.ls.houses.webapi.dtos.CreateLocationOut
import pt.isel.ls.houses.webapi.dtos.GetLocationOut

class LocationWebApi(val locationServices: LocationServices) {

    fun createLocation(request: Request): Response {
        val location = Json.decodeFromString<CreateLocationIn>(request.bodyString())
        val newLocation = locationServices.createLocation(location.name, location.type, location.parentLocation)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateLocationOut(newLocation.lid)))
    }

    fun getLocation(request: Request): Response {
        val locationId = request.path("lid").let {
            it ?: throw RequiredPathParamNotFound("lid")
            it.toIntOrNull() ?: throw InvalidIdentifier("lid")
        }

        val location = locationServices.getLocation(locationId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GetLocationOut(location.name, location.type)))
    }

    fun getChildLocations(request: Request): Response {
        val locationId = request.path("lid").let {
            it ?: throw RequiredPathParamNotFound("lid")
            it.toIntOrNull() ?: throw InvalidIdentifier("lid")
        }

        val locations = locationServices.getChildLocations(locationId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(locations.map { GetLocationOut(it.name, it.type) }))
    }

    fun getHierarchy(request: Request): Response {
        val locationId = request.path("lid").let {
            it ?: throw RequiredPathParamNotFound("lid")
            it.toIntOrNull() ?: throw InvalidIdentifier("lid")
        }

        val locations = locationServices.getHierarchy(locationId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(locations.map { GetLocationOut(it.name, it.type) }))
    }
}