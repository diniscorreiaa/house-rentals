package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.LocationRepository

class LocationServices(
    private val locationsRepository: LocationRepository
) {
    fun createLocation(name: String, type: LocationType, parentId: Int?): Location {
        if (parentId != null) {
            getLocation(parentId)
        }
        return locationsRepository.createLocation(name, type, parentId)
    }

    fun getLocation(lid: Int): Location {
        return locationsRepository.getLocation(lid) ?: throw LocationDoesNotExist()
    }

    fun getChildLocations(lid: Int): List<Location> {
        if (locationsRepository.getLocation(lid) == null) {
            throw LocationDoesNotExist()
        }
        return locationsRepository.getChildLocations(lid)
    }

    fun getHierarchy(lid: Int): List<Location> {
        if (locationsRepository.getLocation(lid) == null) {
            throw LocationDoesNotExist()
        }
        return locationsRepository.getHierarchy(lid)
    }
}