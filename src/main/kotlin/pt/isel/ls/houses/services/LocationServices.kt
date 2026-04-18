package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationAlreadyExists
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
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

        if (parentId != null) {
            getLocation(parentId)

            // TODO: We are only making sure that locations WITH parentIds are not duplicate. those without
            //  parentId (countries) can still be duplicate. Data layer will need changes to support this functionality
            getChildLocations(parentId, null, null).forEach {
                if (it.name.equals(name, ignoreCase = true)) throw LocationAlreadyExists()
            }
        }

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
}