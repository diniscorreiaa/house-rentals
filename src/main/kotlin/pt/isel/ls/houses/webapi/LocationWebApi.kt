package pt.isel.ls.houses.webapi

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.houses.data.memory.LocationDataMem
import pt.isel.ls.houses.services.LocationServices

class LocationWebApi {
    private val locationServices = LocationServices(LocationDataMem())

    fun createLocation(request: Request): Response {

        return Response(OK)
    }

    fun getLocation(request: Request): Response {

        return Response(OK)
    }

    fun getChildLocations(request: Request): Response {

        return Response(OK)
    }

    fun getHierarchy(request: Request): Response {

        return Response(OK)
    }
}