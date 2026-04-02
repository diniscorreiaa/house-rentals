package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.repository.HouseRepository
import pt.isel.ls.houses.repository.LocationRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HouseServices(
    private val data: DataRepository
) {
    @OptIn(ExperimentalUuidApi::class)
    fun createHouse(title: String, location: Int, areaSqMt: Double, pricePerNight: Double, description: String, token: Uuid): House {
        data.getUserByToken(token) ?: throw Unauthorized()


        if (data.getLocation(location) == null) throw LocationDoesNotExist()

        return data.createHouse(title, location, areaSqMt, pricePerNight, description)
    }

    fun getHouse(hid: Int) = data.getHouse(hid) ?: throw HouseDoesNotExist()

    fun getHouses(limit: Int?, skip: Int?) = data.getHouses(limit, skip)
}