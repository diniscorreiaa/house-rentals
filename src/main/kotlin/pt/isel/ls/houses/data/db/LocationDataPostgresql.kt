package pt.isel.ls.houses.data.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.repository.LocationRepository

class LocationDataPostgresql(val dataSource: PGSimpleDataSource): LocationRepository {
    override fun createLocation(
        name: String,
        type: LocationType,
        parentId: Int?
    ): Location {
        TODO("Not yet implemented")
    }

    override fun getLocation(lid: Int): Location? {
        TODO("Not yet implemented")
    }

    override fun getChildLocations(lid: Int): List<Location> {
        TODO("Not yet implemented")
    }

    override fun getHierarchy(lid: Int): List<Location> {
        TODO("Not yet implemented")
    }
}