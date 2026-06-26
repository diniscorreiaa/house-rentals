package pt.isel.ls.houses.webapi

import kotlinx.serialization.json.Json
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.path
import pt.isel.ls.houses.exceptions.InvalidIdentifier
import pt.isel.ls.houses.exceptions.RequiredPathParamNotFound
import pt.isel.ls.houses.exceptions.WrongTypeForQuery
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.webapi.dtos.CreateHouseIn
import pt.isel.ls.houses.webapi.dtos.CreateHouseOut
import pt.isel.ls.houses.webapi.dtos.GetHouseOut
import pt.isel.ls.houses.webapi.dtos.PredictPriceOut

class HouseWebApi(private val houseServices: HouseServices) {

    fun createHouse(request: Request): Response {
        val house = Json.decodeFromString<CreateHouseIn>(request.bodyString())
        val token = getToken(request)
        val newHouse = houseServices.createHouse(
            house.title,
            house.location,
            house.areaSqMt,
            house.pricePerNight,
            house.description,
            token
        )
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
            .body(
                Json.encodeToString(
                    GetHouseOut(
                        house.hid,
                        house.title,
                        house.location,
                        house.area,
                        house.price,
                        house.description,
                        house.owner
                    )
                )
            )
    }

    // TODO: verificar se é possível remover o parâmetro não usado request
    fun getHouses(request: Request): Response {
        val paging = getLimitAndSkip(request)

        val locationId = request.query("location")?.toIntOrNull()
        val name = request.query("name")

        val houses = houseServices.getHouses(locationId, name, paging.limit, paging.skip)
        return Response(OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(houses.map {
                GetHouseOut(
                    it.hid,
                    it.title,
                    it.location,
                    it.area,
                    it.price,
                    it.description,
                    it.owner
                )
            }))
    }

    fun getPredictedPrice(request: Request): Response {
        val areaSqMt = request.query("areaSqMt").let {
            it ?: throw RequiredPathParamNotFound("areaSqMt")
            it.toDoubleOrNull() ?: throw WrongTypeForQuery("areaSqMt", "number")
        }

        val location = request.query("location").let {
            it ?: throw RequiredPathParamNotFound("location")
            it.toIntOrNull() ?: throw InvalidIdentifier("location")
        }

        val nights = request.query("nights").let {
            it ?: throw RequiredPathParamNotFound("location")
            it.toIntOrNull() ?: throw WrongTypeForQuery("nights", "number")
        }

        val predictedPrice = houseServices.getPredictedPrice(areaSqMt, location, nights)
        return Response(OK)
            .header("content-type", "application/json")
            .body(
                Json.encodeToString(
                    PredictPriceOut(predictedPrice.first, predictedPrice.second)
                )
            )
    }
}