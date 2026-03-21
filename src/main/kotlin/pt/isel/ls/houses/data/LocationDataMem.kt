package pt.isel.ls.houses.data;

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.LocationRepository

class LocationDataMem : LocationRepository {
    val locations = LocationType.entries.associateWith { mutableListOf<Location>() } // TODO: Set???
    var id = 1

    override fun createLocation(
        name: String,
        type: LocationType,
        parentId: Int?
    ): Location {
        val loc = Location(id++, name, type, parentId)
        locations[type]?.add(loc) ?: throw InternalError() // TODO: Este é o melhor throw para este caso?
        return loc
    }

    override fun getLocation(lid: Int): Location? {
        locations.forEach { pair ->
            pair.value.find {
                it.lid == lid
            }.let { if (it != null) return it }
        }
        return null
    }

    override fun getChildLocations(lid: Int): List<Location> {
        val loc = getLocation(lid)
        val childIdx = LocationType.entries[loc!!.type.ordinal + 1]
        return locations[childIdx]?.filter { it.parentLocation == loc.lid } ?: throw InternalError()
    }

    override fun getHierarchy(lid: Int): List<Location> {
        TODO("Not yet implemented")
    }

}
