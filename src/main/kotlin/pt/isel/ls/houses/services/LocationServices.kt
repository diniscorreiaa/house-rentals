package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.repository.LocationRepository

class LocationServices(
    private val data: DataRepository
) {
    fun createLocation(name: String, type: LocationType, parentId: Int?): Location {
        if (parentId != null) getLocation(parentId)
        return data.createLocation(name, type, parentId)
    }

    fun getLocation(lid: Int): Location {
        return data.getLocation(lid) ?: throw LocationDoesNotExist()
    }

    fun getChildLocations(lid: Int): List<Location> {
        if (data.getLocation(lid) == null) throw LocationDoesNotExist()
        return data.getChildLocations(lid)
    }

    fun getHierarchy(lid: Int): List<Location> {
        if (data.getLocation(lid) == null) throw LocationDoesNotExist()
        return data.getHierarchy(lid)
    }
}