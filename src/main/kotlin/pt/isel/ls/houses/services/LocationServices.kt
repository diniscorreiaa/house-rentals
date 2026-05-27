package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationAlreadyExists
import pt.isel.ls.houses.exceptions.LocationCountryMustNotHaveParent
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.exceptions.LocationMustHaveParent
import pt.isel.ls.houses.exceptions.LocationParentDoesNotExist
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.repository.LocationRepository
import pt.isel.ls.houses.repository.UserRepository
import kotlin.uuid.Uuid

class LocationServices(
    private val userData: UserRepository,
    private val locationData: LocationRepository
) {
    fun createLocation(name: String, type: LocationType, parentId: Int?, token: Uuid): Location {
        userData.getUserByToken(token) ?: throw Unauthorized()

        // If location already exists then we don't want to create it again
        if (locationData.getAllLocations().find { it == Location(0, name, type, parentId) } != null )
            throw LocationAlreadyExists()

        if (type != LocationType.COUNTRY && parentId == null) throw LocationMustHaveParent()

        if (type == LocationType.COUNTRY && parentId != null) throw LocationCountryMustNotHaveParent()

        if (parentId != null && locationData.getLocation(parentId) == null) throw LocationParentDoesNotExist()

        return locationData.createLocation(name, type, parentId)
    }

    fun getLocation(lid: Int) = locationData.getLocation(lid) ?: throw LocationDoesNotExist()

    fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location> {
        if (locationData.getLocation(lid) == null) throw LocationDoesNotExist()
        return locationData.getChildLocations(lid, limit, skip)
    }

    fun getHierarchy(lid: Int): List<Location> {
        if (locationData.getLocation(lid) == null) throw LocationDoesNotExist()
        return locationData.getHierarchy(lid)
    }

    fun getLocationsFromType(type: LocationType, limit: Int?, skip: Int?): List<Location>? = locationData.getLocationsFromType(type, limit, skip)
}