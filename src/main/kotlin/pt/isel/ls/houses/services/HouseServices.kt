package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.HouseRepository
import pt.isel.ls.houses.repository.LocationRepository

class HouseServices(
    private val houseRepository: HouseRepository,
    private val locationRepository: LocationRepository
) {
    fun createHouse(title: String, location: Int, areaSqMt: Double, pricePerNight: Double, description: String): House {
        if (locationRepository.getLocation(location) == null) throw LocationDoesNotExist()

        return houseRepository.createHouse(title, location, areaSqMt, pricePerNight, description)
    }

    fun getHouse(hid: Int) = houseRepository.getHouse(hid) ?: throw HouseDoesNotExist()

    fun getHouses() = houseRepository.getHouses()
}