package pt.isel.ls.houses.webapi

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.exceptions.InvalidIdentifier
import pt.isel.ls.houses.exceptions.RequiredPathParamNotFound
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.webapi.dtos.CreateHouseIn
import pt.isel.ls.houses.webapi.dtos.CreateHouseOut
import pt.isel.ls.houses.webapi.dtos.GetHouseOut

class HouseWebApi(private val houseServices: HouseServices, private val data: DataMem) {

    fun createHouse(request: Request): Response {
        verifyAndGetAuth(request, data)
        val house = Json.decodeFromString<CreateHouseIn>(request.bodyString())
        val newHouse = houseServices.createHouse(house.title, house.location, house.area, house.price, house.description)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(CreateHouseOut(newHouse.hid)))
    }

    fun getHouse(request: Request): Response {
        val houseId = request.path("hid").let {
            it ?: throw RequiredPathParamNotFound("hid")
            it.toIntOrNull() ?: throw InvalidIdentifier("hid")
        }

        val house = houseServices.getHouse(houseId)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(GetHouseOut(house.title, house.location, house.area, house.price, house.description)))
    }

    // TODO: verificar se é possível remover o parâmetro não usado request
    fun getHouses(request: Request): Response {
        val houses = houseServices.getHouses()
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(houses.map { GetHouseOut(it.title, it.location, it.area, it.price, it.description) }))
    }
}