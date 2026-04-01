package pt.isel.ls.houses.data.memory

import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.exceptions.LocationDoesNotExist
import pt.isel.ls.houses.repository.LocationRepository

class LocationDataMem : LocationRepository {
    private val locations = LocationType.entries.associateWith { mutableListOf<Location>() } // TODO: Set???
    private var id = 1

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

    override fun getChildLocations(lid: Int, limit: Int?, skip: Int?): List<Location> {
        val loc = getLocation(lid) ?: throw LocationDoesNotExist()
        val childIdx = LocationType.entries[loc.type.ordinal + 1]
        val locations = locations[childIdx]?.filter { it.parentLocation == loc.lid } ?: throw InternalError()
        if (limit != null) return locations.subList(skip ?: 0, (skip ?: 0) + limit)
        return locations
    }

    // TODO: I'm depending a lot on !! which is not a good practice
    //  but it seems to work so it's fine for now, as we are short on time.
    //  Refactor later
    override fun getHierarchy(lid: Int): List<Location> {
        val loc = getLocation(lid) ?: throw LocationDoesNotExist()
        if (loc.type == LocationType.COUNTRY) return listOf(loc)

        val list = mutableListOf(loc)

        var parentId = loc.parentLocation!!     // assuming that since it's not LocationType.COUNTRY, it must have a parentId
        while (true) {
            val parent = getLocation(parentId)!!    // assuming that there's always a parent location until LocationType.COUNTRY
            list.addFirst(parent)
            parentId = parent.parentLocation ?: return list
        }
    }

}
