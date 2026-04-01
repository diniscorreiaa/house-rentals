package pt.isel.ls.houses.repository

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType

interface LocationRepository {
    fun createLocation(name: String, type: LocationType, parentId: Int?): Location
    fun getLocation(lid: Int): Location?
    fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location>
    fun getHierarchy(lid: Int): List<Location> // not sure if it's the same
}