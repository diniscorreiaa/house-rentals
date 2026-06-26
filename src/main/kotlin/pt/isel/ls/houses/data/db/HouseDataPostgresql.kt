package pt.isel.ls.houses.data.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.HouseCache
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.HouseRepository

const val HOUSE_DETAILS_CACHE_SIZE = 100

class HouseDataPostgresql(val dataSource: PGSimpleDataSource) : HouseRepository {
    val houseDetailsCache = HouseCache(HOUSE_DETAILS_CACHE_SIZE)

    override fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String,
        owner: Int
    ): House {
        dataSource.connection.use {
            val st =
                it.prepareStatement("INSERT INTO HOUSE (\"Title\", \"Location\", \"Area\", \"Price\", \"Description\", \"Owner\") VALUES (?, ?, ?, ?, ?, ?) RETURNING \"IdHouse\"")

            st.setString(1, title)
            st.setInt(2, location)
            st.setDouble(3, areaSqMt)
            st.setDouble(4, pricePerNight)
            st.setString(5, description)
            st.setInt(6, owner)

            val rs = st.executeQuery()
            if (!rs.next()) throw InternalError() // TODO: Faz sentido lançar erros no dataDB?
            return House(rs.getInt(1), title, location, areaSqMt, pricePerNight, description, owner)
        }
    }

    override fun getHouse(hid: Int): House? {
        houseDetailsCache.getHouse(hid).let { if (it != null) return it }

        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM HOUSE WHERE \"IdHouse\" = ?")
            st.setInt(1, hid)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return House(
                hid,
                rs.getString("Title"),
                rs.getInt("Location"),
                rs.getDouble("Area"),
                rs.getDouble("Price"),
                rs.getString("Description"),
                rs.getInt("Owner")
            ).also { house -> houseDetailsCache.putHouse(house) }
        }
    }

    override fun getHouses(locId: Int?, name: String?, limit: Int?, skip: Int?): List<House> {
        dataSource.connection.use {
            var query = "SELECT * FROM HOUSE"
            var containsWhere = false

            if(locId != null) {
                query += " WHERE \"Location\" = ?"
                containsWhere = true
            }

            if (!name.isNullOrBlank()) {
                if(!containsWhere) query += " WHERE LOWER(\"Title\") LIKE LOWER(?)"
                else query += " AND LOWER(\"Title\") LIKE LOWER(?)"
            }

            if (limit != null) query += " ORDER BY \"IdHouse\" LIMIT ? OFFSET ?"

            val st = it.prepareStatement(query)

            var paramIdx = 1

            if (locId != null) st.setInt(paramIdx++, locId)
            if (!name.isNullOrBlank()) st.setString(paramIdx++, "%$name%")
            if (limit != null) {
                st.setInt(paramIdx++, limit)
                st.setInt(paramIdx, skip ?: 0)
            }

            val rs = st.executeQuery()

            val houses = mutableListOf<House>()

            while (rs.next()) {
                houses.add(
                    House(
                        rs.getInt("IdHouse"),
                        rs.getString("Title"),
                        rs.getInt("Location"),
                        rs.getDouble("Area"),
                        rs.getDouble("Price"),
                        rs.getString("Description"),
                        rs.getInt("Owner")
                    )
                )
            }

            return houses
        }
    }

}