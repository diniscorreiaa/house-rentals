package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.repository.HouseRepository
import pt.isel.ls.houses.repository.LocationRepository
import pt.isel.ls.houses.repository.UserRepository
import kotlin.uuid.Uuid

class HouseServices(
    private val userData: UserRepository,
    private val locationData: LocationRepository,
    private val houseData: HouseRepository
) {
    fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String,
        token: Uuid
    ): House {
        userData.getUserByToken(token) ?: throw Unauthorized()


        if (locationData.getLocation(location) == null) throw LocationDoesNotExist()

        return houseData.createHouse(title, location, areaSqMt, pricePerNight, description)
    }

    fun getHouse(hid: Int) = houseData.getHouse(hid) ?: throw HouseDoesNotExist()

    fun getHouses(limit: Int?, skip: Int?) = houseData.getHouses(limit, skip)
}