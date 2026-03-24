package pt.isel.ls.houses.webapi

import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import pt.isel.ls.houses.data.memory.HouseDataMem
import pt.isel.ls.houses.data.memory.LocationDataMem
import pt.isel.ls.houses.services.HouseServices

class HouseWebApi {
    private val houseWebApi = HouseServices(HouseDataMem(), LocationDataMem())

    fun createHouse(request: Request): Response {

        return Response(OK)
    }

    fun getHouse(request: Request): Response {

        return Response(OK)
    }

    fun getHouses(request: Request): Response {

        return Response(OK)
    }
}