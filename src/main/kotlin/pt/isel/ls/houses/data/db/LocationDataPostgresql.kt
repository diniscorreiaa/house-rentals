package pt.isel.ls.houses.data.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.LocationRepository
import java.sql.Types

class LocationDataPostgresql(val dataSource: PGSimpleDataSource): LocationRepository {
    override fun createLocation(
        name: String,
        type: LocationType,
        parentId: Int?
    ): Location {
        dataSource.connection.use {
            val st = it.prepareStatement("INSERT INTO LOCATION (LocationName, Type, ParentLocation) VALUES (?, ?, ?) RETURNING IdLocation")

            st.setString(1, name)
            st.setString(2, type.toString())
            if (parentId != null) st.setInt(3, parentId)
            else st.setNull(3, Types.INTEGER) // https://developer.android.com/reference/java/sql/PreparedStatement#setNull(int,%20int,%20java.lang.String):~:text=second%20is%202%2C%20...-,sqlType,int%3A%20a%20value%20from%20java.sql.Types,-typeName

            val rs = st.executeQuery()
            if (!rs.next()) throw InternalError() // TODO: Throws nesta camada?
            return Location(
                rs.getInt(1),
                name,
                type,
                parentId
            )
        }
    }

    override fun getLocation(lid: Int): Location? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM LOCATION WHERE IdLocation = ?")

            st.setInt(1, lid)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return Location(
                lid,
                rs.getString("LocationName"),
                LocationType.valueOf(rs.getString("Type")),
                rs.getInt("ParentLocation")
            )
        }
    }

    override fun getChildLocations(lid: Int): List<Location> {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM LOCATION WHERE ParentLocation = ?")

            st.setInt(1, lid)

            val rs = st.executeQuery()
            val locations = mutableListOf<Location>()
            while (rs.next()) {
                locations.add(
                    Location(
                        rs.getInt("IdLocation"),
                        rs.getString("LocationName"),
                        LocationType.valueOf(rs.getString("Type")),
                        lid
                    )
                )
            }
            return locations
        }
    }

    override fun getHierarchy(lid: Int): List<Location> {
        dataSource.connection.use {
            val st = it.prepareStatement( // https://www.postgresql.org/docs/current/queries-with.html#QUERIES-WITH-RECURSIVE
                """
                    WITH RECURSIVE HIERARCHY AS (
                        SELECT * 
                        FROM LOCATION
                        WHERE IdLocation = ?
                        
                        UNION ALL
                        
                        SELECT L.*
                        FROM LOCATION AS L
                        INNER JOIN HIERARCHY AS H ON L.IdLocation = H.ParentLocation
                    )
                    SELECT * FROM HIERARCHY
                """)

            st.setInt(1, lid)

            val rs = st.executeQuery()
            val hierarchy = mutableListOf<Location>()
            while (rs.next()) {
                hierarchy.add(
                    Location(
                        rs.getInt("IdLocation"),
                        rs.getString("LocationName"),
                        LocationType.valueOf(rs.getString("Type")),
                        rs.getInt("ParentLocation")
                    )
                )
            }

            return hierarchy.reversed() // Retornar hierarquia a começar na raiz
        }
    }
}