package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.repository.HouseRepository
import pt.isel.ls.houses.repository.LocationRepository

class HouseServices(
    private val data: DataRepository
) {
    fun createHouse(title: String, location: Int, areaSqMt: Double, pricePerNight: Double, description: String): House {
        if (data.getLocation(location) == null) throw LocationDoesNotExist()

        return data.createHouse(title, location, areaSqMt, pricePerNight, description)
    }

    fun getHouse(hid: Int) = data.getHouse(hid) ?: throw HouseDoesNotExist()

    fun getHouses(limit: Int?, skip: Int?) = data.getHouses(limit, skip)
}