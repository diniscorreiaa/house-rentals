package pt.isel.ls.houses.data.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.HouseRepository

class HouseDataPostgresql(val dataSource: PGSimpleDataSource): HouseRepository {
    override fun createHouse(
        title: String,
        location: Int,
        areaSqMt: Double,
        pricePerNight: Double,
        description: String
    ): House {
        dataSource.connection.use {
            val st = it.prepareStatement("INSERT INTO HOUSE (Title, Location, Area, Price, Description) VALUES (?, ?, ?, ?, ?) RETURNING IdHouse")

            st.setString(1, title)
            st.setInt(2, location)
            st.setDouble(3, areaSqMt)
            st.setDouble(4, pricePerNight)
            st.setString(5, description)

            val rs = st.executeQuery()
            if (!rs.next()) throw InternalError() // TODO: Faz sentido lançar erros no dataDB?
            return House(rs.getInt(1), title, location, areaSqMt, pricePerNight, description)
        }
    }

    override fun getHouse(hid: Int): House? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM HOUSE WHERE IdHouse = ?")
            st.setInt(1, hid)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return House(
                hid,
                rs.getString("Title"),
                rs.getInt("Location"),
                rs.getDouble("Area"),
                rs.getDouble("Price"),
                rs.getString("Description")
                )
        }
    }

    override fun getHouses(): List<House> {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM HOUSE")

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
                        rs.getString("Description")
                    )
                )
            }

            return houses
        }
    }

}