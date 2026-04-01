package pt.isel.ls.houses.services

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.LocationAlreadyExists
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.DataRepository

class LocationServices(
    private val data: DataRepository
) {
    fun createLocation(name: String, type: LocationType, parentId: Int?): Location {
        if (parentId != null) {
            getLocation(parentId)

            // TODO: We are only making sure that locations WITH parentIds are not duplicate. those without
            //  parentId (countries) can still be duplicate. Data layer will need changes to support this functionality
            getChildLocations(parentId).forEach {
                if (it.name.equals(name, ignoreCase = true)) throw LocationAlreadyExists()
            }
        }

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