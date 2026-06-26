package pt.isel.ls.houses.services

import pt.isel.ls.houses.HousePrediction
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.HouseDoesNotExist
import pt.isel.ls.houses.exceptions.HouseLocationMustBeLocality
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.predictPrice
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
        val user = userData.getUserByToken(token) ?: throw Unauthorized()


        if (locationData.getLocation(location) == null) throw LocationDoesNotExist()

        if (locationData.getLocation(location)!!.type != LocationType.LOCALITY) throw HouseLocationMustBeLocality()

        return houseData.createHouse(title, location, areaSqMt, pricePerNight, description, user.uid)
    }

    fun getHouse(hid: Int) = houseData.getHouse(hid) ?: throw HouseDoesNotExist()

    fun getHouses(limit: Int?, skip: Int?) = houseData.getHouses(limit, skip)

    fun getPredictedPrice(areaSqMt: Double, location: Int, nights: Int): Pair<Double, Double> {
        val locationValue = locationData.getLocation(location) ?: throw LocationDoesNotExist()
        val allHouses = getHouses(null, null)

        val relevantHouses = if (locationValue.type == LocationType.LOCALITY)
            allHouses.filter { it.location == location }
        else TODO("For case where it's an upper location, include all children recursively")

        val housePredictions = relevantHouses.map { HousePrediction(it.area, it.price) }

        val predictedPerNight = predictPrice(housePredictions, areaSqMt)
        return Pair(predictedPerNight, predictedPerNight * nights)
    }
}