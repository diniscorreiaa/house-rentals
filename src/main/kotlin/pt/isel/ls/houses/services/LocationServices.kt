package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationAlreadyExists
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.Uuid

class LocationServices(
    private val data: DataRepository
) {
    fun createLocation(name: String, type: LocationType, parentId: Int?, token: Uuid): Location {
        data.getUserByToken(token) ?: throw Unauthorized()

        // If location already exists then we don't want to create it again
        if (data.getAllLocations().find { it == Location(0, name, type, parentId) } != null )
            throw LocationAlreadyExists()

        return data.createLocation(name, type, parentId)
    }

    fun getLocation(lid: Int) = data.getLocation(lid) ?: throw LocationDoesNotExist()

    fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location> {
        if (data.getLocation(lid) == null) throw LocationDoesNotExist()
        return data.getChildLocations(lid, limit, skip)
    }

    fun getHierarchy(lid: Int): List<Location> {
        if (data.getLocation(lid) == null) throw LocationDoesNotExist()
        return data.getHierarchy(lid)
    }
}